package package1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "metricsVital")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "moyenneDegree", "DispersionPredicatsDansLesAnnotations", "RapporPredicatsParGroupesAnnotations",
		"RapporPredicatsfichierParNbTotalFichier", "moyenneNbPredicatTousLesFichierParNbTotalFichier",
		"RapportAnnotationDifferenteParAnnotationTotal" })
public class MetricsVital {

	private float moyenneDegree; // sum(moyenne degree) / nbAnnotationsGroupe
	private float DispersionPredicatsDansLesAnnotations; // nbPredicatTotal / nbAnnotationTotal
	private float RapporPredicatsParGroupesAnnotations; // nbPredicatTotalDeTousLesVPGs / nbVpgTotal // var
														// fan-out-onVPG
	private float RapporPredicatsfichierParNbTotalFichier; // (nbPredicatsParFichier / NbFichier) d'un fichier // var
															// fan-in-on file
	private float moyenneNbPredicatTousLesFichierParNbTotalFichier; // nbPredicatsTotalDeTousLesFichier / nbFichiers //
																	// Var fan-out-on file
	private float RapportAnnotationDifferenteParAnnotationTotal; // nbAnnotationsDifferent / nbAnnotationsTotal d'un
																	// fichier // VP Fan in on file

	public MetricsVital() {
		super();
	}

	public MetricsVital(float moyenneDegree, float dispersionPredicatsDansLesAnnotations,
			float rapporPredicatsParGroupesAnnotations, float rapporPredicatsfichierParNbTotalFichier,
			float moyenneNbPredicatTousLesFichierParNbTotalFichier,
			float rapportAnnotationDifferenteParAnnotationTotal) {
		super();
		this.moyenneDegree = moyenneDegree;
		this.DispersionPredicatsDansLesAnnotations = dispersionPredicatsDansLesAnnotations;
		this.RapporPredicatsParGroupesAnnotations = rapporPredicatsParGroupesAnnotations;
		this.RapporPredicatsfichierParNbTotalFichier = rapporPredicatsfichierParNbTotalFichier;
		this.moyenneNbPredicatTousLesFichierParNbTotalFichier = moyenneNbPredicatTousLesFichierParNbTotalFichier;
		this.RapportAnnotationDifferenteParAnnotationTotal = rapportAnnotationDifferenteParAnnotationTotal;
	}

	public float getMoyenneDegree() {
		return this.moyenneDegree;
	}

	public void setMoyenneDegree(float moyenneDegree) {
		this.moyenneDegree = moyenneDegree;
	}

	public float getDispersionPredicatsDansLesAnnotations() {
		return this.DispersionPredicatsDansLesAnnotations;
	}

	public void setDispersionPredicatsDansLesAnnotations(float dispersionPredicatsDansLesAnnotations) {
		this.DispersionPredicatsDansLesAnnotations = dispersionPredicatsDansLesAnnotations;
	}

	public float getRapporPredicatsParGroupesAnnotations() {
		return this.RapporPredicatsParGroupesAnnotations;
	}

	public void setRapporPredicatsParGroupesAnnotations(float rapporPredicatsParGroupesAnnotations) {
		this.RapporPredicatsParGroupesAnnotations = rapporPredicatsParGroupesAnnotations;
	}

	public float getRapporPredicatsfichierParNbTotalFichier() {
		return this.RapporPredicatsfichierParNbTotalFichier;
	}

	public void setRapporPredicatsfichierParNbTotalFichier(float rapporPredicatsfichierParNbTotalFichier) {
		this.RapporPredicatsfichierParNbTotalFichier = rapporPredicatsfichierParNbTotalFichier;
	}

	public float getMoyenneNbPredicatTousLesFichierParNbTotalFichier() {
		return this.moyenneNbPredicatTousLesFichierParNbTotalFichier;
	}

	public void setMoyenneNbPredicatTousLesFichierParNbTotalFichier(
			float moyenneNbPredicatTousLesFichierParNbTotalFichier) {
		this.moyenneNbPredicatTousLesFichierParNbTotalFichier = moyenneNbPredicatTousLesFichierParNbTotalFichier;
	}

	public float getRapportAnnotationDifferenteParAnnotationTotal() {
		return this.RapportAnnotationDifferenteParAnnotationTotal;
	}

	public void setRapportAnnotationDifferenteParAnnotationTotal(float rapportAnnotationDifferenteParAnnotationTotal) {
		this.RapportAnnotationDifferenteParAnnotationTotal = rapportAnnotationDifferenteParAnnotationTotal;
	}

	public void calculerMetrics(List<AnnotationSynthese> annotationsSyntheses, Set<AnnotationGroupe> annotationsGroupes,
			List<Annotation> annotationsLineariser, int nbFichierParser) {
		// TODO
		calculerMoyenneDegree(annotationsSyntheses);
		calculerDispersionPredicatDansLesAnnotations(annotationsLineariser);
		calculerRapporPredicatsParGroupesAnnotations(annotationsGroupes);
		calculerRapporPredicatsfichierParNbTotalFichier(annotationsLineariser, nbFichierParser);
		// moyenneNbPredicatTousLesFichierParNbTotalFichier; //
		// nbPredicatsTotalDeTousLesFichier / nbFichiers // Var fan-out-on file
		// RapportAnnotationDifferenteParAnnotationTotal; // nbAnnotationsDifferent /
		// nbAnnotationsTotal d'un fichier // VP Fan in on file
	}

	public void calculerMoyenneDegree(List<AnnotationSynthese> annotationsSyntheses) {
		float sum = 0;
		for (AnnotationSynthese annotationSynthese : annotationsSyntheses) {
			sum += annotationSynthese.getMoyDegree();
		}
		this.setMoyenneDegree(sum / annotationsSyntheses.size());
	}

	public void calculerDispersionPredicatDansLesAnnotations(List<Annotation> annotationsLineariser) {
		float resultat = 0;
		int cptPredicatTotal = 0;
		final int nbAnnotationTotal = annotationsLineariser.size();
		Proposition propositionTemporaire;
		List<Predicat> predicatsTemporaire;
		for (Annotation ittAnnotation : annotationsLineariser) {
			propositionTemporaire = ittAnnotation.getProposition();
			predicatsTemporaire = propositionTemporaire.getPredicats();
			cptPredicatTotal += predicatsTemporaire.size();
		}
		resultat = cptPredicatTotal / nbAnnotationTotal;
		setDispersionPredicatsDansLesAnnotations(resultat);
	}

	public void calculerRapporPredicatsParGroupesAnnotations(Set<AnnotationGroupe> annotationsGroupes) {
		float resultat = 0;
		int cptPredicatTotal = 0;
		final int nbAnnotationGroupeTotal = annotationsGroupes.size();
		Proposition propositionTemporaire;
		List<Predicat> predicatsTemporaire;
		for (AnnotationGroupe annotationGroupe : annotationsGroupes) {
			propositionTemporaire = annotationGroupe.getProposition();
			predicatsTemporaire = propositionTemporaire.getPredicats();
			cptPredicatTotal += predicatsTemporaire.size();
		}
		resultat = cptPredicatTotal / nbAnnotationGroupeTotal;
		setRapporPredicatsParGroupesAnnotations(resultat);
	}

	public void calculerRapporPredicatsfichierParNbTotalFichier(List<Annotation> annotationsLineariser,
			int nombreFichierTotal) {
		float resultat = 0;
		int nbPredicatFichierCourant = annotationsLineariser.get(0).getProposition().getPredicats().size();
		;
		List<Integer> nbPredicatsParFichier = new ArrayList<>();

		int indiceDernier = annotationsLineariser.size() - 1;
		boolean estMemeFichier;
		boolean estAvantDernier;

		for (int i = 1; i < indiceDernier; i++) {
			String nomFichierSuivant = annotationsLineariser.get(i + 1).getNomDeFichier();
			estMemeFichier = annotationsLineariser.get(i).getNomDeFichier().equals(nomFichierSuivant);
			if (estMemeFichier) {
				nbPredicatFichierCourant += annotationsLineariser.get(i).getProposition().getPredicats().size();
			} else {
				nbPredicatFichierCourant += annotationsLineariser.get(i).getProposition().getPredicats().size();
				nbPredicatsParFichier.add(nbPredicatFichierCourant);
				nbPredicatFichierCourant = 0;
			}

			estAvantDernier = ((i + 1) == indiceDernier);
			if (estAvantDernier) {
				nbPredicatFichierCourant += annotationsLineariser.get(indiceDernier).getProposition().getPredicats()
						.size();
				nbPredicatsParFichier.add(nbPredicatFichierCourant);
			}
		}

		resultat = nbPredicatsParFichier.stream().mapToInt(e -> e).sum() / nombreFichierTotal;
		setRapporPredicatsfichierParNbTotalFichier(resultat);
	}
}