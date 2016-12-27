/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.donnees.solutions;

import java.util.ArrayList;
import java.util.HashMap;

import problemsolver.donnees.Graphe;
import problemsolver.donnees.Noeud;
import problemsolver.exceptions.ErreurDonneesException;

/**
 *
 * @author Clément
 */
public class Circuit_Hamiltonien extends Circuit{
    
    public Circuit_Hamiltonien(ArrayList<Noeud> listNoeuds, Graphe graphe) throws ErreurDonneesException {
        super(listNoeuds, graphe);
        if(!estHamiltonien())
            throw new ErreurDonneesException("Circuit Hamiltonien ne respecte pas les contraintes.");
        
    }

    private boolean estHamiltonien(){
        HashMap<Noeud, Boolean> apparition = new HashMap<Noeud, Boolean>();
        for(Noeud n:getGraphe().getListNoeuds()){
        	apparition.put(n, false);
        }
        for(Noeud n:getOrdre()){
        	if(apparition.get(n)){ // doublons
        		return false;
        	}else
        		apparition.put(n, true);
        }
        return !apparition.values().contains(false); // Si ça contint un false, on a manqué un point. Sinon c'est ok.

    }
    
	@Override
    public Circuit_Hamiltonien clone() {
    	return (Circuit_Hamiltonien) super.clone();
    }
}
