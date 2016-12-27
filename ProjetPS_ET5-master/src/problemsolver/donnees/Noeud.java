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
//import ui.Afficheur; //TODO voir dans toString pour remettre l'affichage précédant

/**
 *
 * @author Clément
 */
public class Noeud extends Donnees {
    private double x;
    private double y;
    private String nom;
    private static final int GRAPHICSIZE = 8;
    
    public Noeud(double x, double y, String nom){
        super();
        this.x = x;
        this.y = y;
        this.nom = nom;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    @Override
    public void affiche(JPanel j, double echelleLargeur, double echelleHauteur, double translationX, double translationY, Color c) {
        @SuppressWarnings("serial")
		GraphicComponent point = new GraphicComponent(){
            
            @Override
            protected void affiche(Graphics g) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setColor(c);
                g2d.fillOval((int) (((x+translationX)*echelleLargeur*getResizeX())-GRAPHICSIZE/2), (int) (((y+translationY)*echelleHauteur*getResizeY())-GRAPHICSIZE/2), GRAPHICSIZE, GRAPHICSIZE);
            }
        };
        point.setBackground(new Color(255, 255, 255, 0));
        point.setInitSize(j.getSize());
        SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				j.add(point);
				j.getParent().revalidate();
		        j.getParent().repaint();
			}
        	
        });
    }
    
    @Override
    public String toString(){
    	return nom/*+"("+Afficheur.DF.format(x)+";"+Afficheur.DF.format(y)+")"*/; //TODO enlever le commentaire pour reactiver l'affichage des coordonnées 
    }
    
    @Override
    public Noeud clone() {
		Noeud cloned = (Noeud) super.clone();
		cloned.setX(x);
		cloned.setY(y);
		cloned.setNom(nom);
    	return cloned;
    }

	@Override
	public int getSize() {
		return 1;
	}
    
}
