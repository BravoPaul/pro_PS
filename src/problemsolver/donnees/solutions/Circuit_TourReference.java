/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.donnees.solutions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import problemsolver.Graphismes.GraphicComponent;
import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe;

/**
 *
 * @author Clément
 */
public class Circuit_TourReference extends TourReference<Arete, Circuit>{
    
    public Circuit_TourReference(HashSet<Arete> l, Graphe g) {
        super(l, g);
    }
    
    /**
     * calcul la moyenne sur la collection des valeurs des arrêtes selectionnés
     * Ne calcule pas une valeur de circuit
     */
    @Override
	public void calculer(Collection<? extends Circuit> collection) {
    	double div = collection.size();
    	for(Arete a:getKeySet()){
    		double val = 0;
    		for(Circuit c:collection){
    			if(c.getParcourt().contains(a)){
    				val += 1;
    			}
    		}
    		setValeur(a, val/div);
    	}
	}
    
    private double valNotZero(){
    	double somme = 0;
    	for (double valeur : getValues()){
			if(valeur != 0) somme  += valeur;
    	}
    	return somme;
    }
    
    @SuppressWarnings("serial")
	@Override
    public void affiche(JPanel j, double echelleLargeur, double echelleHauteur, double translationX, double translationY, Color c) {
    	double MX = Math.abs(minX);
        double MY = Math.abs(minY);
    	double EL = (j.getWidth()/(maxX+MX))*echelleLargeur;
        double EH = (j.getHeight()/(maxY+MY))*echelleHauteur;
    	GraphicComponent pan = new GraphicComponent(){
            
    		@Override
    		protected void affiche(Graphics g) {
        	Graphics2D g2d = (Graphics2D)g;
                for(Arete a:getKeySet()){
                	g2d.setColor(c);
                	g2d.fillOval((int) (((a.getNoeudA().getX()+MX)*EL*getResizeX())-GRAPHICSIZE/2), (int) (((a.getNoeudA().getY()+MY)*EH*getResizeY())-GRAPHICSIZE/2), GRAPHICSIZE, GRAPHICSIZE);
                	g2d.fillOval((int) (((a.getNoeudB().getX()+MX)*EL*getResizeX())-GRAPHICSIZE/2), (int) (((a.getNoeudB().getY()+MY)*EH*getResizeY())-GRAPHICSIZE/2), GRAPHICSIZE, GRAPHICSIZE);
                	if(getValeur(a) == 0){
                    	g2d.setColor(new Color(255, 0, 0, c.getAlpha()));
                	}
                	else if(getValeur(a) == 1){
                		g2d.setColor(new Color(0, 0, 255,c.getAlpha()));
                	}else{
                		g2d.setColor(new Color(255, 255, 0, c.getAlpha()));
                	}
                	//g2d.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(alphb*c.getAlpha())));
                	g2d.drawLine((int)((a.getNoeudA().getX()+MX)*EL*getResizeX()), (int)((a.getNoeudA().getY()+MY)*EH*getResizeY()), (int)((a.getNoeudB().getX()+MX)*EL*getResizeX()), (int)((a.getNoeudB().getY()+MY)*EH*getResizeY()));
                }
                
            }
        };
        pan.setBackground(new Color(255, 255, 255, 0));
        pan.setInitSize(j.getSize());
        SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				j.add(pan);
				j.getParent().revalidate();
		        j.getParent().repaint();
			}
        	
        });
    }
    
    @Override
    public String toString(){
    	//String ret="Nombre d'arêtes déterministes: "+getKeySet().size()+"\n";
    	String ret="Total = " + this.valNotZero() + "\n";
        for(Arete a: getKeySet()){
            ret+= a+"\tutilisation: "+getValeur(a)+"\n";
        }
        return ret;
    }

   	@Override
       public Circuit_TourReference clone() {
    	return (Circuit_TourReference) super.clone();
   	}
    
    
}
