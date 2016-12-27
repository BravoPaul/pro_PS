package problemsolver.solveur;

import java.util.ArrayList;
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

public class SAA extends Solveur<Probleme_Stochastique<Graphe_Complet, Circuit_Hamiltonien, DonneesScenario<Graphe_Complet, Arete, PhiLambda>, Circuit_TourReference>> {
	int nombreEchantillons;
	ArrayList<Echantillon> listeEchantillon = new ArrayList<Echantillon>();;
	Echantillon referanceEchantillon = new Echantillon();
	Echantillon EchRef;
	Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> solveurSecondaire;

	public SAA(int nbrS, Solveur<Probleme_Stochastique<Graphe_Complet, Circuit_Hamiltonien, DonneesScenario<Graphe_Complet, Arete, PhiLambda>, Circuit_TourReference>> secondS) {
		nombreEchantillons = nbrS;
		//solveurSecondaire = secondS;
	}

	@Override
	public Circuit resoudre(Graphe_Complet donnees, Circuit_Hamiltonien solution, boolean minimiser) throws ErreurDonneesException {
		solveurSecondaire = new Recuit();
		solveurSecondaire.setProbleme(getProbleme());
		solveurSecondaire.getProbleme().setUseStochastique(false);
		solveurSecondaire.setAffiche(false);
		getProbleme().setUseStochastique(false);

		getProbleme().initialiserScenarios(20, 100, nombreEchantillons*nombreEchantillons);
		//getProbleme().initialiserTourRef(getProbleme().getDonnees(), getProbleme().getJeu());

		Circuit_Hamiltonien meilleurSolution = solution;
		Circuit_Hamiltonien realBestSolution = solution;
		// Création des scénarios avec les solutions du recuit
		HashMap<Graphe_Complet, Circuit> listSolution = new HashMap<Graphe_Complet, Circuit>();
		for(Graphe_Complet graphe: (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()) {
			meilleurSolution = (Circuit_Hamiltonien) solveurSecondaire.resoudre(graphe.clone(), meilleurSolution.clone(), minimiser);
			listSolution.put(graphe, meilleurSolution.clone());
			
			if(meilleurSolution.distanceTotale() < realBestSolution.distanceTotale())
				realBestSolution = meilleurSolution.clone();
		}

		// Création et calcul du tour de référence à partir des scénarios initiés
		//getProbleme().getTourRef().calculer(listSolution.values());
		
		return realBestSolution;
		
		/*
		// di yi ci zhu shi
		int nombreDeScenario = getProbleme().getDonnees().getScenarios().size();

		int increment = nombreDeScenario / nombreEchantillons;
		Echantillon echantillon = new Echantillon();
		for (Graphe_Complet scen : (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()) {
			referanceEchantillon.add(scen);
			if (nombreDeScenario / nombreEchantillons == increment) {
				echantillon = new Echantillon();
				listeEchantillon.add(echantillon);
				increment = 0;
			}
			echantillon.add(scen);
			increment++;
		}

		Circuit_Hamiltonien min_CR = null;
		for (Echantillon ech : listeEchantillon) {
			//Circuit_Hamiltonien echantillonTr =  (Circuit_Hamiltonien) solveurSecondaire.resoudre(donnees,solution, minimiser, listeEchantillon.get(i));
			//HashSet<Arete> hashSet = new HashSet<Arete>();
			//hashSet.addAll(echantillonTr.getParcourt());
			//getProbleme().initialiserTourRefSaa(new Circuit_TourReference(hashSet, echantillonTr.getGraphe()));
			Circuit_Hamiltonien resultatRr = (Circuit_Hamiltonien) solveurSecondaire.resoudre(donnees.clone(),solution.clone(), minimiser, ech);
			if(min_CR==null)
				min_CR = resultatRr;
			else if (resultatRr.distanceTotale() < resultatRr.distanceTotale()) { 
				min_CR = resultatRr;
			}
		}
		//System.out.println("min_CR:"+getDistance(min_CR.getKeySet()));
		return min_CR;*/
	}

	
	@Override
	public String toString(){
		return "saa";
	}

}
