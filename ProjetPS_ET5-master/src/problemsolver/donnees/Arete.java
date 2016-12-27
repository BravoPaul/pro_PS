/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.donnees;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import problemsolver.Graphismes.GraphicComponent;
import ui.Afficheur;

/**
 *
 * @author Clément
 */
public class Arete extends Donnees{
    private Noeud noeudA;
    private Noeud noeudB;
    private double poids;
    
    public Arete(Noeud a, Noeud b, double poids){
        super();
        this.noeudA = a;
        this.noeudB = b;
        this.poids = poids;
    }
    
    public void pondere(double facteurPonderation){
    	setPoids(poids*facteurPonderation);
    }

    public Noeud getNoeudA() {
        return noeudA;
    }

    public void setNoeudA(Noeud noeudA) {
        this.noeudA = noeudA;
    }

    public Noeud getNoeudB() {
        return noeudB;
    }

    public void setNoeudB(Noeud noeudB) {
        this.noeudB = noeudB;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double d) {
        this.poids = d;
    }
    
    @Override
    public void affiche(JPanel j, double echelleLargeur, double echelleHauteur, double translationX, double translationY, Color c) {
        @SuppressWarnings("serial")
		GraphicComponent line = new GraphicComponent(){
            
            @Override
            protected void affiche(Graphics g) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(c);
                g2d.drawLine((int)((noeudA.getX()+translationX)*echelleLargeur*getResizeX()), (int)((noeudA.getY()+translationY)*echelleHauteur*getResizeY()), (int)((noeudB.getX()+translationX)*echelleLargeur*getResizeX()), (int)((noeudB.getY()+translationY)*echelleHauteur*getResizeY()));
            }
        };
        line.setBackground(new Color(255, 255, 255, 0));
        line.setInitSize(j.getSize());
        SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				j.add(line);
				j.getParent().revalidate();
		        j.getParent().repaint();
			}
        	
        });
    }
    
    
    public boolean contientNoeud(Noeud n){
    	if(this.getNoeudA().getNom().equals(n.getNom()) || this.getNoeudB().getNom().equals(n.getNom()))
    		return true;
    	else 
    		return false;
    }
    
    @Override
    public String toString(){
        String ret=noeudA + "; \t"+ noeudB+"; \t" + Afficheur.DF.format(poids);
        return ret;
    }
    
    @Override
    public int hashCode() {// On utilise la fonction de Szudziks pour hasher avec les coordonnées de A et B.
        return getHashCode(noeudA, noeudB);
    }
    
    @Override
    public Arete clone() {
		Arete cloned = (Arete) super.clone();
		cloned.setNoeudA((Noeud) noeudA.clone());
		cloned.setNoeudB((Noeud) noeudB.clone());
		cloned.setPoids(poids);
    	return cloned;
    }
    
    public static int getHashCode(Noeud nA, Noeud nB){
        int a, b;
        if(nA.getID() > nB.getID()){
            a = nA.getID();
            b = nB.getID();
        }else{
            b = nA.getID();
            a = nB.getID();
        }
        return a * (a + 1) + b;
    }
    
    @Override
	public int getSize() {
		return 2;
	}
}
