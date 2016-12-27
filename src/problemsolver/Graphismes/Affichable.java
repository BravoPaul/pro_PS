/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.Graphismes;

import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author Clément
 */
public interface Affichable {
    
    public abstract void affiche(JPanel j, double echelleLargeur, double echelleHauteur, double translationX, double translationY, Color c);
}
