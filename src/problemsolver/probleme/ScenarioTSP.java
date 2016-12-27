package problemsolver.probleme;

import java.util.HashMap;
import java.util.HashSet;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe;

public class ScenarioTSP extends DonneesScenario<Graphe, Arete, PhiLambda> {

	public ScenarioTSP(HashMap<Graphe, PhiLambda> pScenario, HashSet<Arete> lArete) {
		super(pScenario, lArete);
		// TODO Auto-generated constructor stub
	}
	
	public void pondereScenario(double facteurPonderation){
		for(Graphe g : super.scenariosWithPenalities.keySet()) {
			for(Arete a : g.getMapAretes().values()) {
				a.pondere(facteurPonderation);
			}
		}
		for (Arete a1 : super.aretesDeterministes) {
			a1.pondere(facteurPonderation);
		}
	}
}
