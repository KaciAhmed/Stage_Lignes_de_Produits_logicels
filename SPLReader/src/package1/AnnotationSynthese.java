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
		"MoyenneNombreLigne", "MoyenneNombreCaractere", "nbAnnotationSimple", "nbAnnotationComp" })
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

	private int nbAnnotationSimple;
	private int nbAnnotationComp;

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
		this.nbAnnotationSimple = nbAnnotationSimple;
		this.nbAnnotationComp = nbAnnotationComp;
	}

	public Proposition getProposition() {
		return this.proposition;
	}

	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}

	public List<Integer> getLstNombreDeLigne() {
		return this.listeNombreDeLigne;
	}

	public void setLstNombreDeLigne(List<Integer> lstNombreDeLigne) {
		this.listeNombreDeLigne = lstNombreDeLigne;
	}

	public List<Integer> getLstNombreDeCaractere() {
		return this.listeNombreDeCaractere;
	}

	public void setLstNombreDeCaractere(List<Integer> lstNombreDeCaractere) {
		this.listeNombreDeCaractere = lstNombreDeCaractere;
	}

	public List<Integer> getLstDegree() {
		return this.listeDegree;
	}

	public void setLstDegree(List<Integer> lstDegree) {
		this.listeDegree = lstDegree;
	}

	public float getMoyDegree() {
		return this.MoyenneDegree;
	}

	public void setMoyDegree(float moyDegree) {
		this.MoyenneDegree = moyDegree;
	}

	public float getMoyNombreLigne() {
		return this.MoyenneNombreLigne;
	}

	public void setMoyNombreLigne(float moyNombreLigne) {
		this.MoyenneNombreLigne = moyNombreLigne;
	}

	public float getMoyNombreCaractere() {
		return this.MoyenneNombreCaractere;
	}

	public void setMoyNombreCaractere(float moyNombreCaractere) {
		this.MoyenneNombreCaractere = moyNombreCaractere;
	}

	public int getNbAnnotationSimple() {
		return this.nbAnnotationSimple;
	}

	public void setNbAnnotationSimple(int nbAnnotationSimple) {
		this.nbAnnotationSimple = nbAnnotationSimple;
	}

	public int getNbAnnotationComp() {
		return this.nbAnnotationComp;
	}

	public void setNbAnnotationComp(int nbAnnotationComp) {
		this.nbAnnotationComp = nbAnnotationComp;
	}

	@Override
	public String toString() {
		return "AnnotationSynthese [\n proposition=" + this.proposition + ", \n lstNombreDeLigne="
				+ this.listeNombreDeLigne + ", \n lstNombreDeCaractere=" + this.listeNombreDeCaractere
				+ ", \n lstDegree=" + this.listeDegree + ", \n MoyDegree=" + this.MoyenneDegree + ", \n MoyNombreLigne="
				+ this.MoyenneNombreLigne + ", \n MoyNombreCaractere=" + this.MoyenneNombreCaractere
				+ ", \n nbAnnotationSimple=" + this.nbAnnotationSimple + ", nbAnnotationComp=" + this.nbAnnotationComp
				+ "]";
	}
}