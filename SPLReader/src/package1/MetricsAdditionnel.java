package package1;

import java.util.List;

public class MetricsAdditionnel {

	public MetricsAdditionnel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double calculerProportionAnnotationEliminable(List<Annotation> annotations,
			List<Annotation> annotationsEliminables) {
		return ((double) annotationsEliminables.size() / (double) annotations.size());
	}

	public double calculerProportionAnnotationSimplifiable(List<Annotation> annotations,
			List<Annotation> annotationsSimplifiables) {
		return ((double) annotationsSimplifiables.size() / (double) annotations.size());
	}

	public long compterNombreAnnotationConcatenable(List<Annotation> annotations) {
		int nbAnnoatationConcatenable = 0;
		int j = 0;
		for (int i = 0; i < annotations.size(); i++) {
			j = i + 1;
			if (j < annotations.size()) {
				if ((annotations.get(i).getProposition().getFormule()
						.equals(annotations.get(j).getProposition().getFormule()))
						&& ((annotations.get(i).getDebutDeLigne()
								+ annotations.get(i).getNombreDeLigne()) == annotations.get(j).getDebutDeLigne())) {
					nbAnnoatationConcatenable++;
				}
			}
			if (annotations.get(i).estComposer()) {
				nbAnnoatationConcatenable += this
						.compterNombreAnnotationConcatenable(annotations.get(i).getAnnotationsEnfant());
			}
		}
		return nbAnnoatationConcatenable;
	}

	/****
	 * similarit� de code
	 */
	public Matrice getMatriceSimilariteCode(List<Annotation> annotationsLineariser) {
		Matrice matriceSimilariteDeCode = new MatriceSimilarite(annotationsLineariser);
		matriceSimilariteDeCode.calculerMatrice();
		return matriceSimilariteDeCode;
	}
	/*
	 * Fin similarit� de code
	 */
}
