package package1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlSeeAlso({ Annotation.class })
@XmlRootElement(name = "annotationComposer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "nomDeFichier", "proposition", "debutDeLigne", "nombreDeLigne", "nombreDeCaractere", "degre",
		"annotationsEnfant" })
public class AnnotationComposer extends Annotation implements Serializable {

	@XmlElementWrapper(name = "annotations")
	@XmlElement(name = "annotation")
	private List<Annotation> annotationsEnfant = null;

	public AnnotationComposer() {
		super();
		this.annotationsEnfant = new ArrayList<>();
	}

	public AnnotationComposer(String nomDeFichier, int debutDeLigne, int nombreDeLigne, int nombreDeCaractere,
			int degre, CodeVariant codeVariant, Proposition proposition) {
		super(nomDeFichier, debutDeLigne, nombreDeLigne, nombreDeCaractere, degre, codeVariant, proposition);
		this.annotationsEnfant = new ArrayList<Annotation>();
	}

	@Override
	public void ajouterEnfant(Annotation annotation) {
		this.annotationsEnfant.add(annotation);
	}

	@Override
	public String toString() {
		return "AnnotationComposer [\n Nom du Fichier= " + this.getNomDeFichier() + ",\n Proposition= "
				+ this.getProposition() + ",\n DebutDeLigne= " + this.getDebutDeLigne() + ",\n NombreDeLigne = "
				+ this.getNombreDeLigne() + ",\n NombreDeCaractère= " + this.getNombreDeCaractere() + ",\n Degre = "
				+ this.getDegre() + "]";
	}

	@Override
	public void afficherArborescence() {
		System.out.println(this.toString());
		for (Annotation annotation : this.annotationsEnfant) {
			annotation.afficherArborescence();
		}
	}

	@Override
	public List<Annotation> getAnnotationsEnfant() {
		return this.annotationsEnfant;
	}

	@Override
	public void setAnnotationsEnfant(List<Annotation> annotationsEnfant) {
		this.annotationsEnfant = annotationsEnfant;
	}

}
