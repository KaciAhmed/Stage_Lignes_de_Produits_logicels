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

	public boolean estPropositionEquivalente(Proposition prop) {
		return this.proposition.equals(prop);
	}

	public void ajouterAnnotation(Annotation annotation) {
		if (estPropositionEquivalente(annotation.getProposition())) {
			this.annotations.add(annotation);
		}
	}

	public List<Annotation> getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	public Proposition getProposition() {
		return this.proposition;
	}

	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}

	@Override
	public String toString() {
		return "AnnotationGroupe [proposition=" + this.proposition + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.proposition == null) ? 0 : this.proposition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AnnotationGroupe other = (AnnotationGroupe) obj;
		if (this.proposition == null) {
			if (other.proposition != null) {
				return false;
			}
		} else if (!this.proposition.equals(other.proposition)) {
			return false;
		}
		return true;
	}

}