/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.solveur;

import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.Circuit_Hamiltonien;
import problemsolver.exceptions.ErreurDonneesException;
import problemsolver.probleme.Echantillon;
import problemsolver.probleme.Probleme;

/**
 *
 * @author Clément
 * @param <T> Les types de problèmes résolvables (Probleme pour tous, Probleme_Stochastique pour les problèmes stochastiques etc)
 */
public abstract class Solveur<T extends Probleme<Graphe_Complet, Circuit_Hamiltonien>> {
    private T probleme;
    private boolean affiche;
    
    public Solveur(){
    	affiche = true;
    }
    
    public Circuit resoudre(Graphe_Complet dinitiales, Circuit_Hamiltonien solInit, boolean minimiser, Echantillon echantillon) throws ErreurDonneesException {
		return null;
	}
    
    public abstract Circuit resoudre(Graphe_Complet donnees, Circuit_Hamiltonien solution, boolean minimiser) throws ErreurDonneesException;
    public Circuit resoudre(Circuit_Hamiltonien circuitH, boolean minimiser) throws ErreurDonneesException{
		return resoudre(this.getProbleme().getJeu(), circuitH, minimiser);
	}
    
    public Circuit resoudre(boolean minimiser) throws ErreurDonneesException {
		return resoudre(this.getProbleme().solutionInitial(), minimiser);
	}

    /**
     * Renvoi un sous-type de problème
     * @return
     */
    public T getProbleme() {
        return probleme;
    }

    @SuppressWarnings("unchecked")
	public void setProbleme(Probleme<Graphe_Complet, Circuit_Hamiltonien> probleme2) {
    	this.probleme = (T) probleme2;
    }
    
    public void setAffiche(boolean b){
    	affiche = b;
    }
    
    public boolean getAffiche(){return affiche;}

	
}
