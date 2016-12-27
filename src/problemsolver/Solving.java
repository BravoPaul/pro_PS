package problemsolver;

import java.awt.Color;

import javax.swing.SwingUtilities;

import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.solutions.Circuit;
import problemsolver.donnees.solutions.Circuit_Hamiltonien;
import problemsolver.exceptions.ErreurDonneesException;
import problemsolver.probleme.Probleme;
import problemsolver.solveur.Solveur;
import ui.Afficheur;
import ui.GraphFrame;

public class Solving extends Thread {
	private final Solveur<? extends Probleme<Graphe_Complet, Circuit_Hamiltonien>> solveur;
	private final boolean min;

	public Solving(Solveur<? extends Probleme<Graphe_Complet, Circuit_Hamiltonien>> solveur2, boolean m) {
		solveur = solveur2;
		min = m;
	}

	@Override
	public void run(){
		try {
			Circuit d = solveur.resoudre(min);
			SwingUtilities.invokeLater(() -> {
				//parseByFile(file);
				ProblemSolver.getMainFrame().getGFrame().showDonnees(GraphFrame.TAB_SOLUTION, d, Color.BLACK);
				//ProblemSolver.getMainFrame().getGFrame().setText(GraphFrame.TAB_SOLUTION, "TonTexte");
				Afficheur.infoDialog("Terminé en "+ProblemSolver.getMainFrame().getClock());
				ProblemSolver.getMainFrame().stopClock();
			});
		} catch (ErreurDonneesException e) {
			Afficheur.erreurFataleDialog(e);
		}
	}
}
