package package1;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlSeeAlso({ Annotation.class })
@XmlRootElement(name = "annotationSimple")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "nomDeFichier", "proposition", "debutDeLigne", "nombreDeLigne", "nombreDeCaractere", "degre" })
public class AnnotationSimple extends Annotation implements Serializable {

	public AnnotationSimple() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnnotationSimple(String nomDeFichier, int debutDeLigne, int nombreDeLigne, int nombreDeCaractere, int degre,
			CodeVariant codeVariant, Proposition proposition) {
		super(nomDeFichier, debutDeLigne, nombreDeLigne, nombreDeCaractere, degre, codeVariant, proposition);
	}

	@Override
	public String toString() {
		return "AnnotationSimple [\n NomDeFichier= " + this.getNomDeFichier() + ",\n DebutDeLigne= "
				+ this.getDebutDeLigne() + ",\n NombreDeLigne= " + this.getNombreDeLigne() + ", \n NombreDeCaractere= "
				+ this.getNombreDeCaractere() + ",\n Degre= " + this.getDegre() + ",\n Proposition= "
				+ this.getProposition() + "]";
	}

	@Override
	public void afficherArborescence() {
		System.out.println(this.toString());

	}

}
