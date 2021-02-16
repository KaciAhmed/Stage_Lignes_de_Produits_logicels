package package1;
import java.util.ArrayList;
import java.util.List;

class Annotation {

	private String nomDuFichier;
	private String predicat;
	private List<String> variables;
	private int debutDeLigne;
	private int nbLigne;
	private int degre;
	private int nbChar;

	public static String ESPACE = " ";
	public static String DEBUT_ANNOTATION = "//#if";
	public static String CHANGEMENT_ANNOTATION = "//#elif";
	public static String FIN_ANNOTATION = "//#endif";

	public Annotation () {
		this.nomDuFichier = "";
		this.predicat = "";
		this.variables = new ArrayList<String>();
		this.debutDeLigne = 0;
		this.nbLigne = 0;
		this.degre = 0;
		this.nbChar =0;
	}

	public String toString(){
		return "ANNOTATION {" +
				"\n\tFICHIER= " + nomDuFichier+
				"\n\tPREDICAT= " + predicat +
				"\n\tVARIABLES= " + variables +
				"\n\tSTARTLINE= " + debutDeLigne +
				"\n\tNBLINE= " + nbLigne +
				"\n\tDEGRE= " + degre +
				"\n\tNBCHAR= " + nbChar +
				"\n}";
	}

	public void incrementNbLigne(){
		this.nbLigne++;
	}

	public void incrementNbChar(int nb){
		this.nbChar += nb;
	}
	public int getStartLine() {
		return this.debutDeLigne;
	}
	public String getFichier() {
		return this.nomDuFichier;
	}
	public String getPredicat() {
		return this.predicat;
	}
	public List<String> getVariables() {
		return this.variables;
	}
	public int getNbLine() {
		return this.nbLigne;
	}
	public int getDegre() {
		return this.degre;
	}
	public int getNbChar() {
		return this.nbChar;
	}

	public void setFichier(String fichier) {
		this.nomDuFichier = fichier;
	}
	public void setPredicat(String predicat) {
		this.predicat = predicat;
	}
	public void setVariables(List<String> variables) {
		this.variables = variables;
	}
	public void setStartLigne(int startLine) {
		this.debutDeLigne = startLine;
	}
	public void setNbLine(int nbLine) {
		this.nbLigne = nbLine;
	}
	public void setDegre(int degre) {
		this.degre = degre;
	}
	public void setNbChar(int nbChar) {
		this.nbChar = nbChar;
	}
}