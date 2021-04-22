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

	private float moyenneDegree; // sum(moyenne degree dans l'annotation synthèese) / nbAnnotationsGroupe(nb annotationn synthèse = nb annotations groupes)
	private float dispersionPredicatsDansLesAnnotations; // nbPredicatTotal / nbAnnotationTotal
	private double avgVarFanOutOnVPG; // nbPredicatTotalDeTousLesVPGs / nbVpgTotal // var // fan-out-onVPG
	private float avgVarFanOutOnFile; // (nbPredicatsParFichier / NbFichier) d'un fichier // var // fan-in-on file
	private float avgVarFanInOnFile; // nbPredicatsTotalDeTousLesFichier / nbFichiers // // Var fan-out-on file
	private float avgVpFanInOnFile; // nbAnnotationsDifferent / nbAnnotationsTotal d'un
	@XmlTransient
	private Set<Predicat> ensemblePredicats;
	@XmlTransient
	private Map<Predicat, List<AnnotationGroupe>> appartenancePredicatVPG;
	@XmlTransient
	private Map<Predicat, List<String>> appartenancePredicatFichier;
	@XmlTransient
	private Map<Annotation, List<String>> appartenanceAnnotationFichier;

	public MetricsVital() {
		super();
		this.ensemblePredicats = new HashSet<>();
		this.appartenancePredicatVPG = new HashMap<Predicat, List<AnnotationGroupe>>();
		this.appartenancePredicatFichier = new HashMap<Predicat, List<String>>();
		this.appartenanceAnnotationFichier = new HashMap<>();
	}

	public MetricsVital(float moyenneDegree, float dispersionPredicatsDansLesAnnotations,
			double rapporPredicatsParGroupesAnnotations, float rapporPredicatsfichierParNbTotalFichier,
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
		this.appartenancePredicatFichier = new HashMap<Predicat, List<String>>();
		this.appartenanceAnnotationFichier = new HashMap<>();

	}

	public void calculerMetrics(List<AnnotationSynthese> annotationsSyntheses, Set<AnnotationGroupe> annotationsGroupes,
			List<Annotation> annotationsLineariser, int nbFichierParser) {
		this.calculerMoyenneDegree(annotationsSyntheses);
		this.calculerDispersionPredicatDansLesAnnotations(annotationsLineariser);
		this.recupererTousPredicatsProjet(annotationsGroupes);
		this.calculerAvgVarFanOutOnVPG(annotationsGroupes);
		this.calculerAvgVarFanOutOnFile(annotationsGroupes, annotationsLineariser);
		this.calculerAvgVarFanInOnFile(annotationsGroupes, annotationsLineariser);
		this.calculerAvgVpFanInOnFile(annotationsLineariser);
	}

	public void calculerMoyenneDegree(List<AnnotationSynthese> annotationsSyntheses) {
		float sum = 0;
		for (AnnotationSynthese annotationSynthese : annotationsSyntheses) {
			sum += annotationSynthese.getMoyenneDegree();
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
		this.setDispersionPredicatsDansLesAnnotations(resultat);
	}

	public double calculerMoyenneVPGParPredicat(Predicat predicat, int nbTotalGroupeAnnotation) {
		List<AnnotationGroupe> lstAnnotationGroupeOfPredicat = this.appartenancePredicatVPG.get(predicat);
		int nbOccurance = 0;
		for (AnnotationGroupe annotationGroupe : lstAnnotationGroupeOfPredicat) {
			for (Predicat predicat2 : annotationGroupe.getProposition().getPredicats()) {
				if (predicat.equals(predicat2)) {
					nbOccurance++;
				}
			}
		}
		return (nbOccurance + 0.0) / (nbTotalGroupeAnnotation + 0.0);
	}

	public boolean verifierAppartenancePredicat(Predicat predicat, AnnotationGroupe annotationGroupe) {
		return annotationGroupe.getProposition().getPredicats().contains(predicat);
	}

	public void remplirAppratenancePredicatVPG(Set<AnnotationGroupe> annotationsGroupes) {
		List<AnnotationGroupe> annotationGroupesOfPredicat;
		for (Predicat predicat : this.ensemblePredicats) {
			annotationGroupesOfPredicat = new ArrayList<>();
			for (AnnotationGroupe annotationGroupe : annotationsGroupes) {
				if (this.verifierAppartenancePredicat(predicat, annotationGroupe)) {
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
		this.recupererTousPredicatsProjet(annotationsGroupes);
		int nbTotalGroupeAnnotation = annotationsGroupes.size();
		this.remplirAppratenancePredicatVPG(annotationsGroupes);
		List<Double> listMoyenneParPredicat = new ArrayList<>();
		double moyenneVPG = 0.0;
		for (Predicat predicat : this.ensemblePredicats) {
			moyenneVPG = this.calculerMoyenneVPGParPredicat(predicat, nbTotalGroupeAnnotation);
			listMoyenneParPredicat.add(moyenneVPG);
		}
		double resultat = listMoyenneParPredicat.stream().mapToDouble(e -> e).sum()
				/ (listMoyenneParPredicat.size() + 0.0);
		resultat = Math.round(resultat * 100.0) / 100.0;
		this.setAvgVarFanOutOnVPG(resultat);
	}

	public float calculerMoyenneFichiersParPredicat(Predicat predicat, int nbTotalFichier) {
		List<String> listeFichierDuPredicat = this.appartenancePredicatFichier.get(predicat);
		return listeFichierDuPredicat.size() / nbTotalFichier;
	}

	public boolean verifierAppartenancePredicat(Predicat predicat, Annotation annotation) {
		return annotation.getProposition().getPredicats().contains(predicat);
	}

	public void remplirAppratenancePredicatFichier(List<Annotation> annotations) {
		List<String> listeFichierDuPredicat;
		for (Predicat predicat : this.ensemblePredicats) {
			listeFichierDuPredicat = new ArrayList<>();
			for (Annotation annotation : annotations) {
				if (this.verifierAppartenancePredicat(predicat, annotation)) {
					listeFichierDuPredicat.add(annotation.getNomDeFichier());
				}
			}
			this.appartenancePredicatFichier.put(predicat, listeFichierDuPredicat);
		}
	}

	public Set<String> recupererTousLesFichiers(List<Annotation> annotations) {
		Set<String> ensembleFichiers = new HashSet<>();
		for (Annotation annotation : annotations) {
			ensembleFichiers.add(annotation.getNomDeFichier());
		}
		return ensembleFichiers;
	}

	public void calculerAvgVarFanOutOnFile(Set<AnnotationGroupe> annotationsGroupes, List<Annotation> annotations) {
		this.recupererTousPredicatsProjet(annotationsGroupes);
		Set<String> listeFichiers = this.recupererTousLesFichiers(annotations);
		int nbTotalFichier = listeFichiers.size();
		this.remplirAppratenancePredicatFichier(annotations);
		List<Float> listMoyenneParPredicat = new ArrayList<>();
		for (Predicat predicat : this.ensemblePredicats) {
			listMoyenneParPredicat
					.add(Float.valueOf(this.calculerMoyenneFichiersParPredicat(predicat, nbTotalFichier)));
		}
		Double resultat = listMoyenneParPredicat.stream().mapToDouble(e -> e).sum() / listMoyenneParPredicat.size();
		this.setAvgVarFanOutOnFile(resultat.floatValue());

	}

	public float calculerMoyenneFichiersParPredicatFanIn(Predicat predicat, int nbTotalFichier,
			List<Annotation> annotations) {
		List<String> listeFichierDuPredicat = this.appartenancePredicatFichier.get(predicat);
		int nbOccurance = 0;
		for (Annotation annotation : annotations) {
			for (String nomFichier : listeFichierDuPredicat) {
				if (annotation.getNomDeFichier().equals(nomFichier)) {
					for (Predicat predicat2 : annotation.getProposition().getPredicats()) {
						if (predicat.equals(predicat2)) {
							nbOccurance++;
						}
					}
				}
			}
		}
		return nbOccurance / nbTotalFichier;
	}

	public void calculerAvgVarFanInOnFile(Set<AnnotationGroupe> annotationsGroupes, List<Annotation> annotations) {
		this.recupererTousPredicatsProjet(annotationsGroupes);
		Set<String> listeFichiers = this.recupererTousLesFichiers(annotations);
		int nbTotalFichier = listeFichiers.size();
		this.remplirAppratenancePredicatFichier(annotations);
		List<Float> listMoyenneParPredicat = new ArrayList<>();
		for (Predicat predicat : this.ensemblePredicats) {
			listMoyenneParPredicat
					.add(Float.valueOf(this.calculerMoyenneFichiersParPredicat(predicat, nbTotalFichier)));
		}
		Double resultat = listMoyenneParPredicat.stream().mapToDouble(e -> e).sum() / listMoyenneParPredicat.size();
		this.setAvgVarFanInOnFile(resultat.floatValue());

	}

	public float calculerMoyenneFichierParAnnotation(Annotation annotation, int nbTotalFichiers,
			List<Annotation> annotations) {
		List<String> listeFichierOfAnnotation = this.appartenanceAnnotationFichier.get(annotation);
		int nbOccurance = 0;
		for (Annotation annotation2 : annotations) {
			for (String nomFichier : listeFichierOfAnnotation) {
				if (annotation2.getNomDeFichier().equals(nomFichier)
						&& annotation2.getProposition().equals(annotation.getProposition())) {
					nbOccurance++;
				}
			}
		}
		return nbOccurance / nbTotalFichiers;
	}

	public void remplirAppratenanceAnnotationFichier(List<Annotation> annotations, Set<String> listeTotalFichiers) {
		List<String> listeFichierOfAnnotation;
		for (Annotation annotation : annotations) {
			listeFichierOfAnnotation = new ArrayList<>();
			for (String nomFichier : listeTotalFichiers) {
				if (annotation.getNomDeFichier().equals(nomFichier)) {
					listeFichierOfAnnotation.add(annotation.getNomDeFichier());
				}
			}
			this.appartenanceAnnotationFichier.put(annotation, listeFichierOfAnnotation);
		}
	}

	public void calculerAvgVpFanInOnFile(List<Annotation> annotations) {
		Set<String> listeFichiers = this.recupererTousLesFichiers(annotations);
		int nbTotalFichier = listeFichiers.size();
		this.remplirAppratenanceAnnotationFichier(annotations, listeFichiers);
		List<Float> listMoyenneFichierParAnnotation = new ArrayList<>();
		for (Annotation annotation : annotations) {
			listMoyenneFichierParAnnotation.add(
					Float.valueOf(this.calculerMoyenneFichierParAnnotation(annotation, nbTotalFichier, annotations)));
		}
		Double resultat = listMoyenneFichierParAnnotation.stream().mapToDouble(e -> e).sum()
				/ listMoyenneFichierParAnnotation.size();
		this.setAvgVpFanInOnFile(resultat.floatValue());

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

	public double getAvgVarFanOutOnVPG() {
		return this.avgVarFanOutOnVPG;
	}

	public void setAvgVarFanOutOnVPG(double avgVarFanOutOnVPG) {
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