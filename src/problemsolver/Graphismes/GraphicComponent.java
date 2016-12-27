/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.Graphismes;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Clément
 */
@SuppressWarnings("serial")
public abstract class GraphicComponent extends JPanel{
    int initSX;
    int initSY;
    double resizeX;
    double resizeY;
    public GraphicComponent(){
        initSX = 0;
        initSY = 0;
        resizeX = 1.;
        resizeY = 1.;
    }
    
    protected abstract void affiche(Graphics g);
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        affiche(g);
    }
    
    public void setInitSize(Dimension d){
        setSize(d);
        initSX = d.width;
        initSY = d.height;
        resizeX = 1.;
        resizeY = 1.;
    }
    
    @Override public void setSize(Dimension d){
        resizeX = (double)((double)(d.width)/(double)initSX);
        resizeY = (double)((double)(d.height)/(double)initSY);
        super.setSize(d);
    }
    
    public double getResizeX() {
        return resizeX;
    }

    public double getResizeY() {
        return resizeY;
    }
    
    
    
}
