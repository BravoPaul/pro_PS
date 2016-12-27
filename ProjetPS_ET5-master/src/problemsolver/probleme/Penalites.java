/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.probleme;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Donnees;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.TourReference;

/**
 *
 * @author Clément
 */
public abstract class Penalites<T extends TourReference<? extends Arete, ? extends Circuit>, U extends Donnees>{
    
    public abstract boolean ajuster(T tr, U solution);
}
 