/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import problemsolver.TSP.TSP;
import problemsolver.solveur.Recuit;
import ui.Afficheur;
import ui.MainFrame;

/**
 *
 * @author Clément
 */
public class ProblemSolver {

    private static final MainFrame MAINFRAME = new MainFrame( new Manager(null, new TSP(), new Recuit(), true));
    public static final boolean DEBUG = true;
    public static final int MAX_SIZE = 100000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
                Afficheur.erreurFataleDialog(e);
        }
        try{
            MAINFRAME.setVisible(true);
        }catch(Exception e){
            Afficheur.erreurDialog("Une exception est remontée jusqu'au main");
            Afficheur.erreurFataleDialog(e);
        }
        
    }
    
    public static MainFrame getMainFrame(){return MAINFRAME;}
    
}
