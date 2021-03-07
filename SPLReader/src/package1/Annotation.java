package package1;

import java.util.List;

public abstract class Annotation {
	private String nomDeFichier;
	private int debutDeLigne;
	private int nombreDeLigne;
	private int nombreDeCaractere;
	private int degre;
	private CodeVariant codeVariant;
	private Proposition proposition;
	
	public Annotation(String nomDeFichier, int debutDeLigne, int nombreDeLigne, int nombreDeCaractere, int degre,
			CodeVariant codeVariant, Proposition proposition) {
		super();
		this.nomDeFichier = nomDeFichier;
		this.debutDeLigne = debutDeLigne;
		this.nombreDeLigne = nombreDeLigne;
		this.nombreDeCaractere = nombreDeCaractere;
		this.degre = degre;
		this.codeVariant = codeVariant;
		this.proposition = proposition;
	}

	public void incrementNombreDeLigne() {
		this.nombreDeLigne += 1;
	}
	
	public void ajouterNombreDeCaractere(int nombreDeCaratere) {
		this.nombreDeCaractere += nombreDeCaratere;
	}
	
	
	
	public String getNomDeFichier() {
		return nomDeFichier;
	}

	public void setNomDeFichier(String nomDeFichier) {
		this.nomDeFichier = nomDeFichier;
	}

	public int getDebutDeLigne() {
		return debutDeLigne;
	}

	public void setDebutDeLigne(int debutDeLigne) {
		this.debutDeLigne = debutDeLigne;
	}

	public int getNombreDeLigne() {
		return nombreDeLigne;
	}

	public void setNombreDeLigne(int nombreDeLigne) {
		this.nombreDeLigne = nombreDeLigne;
	}

	public int getNombreDeCaractere() {
		return nombreDeCaractere;
	}

	public void setNombreDeCaractere(int nombreDeCaractere) {
		this.nombreDeCaractere = nombreDeCaractere;
	}

	public int getDegre() {
		return degre;
	}

	public void setDegre(int degre) {
		this.degre = degre;
	}

	public CodeVariant getCodeVariant() {
		return codeVariant;
	}

	public void setCodeVariant(CodeVariant codeVariant) {
		this.codeVariant = codeVariant;
	}

	public Proposition getProposition() {
		return proposition;
	}

	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}

	public abstract void afficherArborescence();
}