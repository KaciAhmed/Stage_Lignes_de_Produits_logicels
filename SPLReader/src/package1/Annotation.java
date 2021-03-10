package package1;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlSeeAlso({ AnnotationSimple.class, AnnotationComposer.class })
@XmlRootElement(name = "annotation")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
@XmlType(propOrder = { "nomDeFichier", "proposition", "debutDeLigne", "nombreDeLigne", "nombreDeCaractere", "degre" })
public abstract class Annotation implements Serializable {

	private String nomDeFichier;
	private int debutDeLigne;
	private int nombreDeLigne;
	private int nombreDeCaractere;
	private int degre;
	@XmlTransient
	private CodeVariant codeVariant;
	private Proposition proposition;

	public Annotation() {
		super();
		// TODO Auto-generated constructor stub
	}

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
		return this.nomDeFichier;
	}

	public void setNomDeFichier(String nomDeFichier) {
		this.nomDeFichier = nomDeFichier;
	}

	public int getDebutDeLigne() {
		return this.debutDeLigne;
	}

	public void setDebutDeLigne(int debutDeLigne) {
		this.debutDeLigne = debutDeLigne;
	}

	public int getNombreDeLigne() {
		return this.nombreDeLigne;
	}

	public void setNombreDeLigne(int nombreDeLigne) {
		this.nombreDeLigne = nombreDeLigne;
	}

	public int getNombreDeCaractere() {
		return this.nombreDeCaractere;
	}

	public void setNombreDeCaractere(int nombreDeCaractere) {
		this.nombreDeCaractere = nombreDeCaractere;
	}

	public int getDegre() {
		return this.degre;
	}

	public void setDegre(int degre) {
		this.degre = degre;
	}

	public CodeVariant getCodeVariant() {
		return this.codeVariant;
	}

	public void setCodeVariant(CodeVariant codeVariant) {
		this.codeVariant = codeVariant;
	}

	public Proposition getProposition() {
		return this.proposition;
	}

	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}

	public abstract void afficherArborescence();

	public void ajouterEnfant(Annotation annotation) {
		throw new UnsupportedOperationException();
	}

	public List<Annotation> getAnnotationsEnfant() {
		throw new UnsupportedOperationException();
	}

	public void setAnnotationsEnfant(List<Annotation> annotation) {
		throw new UnsupportedOperationException();
	};

}