package problemsolver.solveur;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.Circuit_Hamiltonien;
import problemsolver.donnees.solutions.Circuit_TourReference;
import problemsolver.exceptions.ErreurDonneesException;
import problemsolver.probleme.DonneesScenario;
import problemsolver.probleme.PhiLambda;
import problemsolver.probleme.Probleme;
import problemsolver.probleme.Probleme_Stochastique;

public class RecuitPha extends Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>>{
	Pha p;
	Solveur<Probleme<Graphe_Complet, Circuit_Hamiltonien>> r;
	
	public RecuitPha(Pha mt){
		super();
		r = mt.getSolveur();
		p = mt;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Circuit resoudre(Graphe_Complet graphe, Circuit_Hamiltonien solution, boolean minimiser) throws ErreurDonneesException {
		p.setProbleme((Probleme_Stochastique<Graphe_Complet, Circuit_Hamiltonien, DonneesScenario<Graphe_Complet, Arete, PhiLambda>, Circuit_TourReference>) this.getProbleme());
		r.setProbleme(this.getProbleme());
		Circuit bestSolution = p.resoudre(graphe, solution, minimiser);
		p.getProbleme().setUseHeuristique(true);
		p.getProbleme().setUseStochastique(false);
		return bestSolution; //r.resoudre(bestSolution, minimiser);
	}
	
    @Override
    public String toString(){
        return "Pha "+p.donneesString();
    }

}
