package package1;


public class AnnotationSimple extends Annotation {

	public AnnotationSimple(String nomDeFichier, int debutDeLigne, int nombreDeLigne, int nombreDeCaractere, int degre,
			CodeVariant codeVariant, Proposition proposition) {
		super(nomDeFichier, debutDeLigne, nombreDeLigne, nombreDeCaractere, degre, codeVariant, proposition);
	}
	
	
	@Override
	public String toString() {
		return "AnnotationSimple [NomDeFichier= " + getNomDeFichier() + ", DebutDeLigne= " + getDebutDeLigne()
				+ ", NombreDeLigne= " + getNombreDeLigne() + ", NombreDeCaractere= " + getNombreDeCaractere()
				+ ", Degre= " + getDegre() + ", CodeVariant= " + getCodeVariant() + ", Proposition= "
				+ getProposition() + "]";
	}




	public void afficherArborescence() {
		System.out.println(this.toString());
		
	}

}
