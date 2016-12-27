/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.donnees.solutions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import problemsolver.Graphismes.GraphicComponent;
import problemsolver.donnees.Arete;
import problemsolver.donnees.Donnees;
import problemsolver.donnees.Graphe;
import problemsolver.donnees.Noeud;

public class Circuit extends Donnees {
	private ArrayList<Noeud> ordre;
	private Graphe graphe;

	protected final double maxX;
	protected final double maxY;
	protected final double minX;
	protected final double minY;
	protected static final int GRAPHICSIZE = 8;


	@SuppressWarnings("unchecked")
	public Circuit(ArrayList<Noeud> _listNoeuds, Graphe _graphe){
		super();
		ordre = (ArrayList<Noeud>) _listNoeuds.clone();
		graphe = _graphe;
		maxX = graphe.getMaxX();
		maxY = graphe.getMaxY();
		minX = graphe.getMinX();
		minY = graphe.getMinY();
	}

	public Circuit(Graphe _graphe){
		super();
		graphe = _graphe;
		maxX = graphe.getMaxX();
		maxY = graphe.getMaxY();
		minX = graphe.getMinX();
		minY = graphe.getMinY();
	}

	public ArrayList<Noeud> getOrdre() {
		return ordre;
	}

	public Graphe getGraphe(){
		return graphe;
	}


	public ArrayList<Arete> getParcourt(){
		ArrayList<Arete> ret = new ArrayList<Arete>();
		for(int i = 0; i < ordre.size(); i++){
			Noeud a = ordre.get(i);
			Noeud b;
			if(i+1 == ordre.size())
				b = ordre.get(0);
			else
				b = ordre.get(i+1);

			ret.add(graphe.getArete(a, b));
		}
		return ret;
	}

	public double distanceTotale(){
		ArrayList<Arete> lA = new ArrayList<Arete>();
		double distance = 0;
		lA = this.getParcourt();
		for(Arete a:lA){
			distance = distance + a.getPoids();

		}
		return distance;
	}


	@Override
	public void affiche(JPanel j, double echelleLargeur, double echelleHauteur, double translationX, double translationY, Color c) {
		double MX = Math.abs(minX);
		double MY = Math.abs(minY);
		double EL = (j.getWidth()/(maxX+MX))*echelleLargeur;
		double EH = (j.getHeight()/(maxY+MY))*echelleHauteur;
		@SuppressWarnings("serial")
		GraphicComponent pan = new GraphicComponent(){

			@Override
			protected void affiche(Graphics g) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(c);
				for(Noeud n:ordre){ // place les villes
					g2d.fillOval((int) (((n.getX()+MX)*EL*getResizeX())-GRAPHICSIZE/2), (int) (((n.getY()+MY)*EH*getResizeY())-GRAPHICSIZE/2), GRAPHICSIZE, GRAPHICSIZE);
				}
				for(Arete a:getParcourt()){ // place les traits entre les villes
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

	public int getNombreNoeuds(){
		return ordre.size();
	}

	public void swapNoeud(int a, int b){
		if(a >= ordre.size() || b >= ordre.size())
			return;
		else{
			Collections.swap(ordre, a, b);
		}
	}

	public int getNoeudPlace(Noeud n){
		return ordre.indexOf(n);
	}

	@Override
	public String toString(){
		String ret="Total: "+ this.distanceTotale()+"\n";
		for(Arete a: this.getParcourt()){
			ret+= a+"\n";
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Circuit clone() {
		Circuit cloned = (Circuit) super.clone();
		cloned.ordre = (ArrayList<Noeud>) ordre.clone();
		cloned.graphe = graphe.clone();
		return cloned;
	}

	@Override
	public int getSize() {
		int nbSommet = ordre.size();
		return nbSommet*3;
	}
}
