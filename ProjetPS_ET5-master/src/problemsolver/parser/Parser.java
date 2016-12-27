/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolver.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import problemsolver.donnees.Donnees;
import problemsolver.exceptions.ErreurDonneesException;

/**
 *
 * @author Clément
 * @param <T> le type de données parsé
 */
public abstract class Parser<T extends Donnees> {
    
    public abstract T Parse(File f) throws ErreurDonneesException, FileNotFoundException, IOException;
}
