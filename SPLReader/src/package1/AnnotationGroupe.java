package package1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		if (this.estPropositionEquivalente(annotation.getProposition())) {
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
		return Objects.hash(this.proposition);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
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