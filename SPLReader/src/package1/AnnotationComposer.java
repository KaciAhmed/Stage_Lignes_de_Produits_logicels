package package1;

import java.util.List;

public class AnnotationComposer extends Annotation {
	private List<Annotation> annotationsComposer;
	
	public AnnotationComposer(String nomDeFichier, int debutDeLigne, int nombreDeLigne, int nombreDeCaractere,
			int degre, CodeVariant codeVariant, Proposition proposition) {
		super(nomDeFichier, debutDeLigne, nombreDeLigne, nombreDeCaractere, degre, codeVariant, proposition);
	}
	
	@Override
	public int getNombreDeLigne() {
		int sum = 0;
		for (Annotation annotation : annotationsComposer) {
			sum =+ annotation.getNombreDeLigne();
		}
		return sum;
	}

	@Override
	public int getNombreDeCaractere() {
		int sum = 0;
		for (Annotation annotation : annotationsComposer) {
			sum += annotation.getNombreDeCaractere();
		}
		return sum;
	}

	@Override
	public String toString() {
		return "AnnotationComposer [getNombreDeLigne()=" + getNombreDeLigne() + ", getNombreDeCaractere()="
				+ getNombreDeCaractere() + ", getVariables()=" + getVariables() + ", getNomDeFichier()="
				+ getNomDeFichier() + ", getDebutDeLigne()=" + getDebutDeLigne() + ", getDegre()=" + getDegre()
				+ ", getProposition()=" + getProposition() + "]";
	}	
}
