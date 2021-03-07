package package1;

public class AnnotationSimple extends Annotation {

	public AnnotationSimple(String nomDeFichier, int debutDeLigne, int nombreDeLigne, int nombreDeCaractere, int degre,
			CodeVariant codeVariant, Proposition proposition) {
		super(nomDeFichier, debutDeLigne, nombreDeLigne, nombreDeCaractere, degre, codeVariant, proposition);
	}

	@Override
	public String toString() {
		return "AnnotationSimple [\n NomDeFichier= " + getNomDeFichier() + ",\n DebutDeLigne= " + getDebutDeLigne()
				+ ",\n NombreDeLigne= " + getNombreDeLigne() + ", \n NombreDeCaractere= " + getNombreDeCaractere()
				+ ",\n Degre= " + getDegre() + ",\n Proposition= " + getProposition() + "]";
	}

	@Override
	public void afficherArborescence() {
		System.out.println(this.toString());

	}

}
