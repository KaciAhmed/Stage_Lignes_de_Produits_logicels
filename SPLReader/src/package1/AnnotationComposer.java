package package1;

import java.util.ArrayList;
import java.util.List;

public class AnnotationComposer extends Annotation {
	private List<Annotation> annotationsEnfant;

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
		return "AnnotationComposer [\n Nom du Fichier= " + getNomDeFichier() + ",\n Proposition= " + getProposition()
				+ ",\n DebutDeLigne= " + getDebutDeLigne() + ",\n NombreDeLigne = " + getNombreDeLigne()
				+ ",\n NombreDeCaractère= " + getNombreDeCaractere() + ",\n Degre = " + getDegre() + "]";
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
