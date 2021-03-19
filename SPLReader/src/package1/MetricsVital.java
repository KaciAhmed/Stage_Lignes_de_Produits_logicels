package package1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "metricsVital")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricsVital {

	private float moyenneDegree; // sum(moyenne degree) / nbAnnotationsGroupe
	private float dispersionPredicatsDansLesAnnotations; // nbPredicatTotal / nbAnnotationTotal

	private float avgVarFanOutOnVPG; // nbPredicatTotalDeTousLesVPGs / nbVpgTotal // var
										// fan-out-onVPG
	private float avgVarFanOutOnFile; // (nbPredicatsParFichier / NbFichier) d'un fichier // var
										// fan-in-on file
	private float avgVarFanInOnFile; // nbPredicatsTotalDeTousLesFichier / nbFichiers //
										// Var fan-out-on file
	private float avgVpFanInOnFile; // nbAnnotationsDifferent / nbAnnotationsTotal d'un
	@XmlTransient // fichier // VP Fan in on file
	private Set<Predicat> ensemblePredicats;
	@XmlTransient
	private Map<Predicat, List<AnnotationGroupe>> appartenancePredicatVPG;

	public MetricsVital() {
		super();
		this.ensemblePredicats = new HashSet<>();
		this.appartenancePredicatVPG = new HashMap<Predicat, List<AnnotationGroupe>>();
	}

	public MetricsVital(float moyenneDegree, float dispersionPredicatsDansLesAnnotations,
			float rapporPredicatsParGroupesAnnotations, float rapporPredicatsfichierParNbTotalFichier,
			float moyenneNbPredicatTousLesFichierParNbTotalFichier,
			float rapportAnnotationDifferenteParAnnotationTotal) {
		super();
		this.moyenneDegree = moyenneDegree;
		this.dispersionPredicatsDansLesAnnotations = dispersionPredicatsDansLesAnnotations;
		this.avgVarFanOutOnVPG = rapporPredicatsParGroupesAnnotations;
		this.avgVarFanOutOnFile = rapporPredicatsfichierParNbTotalFichier;
		this.avgVarFanInOnFile = moyenneNbPredicatTousLesFichierParNbTotalFichier;
		this.avgVpFanInOnFile = rapportAnnotationDifferenteParAnnotationTotal;
		this.ensemblePredicats = new HashSet<>();
		this.appartenancePredicatVPG = new HashMap<Predicat, List<AnnotationGroupe>>();
	}

	public void calculerMetrics(List<AnnotationSynthese> annotationsSyntheses, Set<AnnotationGroupe> annotationsGroupes,
			List<Annotation> annotationsLineariser, int nbFichierParser) {
		// TODO
		calculerMoyenneDegree(annotationsSyntheses);
		calculerDispersionPredicatDansLesAnnotations(annotationsLineariser);
		recupererTousPredicatsProjet(annotationsGroupes);
		calculerAvgVarFanOutOnVPG(annotationsGroupes);
		// calculerRapporPredicatsParGroupesAnnotations(annotationsGroupes);
		// calculerRapporPredicatsfichierParNbTotalFichier(annotationsLineariser,
		// nbFichierParser);
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

	/*
	 * récupurer tous les prédicats du projet //set cacluer le nombre total de
	 * groupes annotations pour chaque prédicats chercher la liste des Groupes
	 * d'annotations qui le contient // caluler la cardinalité des de la liste des
	 * Groupes d'annotations diviser la cardinalité par le nombre de groupe
	 * d'annotation et stocké le résultat dans une liste sommet les élément de la
	 * liste et le diviser par le nombre d'élément de la liste
	 */
	public float calculerMoyenneParPredicat(Predicat predicat, int nbTotalGroupeAnnotation) {
		List<AnnotationGroupe> lstAnnotationGroupeOfPredicat = this.appartenancePredicatVPG.get(predicat);
		int nbOccurance = 0;
		for (AnnotationGroupe annotationGroupe : lstAnnotationGroupeOfPredicat) {
			for (Predicat predicat2 : annotationGroupe.getProposition().getPredicats()) {
				if (predicat.equals(predicat2)) {
					nbOccurance++;
				}
			}
		}
		return nbOccurance / nbTotalGroupeAnnotation;
	}

	public boolean verifierAppartenancePredicat(Predicat predicat, AnnotationGroupe annotationGroupe) {
		return annotationGroupe.getProposition().getPredicats().contains(predicat);
	}

	public void remplirAppratenancePredicatVPG(Set<AnnotationGroupe> annotationsGroupes) {
		List<AnnotationGroupe> annotationGroupesOfPredicat;
		for (Predicat predicat : this.ensemblePredicats) {
			annotationGroupesOfPredicat = new ArrayList<>();
			for (AnnotationGroupe annotationGroupe : annotationsGroupes) {
				if (verifierAppartenancePredicat(predicat, annotationGroupe)) {
					annotationGroupesOfPredicat.add(annotationGroupe);
				}
			}
			this.appartenancePredicatVPG.put(predicat, annotationGroupesOfPredicat);
		}
	}

	public void recupererTousPredicatsProjet(Set<AnnotationGroupe> annotationsGroupes) {
		for (AnnotationGroupe annotationGroupe : annotationsGroupes) {
			for (Predicat predicat : annotationGroupe.getProposition().getPredicats()) {
				this.ensemblePredicats.add(predicat);
			}
		}
	}

	public void calculerAvgVarFanOutOnVPG(Set<AnnotationGroupe> annotationsGroupes) {
		recupererTousPredicatsProjet(annotationsGroupes);
		int nbTotalGroupeAnnotation = annotationsGroupes.size();
		remplirAppratenancePredicatVPG(annotationsGroupes);
		List<Float> listMoyenneParPredicat = new ArrayList<>();
		for (Predicat predicat : this.ensemblePredicats) {
			listMoyenneParPredicat.add(Float.valueOf(calculerMoyenneParPredicat(predicat, nbTotalGroupeAnnotation)));
		}
		Double resultat = listMoyenneParPredicat.stream().mapToDouble(e -> e).sum() / listMoyenneParPredicat.size();
		this.setAvgVarFanOutOnVPG(resultat.floatValue());

	}

	// Getter && Setter
	public float getMoyenneDegree() {
		return this.moyenneDegree;
	}

	public void setMoyenneDegree(float moyenneDegree) {
		this.moyenneDegree = moyenneDegree;
	}

	public float getDispersionPredicatsDansLesAnnotations() {
		return this.dispersionPredicatsDansLesAnnotations;
	}

	public void setDispersionPredicatsDansLesAnnotations(float dispersionPredicatsDansLesAnnotations) {
		this.dispersionPredicatsDansLesAnnotations = dispersionPredicatsDansLesAnnotations;
	}

	public Set<Predicat> getEnsemblePredicats() {
		return this.ensemblePredicats;
	}

	public void setEnsemblePredicats(Set<Predicat> ensemblePredicats) {
		this.ensemblePredicats = ensemblePredicats;
	}

	public Map<Predicat, List<AnnotationGroupe>> getAppartenancePredicatVPG() {
		return this.appartenancePredicatVPG;
	}

	public void setAppartenancePredicatVPG(Map<Predicat, List<AnnotationGroupe>> appartenancePredicatVPG) {
		this.appartenancePredicatVPG = appartenancePredicatVPG;
	}

	public float getAvgVarFanOutOnVPG() {
		return this.avgVarFanOutOnVPG;
	}

	public void setAvgVarFanOutOnVPG(float avgVarFanOutOnVPG) {
		this.avgVarFanOutOnVPG = avgVarFanOutOnVPG;
	}

	public float getAvgVarFanOutOnFile() {
		return this.avgVarFanOutOnFile;
	}

	public void setAvgVarFanOutOnFile(float avgVarFanOutOnFile) {
		this.avgVarFanOutOnFile = avgVarFanOutOnFile;
	}

	public float getAvgVarFanInOnFile() {
		return this.avgVarFanInOnFile;
	}

	public void setAvgVarFanInOnFile(float avgVarFanInOnFile) {
		this.avgVarFanInOnFile = avgVarFanInOnFile;
	}

	public float getAvgVpFanInOnFile() {
		return this.avgVpFanInOnFile;
	}

	public void setAvgVpFanInOnFile(float avgVpFanInOnFile) {
		this.avgVpFanInOnFile = avgVpFanInOnFile;
	}

}