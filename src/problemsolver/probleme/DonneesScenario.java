/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.probleme;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import problemsolver.donnees.Donnees;
import problemsolver.donnees.solutions.TourReference;

/**
 *
 * @author Clément
 * @param <T> les données de chaque scénario (un graphe pour le TSP)
 * @param <U> les données déterministes (les arêtes déterministes pour le TSP)
 * @param <V> Le type de pénalités
 */
public class DonneesScenario<T extends Donnees, U extends Donnees, V extends Penalites<? extends TourReference<?, ?>, ? extends Donnees>> {
    protected HashMap<T, V> scenariosWithPenalities; // un ensemble de scénarios
    protected HashSet<U> aretesDeterministes; // un hashset des données deterministes
    
    public DonneesScenario(HashMap <T, V> pScenario, HashSet<U> lArete){
        scenariosWithPenalities = pScenario;
        aretesDeterministes = lArete;
    }
    
    public HashMap<T, V> getDonneesScenarios(){
    	return scenariosWithPenalities;
    }
    
	public Set<T> getScenarios(){
        return scenariosWithPenalities.keySet();
    }
    
    public V getPenalites(T d){
        return scenariosWithPenalities.get(d);
    }
    
    public Collection<V> getPenalites(){
        return scenariosWithPenalities.values();
    }
    
    public HashSet<U> getAretesDeterministes(){
        return aretesDeterministes;
    }
    
}
