package package1;

import java.util.List;

public class FabricationAnnotation {

	public static Annotation makeAnnotation(String predicat, List<String> variables, int degre, int nbChar, int nbLigne) {
		Annotation product = new Annotation();
		product.setFichier(Annotation.FICHIER_NON_DEFINI);
		product.setPredicat(predicat);
		product.setVariables(variables);
		product.setDebutDeLigne(Annotation.NUMERO_NON_DEFINI);
		product.setDegre(degre);
		product.setNbChar(nbChar);
		product.setNbLine(nbLigne);
		return product;		
	}

	public static Annotation makeAnnotation(Annotation obj) {
		return makeAnnotation(obj.getPredicat(),
				obj.getVariables(), 
				obj.getDegre(), 
				obj.getNbChar(), 
				obj.getNbLine()
		);
	}
}
