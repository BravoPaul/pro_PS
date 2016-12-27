package problemsolver.probleme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import problemsolver.donnees.Arete;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.Circuit_TourReference;

public class PhiLambda extends Penalites<Circuit_TourReference, Circuit>{

	private HashMap<Arete, Double> phi;
	private HashMap<Arete, Double> lambda;
	private static final double ALPHA = 1.55;
	@SuppressWarnings("unused")
	private static final double PRECISION = 0.99; // Si x > y, et x * précision < y alors x = y, autrement dit 99% pareil.
	
	
	public PhiLambda(HashSet<Arete> listA){
		phi = new HashMap<Arete, Double>();
		lambda = new HashMap<Arete, Double>();
		for(Arete a:listA){
			phi.put(a, a.getPoids()/2+0.1);
			lambda.put(a, 0.);
		}
	}
	
	public HashMap<Arete, Double> getPhi() {
		return phi;
	}



	public void setPhi(HashMap<Arete, Double> phi) {
		this.phi = phi;
	}



	public HashMap<Arete, Double> getLambda() {
		return lambda;
	}



	public void setLambda(HashMap<Arete, Double> lambda) {
		this.lambda = lambda;
	}

	public double getPhi(Arete a){
		return phi.get(a);
	}
	
	public double getLambda(Arete a){
		return lambda.get(a);
	}

	public static double getAlpha() {
		return ALPHA;
	}

	@Override
	public boolean ajuster(Circuit_TourReference tr, Circuit solution) {
		ArrayList<Arete> tour = solution.getParcourt();
		boolean ret = true;
		for(Arete a:lambda.keySet()){
			double lastLambda = lambda.get(a);
			double lastPhi = phi.get(a);
			double is;
			if(tour.contains(a))
				is = 1;
			else
				is = 0;
			double newLambda = lastLambda+lastPhi*(is - tr.getValeur(a));
			
			
			if(lastLambda != newLambda){//on a changé la valeur
				/*if(!(lastLambda > newLambda && lastLambda*PRECISION < newLambda) && !(lastLambda < newLambda && lastLambda > newLambda*PRECISION)){
					//On a changé la valeur de beaucoup. (trop)
					ret = false;
				}*/
				ret = false;
			}
			
			lambda.put(a, newLambda);
			phi.put(a, lastPhi*ALPHA);
		}
		return ret;

	}

}
