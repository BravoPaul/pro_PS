package problemsolver.probleme;

import java.util.ArrayList;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.TourReference;

@SuppressWarnings("serial")
public class EchantillonS2APHA extends ArrayList<DonneesScenario<Graphe, Arete, PhiLambda>> {
	private TourReference<Arete, Circuit> tourReference;
	
	public TourReference<Arete, Circuit> getTr() {
		return tourReference;
	}
	
	public void setTr(TourReference<Arete, Circuit> a_tourRef) {
		this.tourReference = a_tourRef;
	}

}
