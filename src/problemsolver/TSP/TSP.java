/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.TSP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.Noeud;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.Circuit_Hamiltonien;
import problemsolver.donnees.solutions.Circuit_TourReference;
import problemsolver.donnees.solutions.TourReference;
import problemsolver.exceptions.ErreurDonneesException;
import problemsolver.parser.Parse_Graphe_SAX;
import problemsolver.probleme.DonneesScenario;
import problemsolver.probleme.PhiLambda;
import problemsolver.probleme.Probleme_Stochastique;

/**
 *
 * @author Clément
 */
public class TSP extends Probleme_Stochastique<Graphe_Complet, Circuit_Hamiltonien, DonneesScenario<Graphe_Complet, Arete, PhiLambda>, Circuit_TourReference>{

	private HashSet<Noeud> intouchable;

	public TSP() {
		super(new Parse_Graphe_SAX()); //TODO change to Parse_Graphe() to come back to previous version
		intouchable = new HashSet<Noeud>();
	}

	@Override
	protected double fonctionObjectifStochastique(Graphe_Complet gC, Circuit_Hamiltonien circuit) {
		double valeurTotale = this.fonctionObjectif(gC, circuit);
		double valeurPhi = 0;
		HashSet<Arete> hashSetAretes = getDonnees().getAretesDeterministes();
		for(Arete arete : hashSetAretes){
			if(circuit.getParcourt().contains(arete)){
				valeurTotale += (1 - this.getTourRef().getValeur(arete)) * this.getDonnees().getPenalites(gC).getLambda(arete);
				valeurPhi += Math.pow((1 - this.getTourRef().getValeur(arete)) * this.getDonnees().getPenalites(gC).getPhi(arete), 2);
			}else{
				valeurTotale += (0 - this.getTourRef().getValeur(arete)) * this.getDonnees().getPenalites(gC).getLambda(arete);
				valeurPhi += Math.pow((0 - this.getTourRef().getValeur(arete)) * this.getDonnees().getPenalites(gC).getPhi(arete), 2);
			}
		}

		valeurTotale += valeurPhi/2;
		return valeurTotale;
	}

	/**
	 * retourne un objet DonnéesScenario qui contient nombre de scénarios 
	 */
	@Override
	protected DonneesScenario<Graphe_Complet, Arete, PhiLambda> creerScenarios(double variation, double pourcentDet, int nombre) throws ErreurDonneesException {
		HashMap<Graphe_Complet, PhiLambda> hashMapscenariosAndPenality = new HashMap<Graphe_Complet, PhiLambda>();
		int nombreDet = (int) (getJeu().clone().getListAretes().size()*(pourcentDet/100));
		HashSet<Arete> hashSetArretesDetSelected = new HashSet<Arete>();
		//HashSet<Arete> hashSetArretesStoSelected = new HashSet<Arete>();
		for(int i = 0; i < nombreDet; i++){
			int choix = (int) (Math.random()*getJeu().getListAretes().size());
			while(hashSetArretesDetSelected.contains(getJeu().getListAretes().get(choix))){
				// Le choix doit toujours etre different
				choix = (int) (Math.random()*getJeu().getListAretes().size());
			}
			hashSetArretesDetSelected.add(getJeu().getListAretes().get(choix));
		}
		
		/*
		for (Arete arr : getJeu().clone().getListAretes()){
			if(hashSetArretesDetSelected.contains(arr)){}
			else hashSetArretesStoSelected.add(arr);
		}*/
		
		for(int i = 0; i < nombre; i++){
			// Le graphe actuel change plein de fois pour prendre en compte les valeurs devenues stochastiques : Florian 
			// TODO il y a un problème ici car on modifie à chaque fois le graph parser (voir Graphe_Complet(Jeu, var, liste_des_arretes_stochqstiques), je pense que seul les données deterministes ne changes pas mais le reste vaut 0 comme on peut le voir dans les logs du problème
			// Losrqu'on relance la résolution, les données stochastiques changes et seul les arretes qui étaient deterministes et le restent ne valent pas 0 d'ou la baisse de la fonction objectif à cahque relance : Florian
			Graphe_Complet graphe = new Graphe_Complet(getJeu().clone(), variation, hashSetArretesDetSelected);
			// Je voudrais afficher le graphe cree pour voir ce qu'il donne
			
			hashMapscenariosAndPenality.put(graphe, new PhiLambda(hashSetArretesDetSelected));
		}

		// DonneesScenario regroupe des graphes et leurs pénalités
		return new DonneesScenario<Graphe_Complet, Arete, PhiLambda>(hashMapscenariosAndPenality, hashSetArretesDetSelected);
	}

	/**
	 * retourne un tour de référence à partir des données fournises
	 */
	protected Circuit_TourReference creerTourRef(DonneesScenario<Graphe_Complet, Arete, PhiLambda> ar, Graphe_Complet d) throws ErreurDonneesException{
		return new Circuit_TourReference(ar.getAretesDeterministes(), d);
	}

	/**
	 * retroune la valeur de la fonction objectif 
	 */
	@Override
	protected double fonctionObjectif(Graphe_Complet graphe, Circuit_Hamiltonien solution) {
		return solution.distanceTotale();
	}

	/**
	 * retounre le circuit hamiltonien des noeuds dans l'ordre du problème
	 */
	@Override
	public Circuit_Hamiltonien solutionInitialBase() throws ErreurDonneesException {
		ArrayList<Noeud> parcourt = new ArrayList<Noeud>();
		for(Noeud n:getJeu().getListNoeuds()){
			parcourt.add(n);
		}
		return new Circuit_Hamiltonien(parcourt, getJeu());
	}

	/**
	 * retourne un voisin du circuit passé en paramètre
	 */
	@Override
	public Circuit_Hamiltonien voisinageBase(Circuit_Hamiltonien solution) {
		int a, b;
		do{
			a = (int) (Math.random()*solution.getNombreNoeuds());
			b = (int) (Math.random()*solution.getNombreNoeuds());
		}while(a == b);
		
		Circuit_Hamiltonien ret = (Circuit_Hamiltonien) solution.clone();
		
		ret.swapNoeud(a, b);

		return ret;
	}

	/**
	 * retourne la taille du problème
	 */
	@Override
	public int getTaille() {
		return this.getJeu().getListNoeuds().size();
	}

	@Override
	public Circuit_Hamiltonien solutionInitialHeur(TourReference<? extends Arete, ? extends Circuit> tr) throws ErreurDonneesException {
		Circuit_Hamiltonien ret = solutionInitialBase();
		Circuit_TourReference tor = (Circuit_TourReference) tr;
		for(Arete a:tor.getKeySet()){
			if(tor.getValeur(a) == 1.){
				if(!ret.getParcourt().contains(a)){
					int b = ret.getNoeudPlace(a.getNoeudA());
					int c = ret.getNoeudPlace(a.getNoeudB());
					if(b+1 < ret.getNombreNoeuds())
						ret.swapNoeud(c, b+1);
					else
						ret.swapNoeud(c, b-1);
					intouchable.add(a.getNoeudA());
					intouchable.add(a.getNoeudB());
				}
			}
		}
		for(Arete a:tor.getKeySet()){
			if(tor.getValeur(a) == 0.){
				if(!ret.getParcourt().contains(a)){
					int n = 0;
					if(intouchable.contains(a.getNoeudA()))
						n = ret.getNoeudPlace(a.getNoeudB());
					else
						n = ret.getNoeudPlace(a.getNoeudA());

					ret = voisinageHeur(ret, n);
				}
			}
		}
		return ret;
	}


	@Override
	public Circuit_Hamiltonien voisinageHeur(Circuit_Hamiltonien solution) {
		int a, b;

		do{
			do{

				a = (int) (Math.random()*solution.getNombreNoeuds());
				b = (int) (Math.random()*solution.getNombreNoeuds());

			}while(a == b && !intouchable.contains(solution.getOrdre().get(a)) && !intouchable.contains(solution.getOrdre().get(b)));
		}while(getTourRef().getKeySet().contains(getJeu().getArete(solution.getOrdre().get(a), solution.getOrdre().get(b)))
				&& getTourRef().getValeur(getJeu().getArete(solution.getOrdre().get(a), solution.getOrdre().get(b))) == 0.);

		Circuit_Hamiltonien ret = (Circuit_Hamiltonien) solution.clone();
		ret.swapNoeud(a, b);

		return ret;
	}

	public Circuit_Hamiltonien voisinageHeur(Circuit_Hamiltonien solution, int a) {
		int b;

		do{
			do{

				b = (int) (Math.random()*solution.getNombreNoeuds());

			}while(a == b && !intouchable.contains(solution.getOrdre().get(b)));
		}while(getTourRef().getKeySet().contains(getJeu().getArete(solution.getOrdre().get(a), solution.getOrdre().get(b)))
				&& getTourRef().getValeur(getJeu().getArete(solution.getOrdre().get(a), solution.getOrdre().get(b))) == 0.);

		Circuit_Hamiltonien ret = (Circuit_Hamiltonien) solution.clone();
		ret.swapNoeud(a, b);



		return ret;
	}

}