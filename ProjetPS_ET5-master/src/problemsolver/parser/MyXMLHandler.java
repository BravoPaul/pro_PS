package problemsolver.parser;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import mdsj.MDSJ;
import problemsolver.donnees.Arete;
import problemsolver.donnees.Graphe_Complet;
import problemsolver.donnees.Noeud;
import problemsolver.exceptions.ErreurDonneesException;

public class MyXMLHandler extends DefaultHandler {
	private String node = null;
	private int numPoint = 0;
	private String nom = "";
	
	private HashMap<String, Noeud> hashMapStringNoeud = new HashMap<String, Noeud>();
	private HashMap<Integer, Arete> hashMapVilleArete = new HashMap<Integer, Arete>();
	private ArrayList<Noeud> arrayListNoeud = new ArrayList<Noeud>();
	private Noeud nAct = null;
	
	private Graphe_Complet graphe;

	private String strCout;
	private double cout = 0.0;

	// debut du parsing
	@Override
	public void startDocument() throws SAXException {
		//System.out.println("Debut du parsing");
	}

	// fin du parsing
	@Override
	public void endDocument() throws SAXException {
		//System.out.println("Fin du parsing");
		
    	for(Noeud n:hashMapStringNoeud.values()){
    		arrayListNoeud.add(n);
    	}		
	}

	/**
	 * 
	 * Redefinition de la methode pour intercepter les evenements
	 * 
	 */
	@Override
	public void startElement(String namespaceURI, String lname, String qname, Attributes attrs) throws SAXException {
		node = qname;

		if (qname.equalsIgnoreCase("vertex")) {
			//System.out.println("ajout d'un noeud a la liste");
			if(!hashMapStringNoeud.containsKey(Integer.toString(numPoint)))
				hashMapStringNoeud.put(Integer.toString(numPoint), new Noeud(0.,0.,Integer.toString(numPoint)));
			nAct = hashMapStringNoeud.get(Integer.toString(numPoint));
		}
		if (qname.equalsIgnoreCase("edge")) {
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {

					// catch attribute value
					String aname = attrs.getLocalName(i);

					if (aname.equalsIgnoreCase("cost")) {
						strCout = attrs.getValue(i);
						try {
							cout = Double.parseDouble(strCout);
							
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		//System.out.println("Fin de l'element " + qName);
		node = qName;

		if (node.equalsIgnoreCase("vertex")) {
			//System.out.println("passage a la ville suivante");
			numPoint++;
		}
		if (node.equalsIgnoreCase("edge")) {
			//System.out.println("stockage du cout dans : " + villeActuel + " " + villeArrivee);
			
			if(!nom.equals(Integer.toString(numPoint))){
    			if(!hashMapStringNoeud.containsKey(nom))
    				hashMapStringNoeud.put(nom, new Noeud(0.,0.,nom));
    			Noeud nTmp = hashMapStringNoeud.get(nom);
    			if(!hashMapVilleArete.containsKey(Arete.getHashCode(nAct, nTmp))){
    				double poids = cout;
    				hashMapVilleArete.put(Arete.getHashCode(nAct, nTmp), new Arete(nAct, nTmp, poids));
    			}
			}
		}
	}

	/**
	 * 
	 * permet de recuperer la valeur d'un noeud
	 * 
	 */
	@Override
	public void characters(char[] data, int start, int end) {

		// La variable data contient tout notre fichier.
		// Pour recuperer la valeur, nous devons nous servir des limites en
		// parametre
		// "start" correspond a l'indice ou commence la valeur recherchee
		// "end" correspond a la longueur de la chaine
		String str = new String(data, start, end);
		if (node.compareTo("edge")==0) {
			nom = str;
		}

	}

	public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
		// don't do anything
	}
	
	@SuppressWarnings("unused")
	private void RepartitionNoeudsMDSJ(Graphe_Complet g){
    	double[][] x;
    	x = MDSJ.classicalScaling(g.getDoubleArray(), 2);
    	
    	g.setCoordonnees(x);
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
    
    public Graphe_Complet getGraphComplet() throws ErreurDonneesException{
    	graphe = new Graphe_Complet(arrayListNoeud, hashMapVilleArete);
		RepartitionNoeuds(graphe);
    	return graphe;
 	}
}
