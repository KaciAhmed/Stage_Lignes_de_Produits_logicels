package package1;

import java.util.ArrayList;
import java.util.List;

public class AnnotationGroupe {
	private List<Annotation> annotations;
	private Proposition proposition;
	
	
	
	public AnnotationGroupe(Proposition proposition) {
		super();
		this.annotations = new ArrayList<Annotation>();
		this.proposition = proposition;
	}

	public boolean estPropositionEquivalente(Proposition prop){
		return this.proposition.equals(prop);
	}

	public void ajouterAnnotation(Annotation annotation) {
		if(estPropositionEquivalente(annotation.getProposition())) {
			this.annotations.add(annotation);
		}
	}
}
