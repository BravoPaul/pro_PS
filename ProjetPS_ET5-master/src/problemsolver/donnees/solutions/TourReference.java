/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.donnees.solutions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Donnees;
import problemsolver.donnees.Graphe;

/**
 *
 * @author Clément
 * @param <T> le type de données Arete
 * @param <U> le type d'extraction Circuit
 */
public abstract class TourReference<T extends Arete, U extends Circuit> extends Circuit{
    private HashMap<T, Double> moyenneTour;
    
    public TourReference(HashSet<T> l, Graphe g){
        super(g);
        moyenneTour = new HashMap<>();
        l.stream().forEach((d) -> {
            moyenneTour.put(d, 0.);
        });
    }
    
    public double getValeur(T d){
        return moyenneTour.get(d);
    }
    
    public void setValeur(T d, double v){
        moyenneTour.put(d, v);
    }
    
    public Set<T> getKeySet(){
        return moyenneTour.keySet();
    }
    
    public Collection<Double> getValues(){
    	return moyenneTour.values();
    }
    
    public abstract void calculer(Collection<? extends Circuit> collection);
    
    @Override
	public int getSize() {
    	int taille = 0;
    	for(Donnees d:moyenneTour.keySet()){
    		taille+= d.getSize();
    	}
    	return taille;
	}
    
    public double verifier(){
    	double val = 0;
    	for(Double d:moyenneTour.values())
    		val+=d;
    	return val;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public TourReference<T, U> clone() {
    	TourReference<T, U> cloned = (TourReference<T, U>) super.clone();
		cloned.moyenneTour = (HashMap<T, Double>) moyenneTour.clone();
		return cloned;
	}

	
}
