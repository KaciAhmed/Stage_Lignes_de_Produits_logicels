package package1;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

class Annotation {

	private String fichier;
	private String predicat;
	private List<String> variables; 
	private int startLine;
	private int nbLine;
	private int degre;
	private int nbChar;

	public static String ESPACE = " ";
	public static String DEBUT_ANNOTATION = "//#if";
	public static String CHANGEMENT_ANNOTATION = "//#elif";
	public static String FIN_ANNOTATION = "//#endif";

	public Annotation () {
		this.fichier = "";
		this.predicat = "";
		this.variables = new ArrayList<String>();
		this.startLine = -1;
		this.nbLine = 0;
		this.degre = 0;
		this.nbChar =0;
	}
	
	public String toString(){
		return "ANNOTATION {" + 
				"\nFICHIER= " + fichier+
				"\nPREDICAT= " + predicat +
				"\nVARIABLES= " + variables +
				"\nSTARTLINE= " + startLine +
				"\nNBLINE= " + nbLine +
				"\nDEGRE= " + degre +
				"\nNBCHAR= " + nbChar +
				"\n}\n";
	}
	
	public void incrementNbLine(){
		this.nbLine++;
	}
	
	public void incrementNbChar(int nb){
		this.nbChar += nb;	
	}
	
	public int getStartLine() {
		return this.startLine;
	}
	public String getFichier() {
		return this.fichier;
	}	
	public String getPredicat() {
		return this.predicat;
	}	
	public List<String> getVariables() {
		return this.variables;
	}	
	public int getNbLine() {
		return this.nbLine;
	}
	public int getDegre() {
		return this.degre;
	}
	public int getNbChar() {
		return this.nbChar;
	}
	
	public void setFichier(String fichier) {
		this.fichier = fichier;
	}
	public void setPredicat(String predicat) {
		this.predicat = predicat;
	}
	public void setVariables(List<String> variables) {
		this.variables = variables;
	}
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	public void setNbLine(int nbLine) {
		this.nbLine = nbLine;
	}
	public void setDegre(int degre) {
		this.degre = degre;
	}
	public void setNbChar(int nbChar) {
		this.nbChar = nbChar;
	}
}