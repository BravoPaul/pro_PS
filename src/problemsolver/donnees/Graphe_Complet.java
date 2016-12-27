/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.donnees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import problemsolver.exceptions.ErreurDonneesException;

/**
 *
 * @author Clément
 */
public class Graphe_Complet extends Graphe{
    
    public Graphe_Complet(ArrayList<Noeud> ln, HashMap<Integer, Arete> ma) throws ErreurDonneesException {
        super(ln, ma);
        /*if(!estComplet())
            throw new ErreurDonneesException("Graphe complet ne respecte pas les contraintes: ");
        */
    }

    public Graphe_Complet(Graphe g, double variation, HashSet<Arete> valDet) throws ErreurDonneesException {
        super(g, variation, valDet);
        if(!estComplet())
            throw new ErreurDonneesException("Graphe complet ne respecte pas les contraintes.");
    }
    
    private boolean estComplet(){
        
    	int nbArete, nbSommet;
    	
    	nbArete = this.getListAretes().size();
    	nbSommet = this.getListNoeuds().size();
    	return (nbArete == nbSommet*(nbSommet-1)/2);
    }
    
    @Override
    public Graphe_Complet clone() {
    	return (Graphe_Complet) super.clone();
    }
    
    @Override
	public int getSize() {
		int nbSommet = getListNoeuds().size();
		return nbSommet*(nbSommet-1);
	}
}
