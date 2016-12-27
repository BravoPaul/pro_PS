package problemsolver.solveur;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Set;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Donnees;
import problemsolver.donnees.Graphe;
import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.Circuit_Hamiltonien;
import problemsolver.donnees.solutions.Circuit_TourReference;
import problemsolver.donnees.solutions.TourReference;
import problemsolver.exceptions.ErreurDonneesException;
import problemsolver.probleme.DonneesScenario;
import problemsolver.probleme.EchantillonS2APHA;
import problemsolver.probleme.Penalites;
import problemsolver.probleme.PhiLambda;
import problemsolver.probleme.Probleme;
import problemsolver.probleme.Probleme_Stochastique;
import ui.Afficheur;

@SuppressWarnings("unused")
public class S2APHA extends Solveur<Probleme_Stochastique<Graphe_Complet, Circuit_Hamiltonien, DonneesScenario<Graphe_Complet, Arete, PhiLambda>, Circuit_TourReference>> {
	private Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> secondSolveur;
	private int variation;
	private int pourcentDet;
	private static final int DEFVAR = 20, DEFPER = 20;

	private int nombreEchantillons;
	private int nombreScenarios;
	private int tailleEchantillonRef;

	HashMap<Graphe_Complet, Circuit> listSolution = new HashMap<Graphe_Complet, Circuit>();
	private HashMap<Donnees, Penalites<? extends TourReference<?, ?>, ? extends Donnees>> allScenarios;
	private ArrayList<EchantillonS2APHA> listeEchantillon;
	private EchantillonS2APHA EchRef;

	
	public S2APHA(int nbEchantillon, Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> secondS, int var, int pDet) {
		nombreEchantillons = nbEchantillon;
		nombreScenarios = nombreEchantillons;
		tailleEchantillonRef = 4 * nombreScenarios;
		secondSolveur = secondS;
		variation = var;
		pourcentDet = pDet;
	}

	public S2APHA(int nbrS, Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> secondS) {
		this(nbrS, secondS, DEFVAR, DEFPER);
	}

	@Override
	public Circuit resoudre(Graphe_Complet donnees, Circuit_Hamiltonien solInit, boolean minimiser) throws ErreurDonneesException {
		boolean b;
		secondSolveur.setProbleme(getProbleme());
		secondSolveur.setAffiche(false);
		
		HashMap<DonneesScenario<Graphe, Arete, PhiLambda>, Donnees> solutionsCalculees = new HashMap<DonneesScenario<Graphe, Arete, PhiLambda>, Donnees>();

		// prépartition du problème
		
		//le problème contient l'ensemble des scénarios de tous les échantillons 
		getProbleme().initialiserScenarios(variation, pourcentDet,
				nombreEchantillons * nombreScenarios + tailleEchantillonRef);
		getProbleme().initialiserTourRef(getProbleme().getDonnees(), getProbleme().getJeu());
		for(Graphe_Complet scen: (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()) {
	    	listSolution.put(scen, secondSolveur.resoudre(scen,solInit,minimiser)); //TSP
	    }
		getProbleme().getTourRef().calculer(listSolution.values());
	    getProbleme().setUseStochastique(true);
	    
	    /*{
		allScenarios = getProbleme().getDs().getHashMapScenarios();
		ArrayList<Donnees> allScenariosKeys = (ArrayList<Donnees>) allScenarios.keySet();
		ArrayList<Penalites<? extends TourReference<?, ?>, ? extends Donnees>> allScenariosPenalities = (ArrayList<Penalites<? extends TourReference<?, ?>, ? extends Donnees>>) allScenarios
				.values();
		HashSet<Donnees> allDataDeterministes = getProbleme().getDs().getDonneesDeterministes();
		int i = 0, j = 0;
		HashMap maHashMap;
		EchantillonS2APHA mon_echantillon = null;

		// mise sous forme d'echantillons
		
		//création des échantillons
		for (i = 0; i < nombreEchantillons; i++) {
			mon_echantillon = null;
			for (j = 0; j < nombreScenarios; j++) {
				maHashMap = new HashMap();
				maHashMap.put(allScenariosKeys.get(j + i * nombreEchantillons), allScenariosPenalities.get(j + i * nombreEchantillons));
				mon_echantillon.add(new DonneesScenario(maHashMap, allDataDeterministes));
			}
			listeEchantillon.add(mon_echantillon);
		}

		//création de l'échantillon de référence
		for (i++; i < nombreEchantillons * nombreScenarios + tailleEchantillonRef; i++) {
			for (j++; j < tailleEchantillonRef; j++) {
				maHashMap = new HashMap();
				maHashMap.put(allScenariosKeys.get(j + i * nombreEchantillons), allScenariosPenalities.get(j + i * nombreEchantillons));
				mon_echantillon.add(new DonneesScenario(maHashMap, allDataDeterministes));
			}
			EchRef = mon_echantillon;
		}

		// listeEchantillon contient tous les echantillons hors référence
		// EchRef contient l'echantillon référence
		
		//pour chaque echantillon on calcule une solution initiale avec le SAA
		for (EchantillonS2APHA echantillon : listeEchantillon) { 
			for (DonneesScenario<Donnees, Donnees, Penalites<? extends TourReference<?, ?>, ? extends Donnees>> donneesScenario : echantillon) {
				for(Donnees scen : donneesScenario.getScenarios()) { // resolution par echantillon
					solutionsCalculees.put(donneesScenario, secondSolveur.resoudre(scen, solInit, minimiser, (Echantillon) new ArrayList<Donnees>(donneesScenario.getScenarios()))); // SAA
				}
			}
		}
		
		// stockage des valeurs intiatiales
		Donnees xBest = null; // meilleur solution
		double vBest=0; // valeur de la meilleur solution ^v^best
		for (Donnees maSolutionInitiale : solutionsCalculees.values()) {
			double v = ((Circuit) maSolutionInitiale).distanceTotale(); // renvoi la ditance du circuit
			if (v > vBest) {
				vBest = v;
				xBest=maSolutionInitiale;
			}
		}
		
		ArrayList<ArrayList<Donnees>> solutionsEchantillons = null; // contient x^mk, les solution des échantillons à l'iteration k
		double[][] v = null; // valeurs des solutions ^v^m,k
		double[] vkbest = null; // valeurs des ^v^k best 
		
		for (Donnees maSolutionInitiale : solutionsCalculees.values()) {
			solutionsEchantillons.add((ArrayList<Donnees>) solutionsCalculees.values());
		}
		
		double epsilon = 0; // seuil de convergence
		double[] epsilonTab = {0}; // seuil de convergence à l'iteration k
		Donnees[] solPondereeTab; // solutions pondérées par les probobabilités à l'iteration k
		Donnees[] solEquilibreeTab=null; // solutions equilibrées à l'iteration k
		double[][] w = new double[nombreEchantillons][1000]; // w^mk
		double[] alpha = new double[1000]; alpha[0]=1; // alpha
		int k=0, kmax = 1000; // indices d'iterations
		double delta = 0.05;
		double beta = 2.0;
		double[] ro = new double[1000]; ro[0]=1;
		while ((epsilonTab[k] >= epsilon || solEquilibreeTab[k] != xBest)  && k < kmax && beta == delta) {
			k++;

			for (i=0; i<listeEchantillon.size(); i++) {
				//solPondereeTab[k] = (1/listeEchantillon.size()) * solutionsEchantillons.get(i).get(k);
			}
			
			solutionsCalculees.clear();
			
			if (alpha[k-1] == 0) {
				alpha[k] = alpha[k-1];
			}
			else
			{
				alpha[k] = alpha[k-1]*delta;
			}
			
			for (i=0; i<listeEchantillon.size(); i++) {
				//solEquilibreeTab[k] = alpha[k] * solPondereeTab[k] + (1 - alpha[k]) * xBest;
			}
				
			if (k >= 2) {
				if (epsilonTab[k] >= epsilonTab[k]/2.0) {
					ro[k] = beta * ro [k-1];
				}
				else
				{
					ro[k] = ro[k-1];
				}
			}
			
			for (i=0; i<listeEchantillon.size(); i++) {
				//x^m k-1 - xEqu^k
				double diff = ((Circuit) solutionsEchantillons.get(i).get(k-1)).distanceTotale() - ((Circuit) solEquilibreeTab[k]).distanceTotale();
				w[i][k] = w[i][k-1] + ro[k] * diff;
			}
			
			for (i=0; i<listeEchantillon.size(); i++) {
				for (DonneesScenario<Donnees, Donnees, Penalites<? extends TourReference<?, ?>, ? extends Donnees>> donneesScenario : listeEchantillon.get(i)) {
					for(Donnees scen : donneesScenario.getScenarios()) { // resolution par echantillon
						solutionsCalculees.put(donneesScenario, secondSolveur.resoudre(scen, solInit, minimiser, (Echantillon) new ArrayList<Donnees>(donneesScenario.getScenarios())));
					}
				}
				listeEchantillon.get(i).getTr().calculer(solutionsCalculees.values());
								
				double somme = 0;
				for (ArrayList<Donnees> unesolution :  solutionsEchantillons) {
					somme = Math.sqrt(Math.abs(((Circuit) unesolution.get(k)).distanceTotale() - ((Circuit) solEquilibreeTab[k]).distanceTotale()));
				}
				epsilonTab[k] = somme; 
			}
			
			//on calcule la "vrai" meilleure solution sur N' 
			//en partant de la partie deterministe des solutions trouvées sur chaque echantillon
			for (i=0; i<listeEchantillon.size(); i++) {
				//on calcule la meilleure valeur objectif sur tout N'
				//en cherchant sur tous les scénarios de celui-ci avec la solution trouvée avant 
				for (j=0; j<EchRef.size(); j++) {
					EchRef.getTr().calculer(solutionsCalculees.values());
				}
			}
			
			// recherche de la meilleur solution du tour
			vkbest[k]=v[0][k];
			for (i=1; i<listeEchantillon.size(); i++) {
				if (v[i][k] < vkbest[k]) {
					vkbest[k]=v[i][k];
				}
				
			}
			
			// recherche de la meilleur solution sur tous les tours 
			if (vkbest[k] < vBest) {
				vBest = vkbest[k];
				for (i=0; i<listeEchantillon.size(); i++) {
					//xBest =  ; //x corespondant au vBest
				}
			}
		}
	    */
		int t=0;
		do{
			t = t + 1;
			listSolution.clear();
			
			for(Graphe_Complet scen: (Set<Graphe_Complet>) getProbleme().getDonnees().getScenarios()){
		    	listSolution .put(scen, secondSolveur.resoudre(scen,solInit,minimiser)); //TSP		    	
		    }
			getProbleme().getTourRef().calculer(listSolution.values());
			b = true;
			for(Graphe_Complet d:listSolution.keySet()){
				b = (getProbleme().getDonnees().getPenalites(d).ajuster(getProbleme().getTourRef(),listSolution.get(d)) && b);
				getProbleme().getDonnees().getPenalites(d).ajuster(getProbleme().getTourRef(),listSolution.get(d));
			}
		}while(!b);
		
		//Afficheur.infoDialog("Terminé en " + t + " tours");
		
		//x^S2APHA
		return getProbleme().getTourRef();
	}

	@Override
	public String toString() {
		return "S2APHA " + donneesString();
	}

	public String donneesString() {
		if (variation == DEFVAR && pourcentDet == DEFPER)
			return "(" + nombreEchantillons + " echantillons, avec " + secondSolveur + ")";
		else
			return "(" + nombreEchantillons + " echantillons, variation " + variation + "% et " + pourcentDet
					+ "% déterminisne, avec " + secondSolveur + ")";
	}

	public Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> getSolveur() {
		return secondSolveur;
	}

}
