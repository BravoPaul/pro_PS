/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.parser;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;

import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.Noeud;
import problemsolver.exceptions.ErreurDonneesException;

/**
 *
 * @author Clément
 */
public class Parse_Graphe extends Parser<Graphe_Complet>{

    @Override
    public Graphe_Complet Parse(File f) throws ErreurDonneesException, IOException, NullPointerException{
    	BufferedReader in = new BufferedReader(new FileReader(f));
    	HashMap<String, Noeud> ln = new HashMap<String, Noeud>();
    	HashMap<Integer, Arete> aa = new HashMap<Integer, Arete>();
    	
    	int numPoint = 0;
    	Noeud nAct = null;

    	String s;
    	while ((s = in.readLine()) != null){
    		if(s.contains("<vertex>")){ // Nouveau point x
    			if(!ln.containsKey(Integer.toString(numPoint)))
    				ln.put(Integer.toString(numPoint), new Noeud(0.,0.,Integer.toString(numPoint)));
    			nAct = ln.get(Integer.toString(numPoint));
    		}
    		
    		if(s.contains("<edge")){//Si on est sur la bonne ligne on lit
    			String nom = s.split(">")[1].split("<")[0];
    			if(!nom.equals(Integer.toString(numPoint))){
	    			if(!ln.containsKey(nom))
	    				ln.put(nom, new Noeud(0.,0.,nom));
	    			Noeud nTmp = ln.get(nom);
	    			if(!aa.containsKey(Arete.getHashCode(nAct, nTmp))){
	    				double poids = new BigDecimal(s.split("\"")[1]).doubleValue();
	    				aa.put(Arete.getHashCode(nAct, nTmp), new Arete(nAct, nTmp, poids));
	    			}
    			}
    		}
    		
    		if(s.contains("</vertex>")){ //La on passe au noeud suivant.
    			numPoint ++;
    		}
    	}
    	ArrayList<Noeud> an = new ArrayList<Noeud>();
    	for(Noeud n:ln.values()){
    		an.add(n);
    	}
    	
        Graphe_Complet g = new Graphe_Complet(an, aa);
    	in.close();
        RepartitionNoeuds(g);
		return g;

    }
    
    private void RepartitionNoeuds(Graphe_Complet g){
    	int n = g.getListNoeuds().size();
    	double[][] x = new double[2][n];
    	double factX = 1;
    	double factY = 1;
    	int nbrDemiTour = 8;
    	double alpha = 0.2;
    	for(int i = 0; i < n; i++){
    		factX = Math.cos((Math.PI*(i+(i*alpha)))/(nbrDemiTour));
    		factY = Math.sin((Math.PI*(i+(i*alpha)))/(nbrDemiTour));
    		x[0][i] = i*alpha*factX;
    		x[1][i] = i*alpha*factY;
    	}
    	
    	g.setCoordonnees(x);
    }

}
