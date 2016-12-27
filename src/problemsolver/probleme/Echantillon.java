package problemsolver.probleme;

import java.util.ArrayList;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.TourReference;

@SuppressWarnings("serial")
public class Echantillon extends ArrayList<Graphe_Complet>{
	private TourReference<Arete, Circuit> tourReference;

	public TourReference<Arete, Circuit> getResultat() {
		return tourReference;
	}

	public void setResultat(TourReference<Arete, Circuit> a_tourRef) {
		this.tourReference = a_tourRef;
	}
	
	public TourReference<Arete, Circuit> getTr() {
		return tourReference;
	}
	
	public void setTr(TourReference<Arete, Circuit> a_tourRef) {
		this.tourReference = a_tourRef;
	}

	
}
