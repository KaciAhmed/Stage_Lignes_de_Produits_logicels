package package1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				nbAnnoatationConcatenable += compterNombreAnnotationConcatenable(
						annotations.get(i).getAnnotationsEnfant());
			}
		}
		return nbAnnoatationConcatenable;
	}
	
	
/****
 *  similarité de code 
 */
	public double getPourcentageSimilariteCode(List<Annotation>annotationsLineariser) {
		Map<Annotation,Double>annotationTraiter= new HashMap<Annotation,Double>();
		double moyTemp;
		double sommeMoy =0;
		for(Annotation annotation: annotationsLineariser) {
			moyTemp = getPourcentageSimilariteCode(annotation,annotationsLineariser);
			annotationTraiter.put(annotation, moyTemp);
		}
		for(Annotation annotation : annotationTraiter.keySet()) {
			sommeMoy+=annotationTraiter.get(annotation);
		}
		return sommeMoy/annotationTraiter.keySet().size();
	}
	private double getPourcentageSimilariteCode(Annotation annotation,List<Annotation>annotationsLineariser) {
		double moy=0;
		long cpt=0;
		for(Annotation annotation2 : annotationsLineariser) {
			if(!annotation.estMemeAnnotation(annotation2)) {
				moy+= getPourcentageSimilariteCodeVariant(annotation.getCodeVariant(),annotation2.getCodeVariant());
				cpt++;
			}
		}
		return moy/cpt;
	}
	private double getPourcentageSimilariteCodeVariant(CodeVariant codeVariant1,CodeVariant codeVariant2){
		long minNbLigne= codeVariant1.getLigneDeCode().size() <codeVariant2.getLigneDeCode().size() ? codeVariant1.getLigneDeCode().size() :codeVariant2.getLigneDeCode().size();
		long maxNbLigne= codeVariant1.getLigneDeCode().size() >codeVariant2.getLigneDeCode().size() ? codeVariant1.getLigneDeCode().size() :codeVariant2.getLigneDeCode().size();

		long nbLigneSimilaire=0;
		this.supprimerLignesVideCodeVariant(codeVariant1);
		this.supprimerLignesVideCodeVariant(codeVariant2);
		for(int i = 0;i<minNbLigne;i++) {
			this.pretraitmentLigneCodeVariant(codeVariant1.getLigneDeCode().get(i),codeVariant2.getLigneDeCode().get(i));
			if(codeVariant1.getLigneDeCode().get(i).equals(codeVariant2.getLigneDeCode().get(i))) {
				nbLigneSimilaire++;
			}
		}
		return nbLigneSimilaire/maxNbLigne;
	}
	private void pretraitmentLigneCodeVariant(String ligne1,String ligne2) {
		ligne1.trim(); 
		ligne1.replaceAll("\\s+","\\s");
		ligne1.replaceAll("\\t+","");
		ligne1.toLowerCase();
		
		ligne2.trim();
		ligne2.replaceAll("\\s+","\\s");
		ligne2.replaceAll("\\t+","");
		ligne2.toLowerCase();
	}
	private void supprimerLignesVideCodeVariant(CodeVariant codeVariant) {
		for(String ligne :codeVariant.getLigneDeCode()) {
			if(ligne.isEmpty())
			{
				codeVariant.getLigneDeCode().remove(ligne);
			}
		}
	}

/*
 *  Fin similarité de code 
 */
}
