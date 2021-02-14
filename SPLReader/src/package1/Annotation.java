package package1;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

class Annotation {
	// Un VP
	private String fichier;
	private String predicat; // Addition && Soustraction
	private List<String> variables; // [0] = Addition  [1] = Soustraction
	private int startLine;
	private int nbLine;
	private int degre;
	private int nbChar;

	public Annotation () {
		this.fichier = "";
		this.predicat = "";
		this.variables = new ArrayList<String>();
		this.startLine = -1;
		this.nbLine = -1;
		this.degre = -1;
		this.nbChar = -1;
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	public String getPredicat() {
		return predicat;
	}
	
	public void setPredicat(String predicat) {
		this.predicat = predicat;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}

	public int getStartLine() {
		return startLine;
	}
	
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getNbLine() {
		return nbLine;
	}

	public void setNbLine(int nbLine) {
		this.nbLine = nbLine;
	}

	public int getDegre() {
		return degre;
	}
	
	public void setDegre(int degre) {
		this.degre = degre;
	}

	public int getNbChar() {
		return nbChar;
	}

	public void setNbChar(int nbChar) {
		this.nbChar = nbChar;
	}

	public String toString(){
		return "Annotation {" + 
				"\nfichier= " + fichier +
				"\npredicat= " + predicat +
				"\nvariables= " + variables +
				"\nstartLine= " + startLine +
				"\nnbLine= " + nbLine +
				"\ndegre= " + degre +
				"\nnbChar= " + nbChar +
				"\n}\n";
	}
}