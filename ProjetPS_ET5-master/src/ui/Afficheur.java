/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import problemsolver.ProblemSolver;

/**
 *
 * @author Clément
 */
public class Afficheur {
	public final static DecimalFormat DF = new DecimalFormat("#.0##");
    
    public static void erreurDialog(String s){
        JOptionPane.showMessageDialog(ProblemSolver.getMainFrame(), s, "Erreur", JOptionPane.WARNING_MESSAGE);
    }
    
    public static void infoDialog(String s){
        JOptionPane.showMessageDialog(ProblemSolver.getMainFrame(), s, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void erreurFataleDialog(Exception ex){
        JOptionPane.showMessageDialog(ProblemSolver.getMainFrame(), exceptionDial(ex, true), "Erreur fatale", JOptionPane.ERROR_MESSAGE);
    }
    
    private static JPanel exceptionDial(Exception ex, boolean stackTrace){
        if(ProblemSolver.DEBUG)
            ex.printStackTrace();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(ex.getMessage()));
        
        if(stackTrace){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            JTextArea textArea = new JTextArea(32, 64);
            textArea.setText(sw.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setCaretPosition(0);
            scrollPane.setVisible(false);
            
            JButton open = new JButton("Détails ->");
            
            open.addActionListener((ActionEvent e) -> {
                if(open.getText().equals("Détails ->")){
                    scrollPane.setVisible(true);
                    open.setText("Détails <-");
                    SwingUtilities.getWindowAncestor(scrollPane).pack();
                }else{
                    scrollPane.setVisible(false);
                    open.setText("Détails ->");
                    SwingUtilities.getWindowAncestor(scrollPane).pack();
                }
            });
            
            panel.add(open);
            panel.add(scrollPane);
        }
       
        return panel;
    }
    
    public static String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
    
    public static void afficheDDouble(double[][] tab){
    	for(int i = 0; i < tab[0].length; i++){
    		System.out.println(tab[0][i] + "; " + tab[1][i]); 
    	}
    }
}
