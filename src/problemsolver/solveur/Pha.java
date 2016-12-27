/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.solveur;

import java.util.HashMap;
import java.util.Set;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.Circuit_Hamiltonien;
import problemsolver.donnees.solutions.Circuit_TourReference;
import problemsolver.exceptions.ErreurDonneesException;
import problemsolver.probleme.DonneesScenario;
import problemsolver.probleme.Echantillon;
import problemsolver.probleme.PhiLambda;
import problemsolver.probleme.Probleme;
import problemsolver.probleme.Probleme_Stochastique;

/**
 *
 * @author Clément
 */
public class Pha extends Solveur<Probleme_Stochastique<Graphe_Complet, Circuit_Hamiltonien, DonneesScenario<Graphe_Complet, Arete, PhiLambda>, Circuit_TourReference>>{
	private int nombreScenarios;
	private Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> secondSolveur;
	private int variation;
	private int pourcentDet;
	private static final int DEFVAR = 20, DEFPER = 20;

	public Pha(int nbrS, Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> secondS, int var, int pDet) {
		nombreScenarios = nbrS;
		secondSolveur = secondS;
		variation = var;
		pourcentDet = pDet;

		//création du solveur secondaire
		secondSolveur.setProbleme(getProbleme());
		secondSolveur.setAffiche(false);
	}

	public Pha(int nbrS, Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> secondS) {
		this(nbrS, secondS, DEFVAR, DEFPER);
	}
	
	// dinittiales ne sert jamais, c'est pas normal, on ne devrait pas le modifié, il contient les données parser, on devrait modifier une copie
	// Pour le SAA il vaudrait mieux que resoudre prenne directement une liste de scenarios
	@Override
	public Circuit_Hamiltonien resoudre(Graphe_Complet dinitiales, Circuit_Hamiltonien solInit, boolean minimiser) throws ErreurDonneesException {
		double t = 0;
		double t_max = 10000;
		boolean continuer;
		
		// Configurer le problème comme stochastique
		getProbleme().setUseStochastique(false);

		// Création des scénarios et transformation du graphe actuel en stochastique avec les variations indiquées
		getProbleme().initialiserScenarios(variation, 100, nombreScenarios);
		
		//Initialisation du tour de référence
		getProbleme().initialiserTourRef(getProbleme().getDonnees(), getProbleme().getJeu().clone());
		
		Circuit_Hamiltonien meilleurSolution = solInit;
		Circuit_Hamiltonien realBestSolution = solInit;
		// Création des scénarios avec les solutions du recuit
		HashMap<Graphe_Complet, Circuit> listSolution = new HashMap<Graphe_Complet, Circuit>();
		for(Graphe_Complet graphe: (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()) {
			meilleurSolution = (Circuit_Hamiltonien) secondSolveur.resoudre(graphe.clone(), meilleurSolution.clone(), minimiser);
			listSolution.put(graphe, meilleurSolution.clone());
			
			if(meilleurSolution.distanceTotale() < realBestSolution.distanceTotale())
				realBestSolution = meilleurSolution.clone();
		}

		// Création et calcul du tour de référence à partir des scénarios initiés
		getProbleme().getTourRef().calculer(listSolution.values());
		
		do{
			t = t + 1;
			
			// Nettoyer les solutions initiales des scénarios
			listSolution.clear();

			// Recalculer les solutions des scénarios de données avec le recuit
			for(Graphe_Complet scen: (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()){
				meilleurSolution = (Circuit_Hamiltonien) secondSolveur.resoudre(scen.clone(), meilleurSolution.clone(), minimiser);
				listSolution.put(scen, meilleurSolution);
				
				if(meilleurSolution.distanceTotale() < realBestSolution.distanceTotale())
					realBestSolution = meilleurSolution;
			}
			// Création et calcul du tour de référence à partir des scénarios initiés
			getProbleme().getTourRef().calculer(listSolution.values());
			
			continuer = true;
			for(Graphe_Complet d : listSolution.keySet()){
				
				// Permet de garder b à false pour la suite de la boucle en calculant les pénalités
				continuer = (getProbleme().getDonnees().getPenalites(d).ajuster(getProbleme().getTourRef(),listSolution.get(d)) && continuer);
				// Calcul des pénalités
				// getProbleme().getDonnees().getPenalites(d).ajuster(getProbleme().getTourRef(),listSolution.get(d));
			}
		}while(!continuer && t < t_max);
		//Afficheur.infoDialog("Terminé en "+t+" tours"); // uncomment to get annoying messages popoing up into your face

		return realBestSolution; //getProbleme().getTourRef();
	}
	
	@Override
	public Circuit_Hamiltonien resoudre(Graphe_Complet dinitiales, Circuit_Hamiltonien solInit, boolean minimiser,Echantillon echantillon) throws ErreurDonneesException{
		double t = 0;
		double t_max = 10000;
		boolean continuer;
		
		// Configurer le problème comme stochastique
		getProbleme().setUseStochastique(true);
		
		Circuit_Hamiltonien meilleurSolution = solInit;
		Circuit_Hamiltonien realBestSolution = solInit;
		// Création des scénarios avec les solutions du recuit
		HashMap<Graphe_Complet, Circuit> listSolution = new HashMap<Graphe_Complet, Circuit>();
		for(Graphe_Complet graphe: (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()) {
			meilleurSolution = (Circuit_Hamiltonien) secondSolveur.resoudre(graphe.clone(), meilleurSolution.clone(), minimiser);
			listSolution.put(graphe, meilleurSolution.clone());
			
			if(meilleurSolution.distanceTotale() < realBestSolution.distanceTotale())
				realBestSolution = meilleurSolution.clone();
		}
		
		getProbleme().initialiserTourRef(getProbleme().getDonnees(), getProbleme().getJeu().clone());
		// Création et calcul du tour de référence à partir des scénarios initiés
		getProbleme().getTourRef().calculer(listSolution.values());
		
		do{
			t = t + 1;
			
			// Nettoyer les solutions initiales des scénarios
			listSolution.clear();

			// Recalculer les solutions des scénarios de données avec le recuit
			for(Graphe_Complet scen: (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()){
				meilleurSolution = (Circuit_Hamiltonien) secondSolveur.resoudre(scen.clone(), meilleurSolution.clone(), minimiser);
				listSolution.put(scen, meilleurSolution);
				
				if(meilleurSolution.distanceTotale() < realBestSolution.distanceTotale())
					realBestSolution = meilleurSolution;
			}
			// Création et calcul du tour de référence à partir des scénarios initiés
			getProbleme().getTourRef().calculer(listSolution.values());
			
			continuer = true;
			for(Graphe_Complet d : listSolution.keySet()){
				
				// Permet de garder b à false pour la suite de la boucle en calculant les pénalités
				continuer = (getProbleme().getDonnees().getPenalites(d).ajuster(getProbleme().getTourRef(),listSolution.get(d)) && continuer);
				// Calcul des pénalités
				// getProbleme().getDonnees().getPenalites(d).ajuster(getProbleme().getTourRef(),listSolution.get(d));
			}
		}while(!continuer && t < t_max);
		//Afficheur.infoDialog("Terminé en "+t+" tours"); // uncomment to get annoying messages popoing up into your face

		return realBestSolution; //getProbleme().getTourRef();
	}

	public String toStringAvgDet(){
		return "Moyenne des données déterministes "+donneesString();
	}
	
	@Override
	public String toString(){
		return "pha";
	}

	public String donneesString(){
		if(variation == DEFVAR && pourcentDet == DEFPER)
			return "("+nombreScenarios+" scenarios, avec "+secondSolveur+")";
		else
			return "("+nombreScenarios+" scenarios, variation "+variation+"% et "+pourcentDet+"% déterminisne, avec "+secondSolveur+")";
	}

	public Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> getSolveur() {
		return secondSolveur;
	}

	
	
}
