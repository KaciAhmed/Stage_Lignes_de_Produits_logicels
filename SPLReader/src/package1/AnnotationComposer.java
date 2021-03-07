package package1;

import java.util.List;

public class AnnotationComposer extends Annotation {
	private List<Annotation> annotationsComposer;
	
	public AnnotationComposer(String nomDeFichier, int debutDeLigne, int nombreDeLigne, int nombreDeCaractere,
			int degre, CodeVariant codeVariant, Proposition proposition) {
		super(nomDeFichier, debutDeLigne, nombreDeLigne, nombreDeCaractere, degre, codeVariant, proposition);
	}

	@Override
	public String toString() {
		return "AnnotationComposer [\n Nom du Fichier= "+ getNomDeFichier() +",\n Proposition= " + getProposition()+
				",\n DebutDeLigne= " + getDebutDeLigne()+",\n NombreDeLigne = " + getNombreDeLigne() + ",\n NombreDeCaractère= "
				+ getNombreDeCaractere() + ",\n Degre = " + getDegre()+ "]";
	}	
	
	public void afficherArborescence() {
		System.out.println(this.toString());
		for (Annotation annotation : annotationsComposer) {
			System.out.println("\n");
			System.out.println("\t");
			annotation.afficherArborescence();
		}
	}
}
