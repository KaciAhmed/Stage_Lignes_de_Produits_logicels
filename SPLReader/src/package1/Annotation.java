package package1;
import java.util.ArrayList;
import java.util.List;

class Annotation {

	static final int NUMERO_NON_DEFINI = -1;
	static final String FICHIER_NON_DEFINI = "non defini";
	private static final String PREDICAT_NON_DEFINI = "non defini";
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
	public static final String REGEX_TAB = "\\t";
	public static final String STRING_VIDE = "";

	public Annotation () {
		this.nomDuFichier = FICHIER_NON_DEFINI;
		this.predicat = PREDICAT_NON_DEFINI;
		this.variables = new ArrayList<String>();
		this.debutDeLigne = NUMERO_NON_DEFINI;
		this.nbLigne = NUMERO_NON_DEFINI;
		this.degre = NUMERO_NON_DEFINI;
		this.nbChar = NUMERO_NON_DEFINI;
	}

	public String toString(){
		return "ANNOTATION {" +
				(nomDuFichier.equals(FICHIER_NON_DEFINI) ? STRING_VIDE : "\n\tFICHIER= " + nomDuFichier) +
				"\n\tPREDICAT= " + predicat +
				"\n\tVARIABLES= " + variables +
				(debutDeLigne == NUMERO_NON_DEFINI? STRING_VIDE : "\n\tLIGNE_DE_DEBUT= " + debutDeLigne) +
				"\n\tNB_LIGNE= " + nbLigne +
				"\n\tDEGRE= " + degre +
				"\n\tNB_CHAR= " + nbChar +
				"\n}";
	}

	public void incrementNbLigne(){
		incrementNbLigne(1);
	}
	
	public void incrementNbLigne(int i) {
		this.nbLigne += i;
	}
	
	public void incrementNbChar(int nb){
		this.nbChar += nb;
	}

	public int getDebutDeLigne() {
		return debutDeLigne;
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
	public void setDebutDeLigne(int debutDeLigne) {
		this.debutDeLigne = debutDeLigne;
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

/*	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((predicat == null) ? 0 : predicat.hashCode());
		return result;
	}
	*/

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Annotation other = (Annotation) obj;
		if (predicat == null) {
			if (other.predicat != null)
				return false;
		} else if (!predicat.equals(other.predicat))
			return false;
		return true;
	}

}