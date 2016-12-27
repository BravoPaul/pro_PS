/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.donnees;

import problemsolver.Graphismes.Affichable;

/**
 *
 * @author Clément
 */
public abstract class Donnees implements Affichable, Cloneable{
    private static int IDglobal = 0;
    private final int ID;
    
    public Donnees(){
        ID = IDglobal++;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int hashCode() {
        return this.ID;
    }
    
    @Override public boolean equals(Object o){
        if(o instanceof Donnees){
            Donnees d = (Donnees) o;
            return d.hashCode() == this.hashCode();
        }
        return false;
    }
    
    @Override
    public Donnees clone() {
    	try {
			return (Donnees) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public abstract int getSize();
}
