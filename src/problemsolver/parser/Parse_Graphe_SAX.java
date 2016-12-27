package problemsolver.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import problemsolver.donnees.Graphe_Complet;
import problemsolver.exceptions.ErreurDonneesException;

/**
 * 
 * @author florian
 *
 */
public class Parse_Graphe_SAX extends Parser<Graphe_Complet>{

	@Override
	public Graphe_Complet Parse(File f) throws ErreurDonneesException, FileNotFoundException, IOException {
    	
		SAXParserFactory factory = SAXParserFactory.newInstance();
		MyXMLHandler handler = new MyXMLHandler();
		
		try {
    		SAXParser saxParser = factory.newSAXParser();
    		saxParser.parse(f, handler);
			
    	}catch (ParserConfigurationException e) {
			// TODO: handle exception
    		e.printStackTrace();
    	} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return handler.getGraphComplet();
    	
	}

	
	
}
