package package1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "synthese")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "proposition", "listeNombreDeLigne", "listeNombreDeCaractere", "listeDegree", "MoyenneDegree",
		"MoyenneNombreLigne", "MoyenneNombreCaractere", "nombreAnnotationSimple", "nombreAnnotationComposer" })
public class AnnotationSynthese {

	private Proposition proposition;
	@XmlList
	private List<Integer> listeNombreDeLigne;
	@XmlList
	private List<Integer> listeNombreDeCaractere;
	@XmlList
	private List<Integer> listeDegree;
	private float MoyenneDegree;
	private float MoyenneNombreLigne;
	private float MoyenneNombreCaractere;

	private int nombreAnnotationSimple;
	private int nombreAnnotationComposer;

	public AnnotationSynthese() {
		super();
		this.listeNombreDeLigne = new ArrayList<Integer>();
		this.listeNombreDeCaractere = new ArrayList<>();
		this.listeDegree = new ArrayList<>();
	}

	public AnnotationSynthese(Proposition proposition, List<Integer> lstNombreDeLigne,
			List<Integer> lstNombreDeCaractere, List<Integer> lstDegree, float moyDegree, float moyNombreLigne,
			float moyNombreCaractere, int nbAnnotationSimple, int nbAnnotationComp) {
		super();
		this.proposition = proposition;
		this.listeNombreDeLigne = lstNombreDeLigne;
		this.listeNombreDeCaractere = lstNombreDeCaractere;
		this.listeDegree = lstDegree;
		this.MoyenneDegree = moyDegree;
		this.MoyenneNombreLigne = moyNombreLigne;
		this.MoyenneNombreCaractere = moyNombreCaractere;
		this.nombreAnnotationSimple = nbAnnotationSimple;
		this.nombreAnnotationComposer = nbAnnotationComp;
	}

	public Proposition getProposition() {
		return this.proposition;
	}

	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}

	public List<Integer> getListeNombreDeLigne() {
		return this.listeNombreDeLigne;
	}

	public void setListeNombreDeLigne(List<Integer> lstNombreDeLigne) {
		this.listeNombreDeLigne = lstNombreDeLigne;
	}

	public List<Integer> getListeNombreDeCaractere() {
		return this.listeNombreDeCaractere;
	}

	public void setListeNombreDeCaractere(List<Integer> lstNombreDeCaractere) {
		this.listeNombreDeCaractere = lstNombreDeCaractere;
	}

	public List<Integer> getListeDegree() {
		return this.listeDegree;
	}

	public void setListeDegree(List<Integer> lstDegree) {
		this.listeDegree = lstDegree;
	}

	public float getMoyenneNombreLigne() {
		return this.MoyenneNombreLigne;
	}

	public void setMoyenneNombreLigne(float moyNombreLigne) {
		this.MoyenneNombreLigne = moyNombreLigne;
	}

	public float getMoyenneNombreCaractere() {
		return this.MoyenneNombreCaractere;
	}

	public void setMoyenneNombreCaractere(float moyNombreCaractere) {
		this.MoyenneNombreCaractere = moyNombreCaractere;
	}

	public float getMoyenneDegree() {
		return this.MoyenneDegree;
	}

	public void setMoyenneDegree(float moyDegree) {
		this.MoyenneDegree = moyDegree;
	}

	public int getNombreAnnotationSimple() {
		return this.nombreAnnotationSimple;
	}

	public void setNombreAnnotationSimple(int nbAnnotationSimple) {
		this.nombreAnnotationSimple = nbAnnotationSimple;
	}

	public int getNombreAnnotationComposer() {
		return this.nombreAnnotationComposer;
	}

	public void setNombreAnnotationComposer(int nbAnnotationComp) {
		this.nombreAnnotationComposer = nbAnnotationComp;
	}

	public void ajouterNombresDeAnnotation(Annotation annotation) {
		int nbLigne = annotation.getNombreDeLigne();
		int nbCaractere = annotation.getNombreDeCaractere();
		int degree = annotation.getDegre();
		this.ajouterListeNombreDeLigne(nbLigne);
		this.ajouterListeNombreDeCaractere(nbCaractere);
		this.ajouterListeNombreDeDegree(degree);
	}

	public void ajouterListeNombreDeLigne(int nombreDeLigne) {
		this.listeNombreDeLigne.add(nombreDeLigne);
	}

	public void ajouterListeNombreDeCaractere(int nombreDeCaractere) {
		this.listeNombreDeCaractere.add(nombreDeCaractere);
	}

	public void ajouterListeNombreDeDegree(int degree) {
		this.listeDegree.add(degree);
	}

	public int sommerListeNombreDeLigne() {
		return this.getListeNombreDeLigne().stream().mapToInt(e -> e).sum();
	}

	public int sommerListeDegree() {
		return this.getListeDegree().stream().mapToInt(e -> e).sum();
	}

	public int sommerListeNombreDeCaractere() {
		return this.getListeNombreDeCaractere().stream().mapToInt(e -> e).sum();
	}

	public void calculerMoyenneNombreLigne() {
		int sumLignes = 0;
		int moyNombreLigne = 0;
		sumLignes = this.sommerListeNombreDeLigne();
		moyNombreLigne = sumLignes / this.getListeNombreDeLigne().size();
		this.setMoyenneNombreLigne(moyNombreLigne);
	}

	public void calculerMoyenneNombreCaractere() {
		int sumNbcaracteres = 0;
		int moyNombreCaractere = 0;
		sumNbcaracteres = this.sommerListeNombreDeCaractere();
		moyNombreCaractere = sumNbcaracteres / this.getListeNombreDeLigne().size();
		this.setMoyenneNombreCaractere(moyNombreCaractere);
	}

	public void calculerMoyenneDegree() {
		int sumDegrees = 0;
		int moyDegree = 0;
		sumDegrees = this.sommerListeDegree();
		moyDegree = sumDegrees / this.getListeDegree().size();
		this.setMoyenneDegree(moyDegree);
	}

	@Override
	public String toString() {
		return "AnnotationSynthese [\n proposition=" + this.proposition + ", \n listeNombreDeLigne="
				+ this.listeNombreDeLigne + ", \n listeNombreDeCaractere=" + this.listeNombreDeCaractere
				+ ", \n listeDegree=" + this.listeDegree + ", \n MoyDegree=" + this.MoyenneDegree
				+ ", \n MoyNombreLigne=" + this.MoyenneNombreLigne + ", \n MoyNombreCaractere="
				+ this.MoyenneNombreCaractere + ", \n nbAnnotationSimple=" + this.nombreAnnotationSimple
				+ ", nbAnnotationComp=" + this.nombreAnnotationComposer + "]";
	}
}