package package1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Parser {
	private static final String DEBUT_ANNOTATION_NON_ESPACER = "//";
	private static final String DEBUT_ANNOTATION_ESPACER = "//\\s";
	public static final String REGEX_TAB = "\\t";
	public static final String STRING_VIDE = "";
	public static final String ESPACE = " ";
	public static int nbFichierParser = 0;

	public static void main(String[] args) {
		List<Annotation> annotations;
		List<AnnotationSynthese> annotationsSyntheses;

		String inputDirectory = STRING_VIDE;
		final String nomDossierSortiePrincipalParDefaut = "out";
		File dossierSortiePrincipal = new File(nomDossierSortiePrincipalParDefaut);
		dossierSortiePrincipal.mkdir();

		final long timestamp = Instant.now().getEpochSecond();
		final String nomDossierSortie = "Sortie" + timestamp;
		File dossier = new File(nomDossierSortiePrincipalParDefaut + File.separator + nomDossierSortie);
		dossier.mkdir();

		String fichierSortiePrincipal = STRING_VIDE;
		String fichierSortieSynthese = STRING_VIDE;
		String fichierSortieImplication = STRING_VIDE;

		final String nomFichierSortie = "output_";
		final String nomFichierSynthese = "output_synthese";
		final String nomFichierImplication = "output_implication";

		if (args.length > 0) {
			inputDirectory = args[0];
			if (args.length > 1) {
				fichierSortiePrincipal = args[1];
			} else {
				fichierSortiePrincipal = nomDossierSortiePrincipalParDefaut + File.separator + nomDossierSortie
						+ File.separator + nomFichierSortie;
			}
			fichierSortieSynthese = nomDossierSortiePrincipalParDefaut + File.separator + nomDossierSortie
					+ File.separator + nomFichierSynthese;
			fichierSortieImplication = nomDossierSortiePrincipalParDefaut + File.separator + nomDossierSortie
					+ File.separator + nomFichierImplication;
		} else {
			System.out.println("Usage <path/to/code/> <optional/path/to/result.xml>");
		}

		fichierSortiePrincipal += ".xml";
		fichierSortieSynthese += ".xml";
		Parser parseur = new Parser();
		annotations = parseur.parser(inputDirectory);
		AnnotationsWrapper annotationsWrapper = new AnnotationsWrapper(annotations);
		annotationsWrapper.setNomFichierInput(inputDirectory);
		jaxbObjectToXML(annotationsWrapper, fichierSortiePrincipal);
		List<Annotation> annotationsLineariser = parseur.lineariserAnnotations(annotations);
		Set<AnnotationGroupe> annotationsGroupes = parseur.creerAnnotationsGroupes(annotations);
		annotationsSyntheses = parseur.creerAnnotationsSyntheses(annotationsGroupes);
		MetricsVital metricsVital = new MetricsVital();
		metricsVital.calculerMetrics(annotationsSyntheses, annotationsGroupes, annotationsLineariser, nbFichierParser);
		AnnotationsSynthesesWrapper annotationsSynthesesWrapper = new AnnotationsSynthesesWrapper(annotationsSyntheses,
				metricsVital);
		jaxbObjectToXML(annotationsSynthesesWrapper, fichierSortieSynthese);

		Set<String> implications = parseur.genererImplications(annotations, new ArrayList<Predicat>());
		for (String str : implications) {
			System.out.println(str);
		}
		parseur.creerFichierImplication(implications, fichierSortieImplication);
	}

	public List<Annotation> parser(String inputDirectory) {
		File dossier = new File(inputDirectory);
		List<Annotation> annotationFinale = lireDossier(dossier);
		return annotationFinale;
	}

	/****************************
	 * Lecture dossier
	 ***************************************/
	private List<Annotation> lireDossier(File dossier) {
		String nomDeFichier;
		List<Annotation> annotations = new ArrayList<>();
		List<Annotation> annotationsCalculer = new ArrayList<>();
		List<String> lignesFichier = null;
		Annotation annotationCourante;
		Stack<Annotation> pileAnnotation;
		List<Annotation> annotationsPrecalculer = null;
		final int indiceCurseurDeLigne = 0;
		final int degre = 0;

		for (File fichier : dossier.listFiles()) {
			if (!fichier.isDirectory()) {
				if (estFichierVoulu(fichier)) {
					System.out
							.println("-------------------------" + fichier.getName() + "-----------------------------");
					nbFichierParser++;
					nomDeFichier = fichier.getAbsolutePath();
					annotations = new ArrayList<Annotation>();
					pileAnnotation = new Stack<Annotation>();
					annotationCourante = null;
					lignesFichier = lireFichier(nomDeFichier);
					creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
							annotationCourante, degre, indiceCurseurDeLigne);
					annotationsPrecalculer = simplifierAnnotations(annotations);
					concatenerListes(annotationsCalculer, annotationsPrecalculer);
				}
			} else {
				annotationsPrecalculer = lireDossier(fichier);
				concatenerListes(annotationsCalculer, annotationsPrecalculer);
			}
		}
		return annotationsCalculer;
	}

	private boolean estFichierVoulu(File fichier) {

		final String extensionFichierVoulu = ".java";
		final int indiceFinFichier = fichier.getName().length();
		final int indiceExtensionFichier = indiceFinFichier - extensionFichierVoulu.length();
		final String fichierExtension = fichier.getName().substring(indiceExtensionFichier, indiceFinFichier);

		return fichierExtension.equals(extensionFichierVoulu);
	}

	/************************************
	 * Lecture fichier
	 *********************************/
	private List<String> lireFichier(String nomDeFichier) {
		List<String> contenuFichier = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(nomDeFichier);
			InputStreamReader iReader = new InputStreamReader(new FileInputStream(nomDeFichier), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(iReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				contenuFichier.add(line);
			}
			iReader.close();
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contenuFichier;
	}

	/**********************
	 * creer arboraissance annotation
	 *************************************/

	private void creerArborescenceDesAnnotations(String nomDeFichier, List<String> lignesFichier,
			List<Annotation> annotations, Stack<Annotation> pileAnnotation, Annotation annotationCourante, int degre,
			int indiceCurseurDeLigne) {
		if (estCurseurFin(lignesFichier, indiceCurseurDeLigne)) {
			System.out.println("Tout est visite");
		} else {
			String ligneCourante = lignesFichier.get(indiceCurseurDeLigne);
			ligneCourante = ligneCourante.replaceAll(REGEX_TAB, STRING_VIDE);
			int nbCharLigneCourante = ligneCourante.length();
			String[] motsCles = ligneCourante.trim().replaceAll(DEBUT_ANNOTATION_ESPACER, DEBUT_ANNOTATION_NON_ESPACER)
					.split(ESPACE);
			if (estDebutAnnotation(motsCles)) {
				miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				degre++;
				indiceCurseurDeLigne++;
				annotationCourante = creerAnnotation(nomDeFichier, ligneCourante, degre, indiceCurseurDeLigne,
						nbCharLigneCourante);
				pileAnnotation.push(annotationCourante);
				creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne);
			} else if (estFinAnnotation(motsCles)) {
				miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				annotationCourante = pileAnnotation.pop();
				annotationCourante.ajouterLigneDeCodeVariant(ligneCourante);
				if (!pileAnnotation.isEmpty()) {
					Annotation annotationParent = pileAnnotation.pop();
					annotationParent.ajouterEnfant(annotationCourante);
					pileAnnotation.push(annotationParent);
				} else {
					annotations.add(annotationCourante);
				}
				degre--;
				indiceCurseurDeLigne++;
				creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne);
			} else if (estMillieuAnnotation(motsCles)) {
				miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				indiceCurseurDeLigne++;
				creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne);
			}
		}
	}

	private void concatenerListes(List<Annotation> lst1, List<Annotation> lst2) {
		for (Annotation annotation : lst2) {
			lst1.add(annotation);
		}
	}

	private boolean estCurseurFin(List<String> lstContenu, int indice) {
		return indice == lstContenu.size();
	}

	private boolean estDebutAnnotation(String[] motsCles) {
		final String debutAnnotation = "//#if";
		boolean resultat = false;
		String concat = "";
		for (String string : motsCles) {
			concat += motsCles[0];
			if (concat.contains(debutAnnotation)) {
				resultat = true;
			}
		}
		return resultat;
	}

	private boolean estFinAnnotation(String[] motsCles) {
		final String finAnnotation = "//#endif";
		return motsCles[0].contains(finAnnotation);
	}

	private boolean estDebutOuFinAnnotation(String[] motsCle) {
		return estDebutAnnotation(motsCle) || estFinAnnotation(motsCle);
	}

	private boolean estMillieuAnnotation(String[] motsCle) {
		return !estDebutOuFinAnnotation(motsCle);
	}

	private void miseAjourPileAnnotation(Stack<Annotation> pileAnnotation, int nbCharLigneCourante) {
		for (Annotation annotation : pileAnnotation) {
			annotation.incrementNombreDeLigne();
			annotation.ajouterNombreDeCaractere(nbCharLigneCourante);
		}
	}

	private Annotation creerAnnotation(String nomDeFichier, String ligne, int degre, int indice,
			int nbCharLigneCourante) {
		final int NB_LIGNE_DEBUT = 1;
		List<String> lst = new ArrayList<>();
		lst.add(ligne);
		CodeVariant codeVariant = new CodeVariant(lst);
		Proposition proposition = Parser.creerProposition(ligne);
		Annotation annotationCourante = new AnnotationComposer(nomDeFichier, indice, NB_LIGNE_DEBUT,
				nbCharLigneCourante, degre, codeVariant, proposition);
		return annotationCourante;
	}

	private static Proposition creerProposition(String ligne) {
		Proposition proposition = new Proposition();
		proposition.parserFormule(ligne);
		return proposition;
	}

	private List<Annotation> simplifierAnnotations(List<Annotation> lstAnnotations) {
		List<Annotation> lst = new ArrayList<Annotation>();
		Annotation annotationComposer = null;
		for (int i = 0; i < lstAnnotations.size(); i++) {
			annotationComposer = lstAnnotations.get(i);
			Annotation annotationSimplifier = simplifierUneAnnotation(annotationComposer);
			lst.add(annotationSimplifier);
		}
		return lst;
	}

	private Annotation simplifierUneAnnotation(Annotation annotationComposer) {
		Annotation annotationRetourner = null;
		List<Annotation> annotationsEnfantCourant = annotationComposer.getAnnotationsEnfant();
		if (annotationsEnfantCourant.isEmpty()) {
			annotationRetourner = creerAnnotationSimple(annotationComposer);
		} else {
			List<Annotation> annotationsEnfantSimplifier = simplifierAnnotations(annotationsEnfantCourant);
			annotationComposer.setAnnotationsEnfant(annotationsEnfantSimplifier);
			annotationRetourner = annotationComposer;
		}
		return annotationRetourner;
	}

	private AnnotationSimple creerAnnotationSimple(Annotation annotationComposer) {
		return new AnnotationSimple(annotationComposer.getNomDeFichier(), annotationComposer.getDebutDeLigne(),
				annotationComposer.getNombreDeLigne(), annotationComposer.getNombreDeCaractere(),
				annotationComposer.getDegre(), annotationComposer.getCodeVariant(),
				annotationComposer.getProposition());
	}

	private static void jaxbObjectToXML(AnnotationsWrapper annotationsWrapper, String outputFileName) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AnnotationsWrapper.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			jaxbMarshaller.marshal(annotationsWrapper, new File(outputFileName));

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void afficherAnnotations(List<Annotation> lstAnnotations) {
		for (Annotation annotation : lstAnnotations) {
			annotation.afficherArborescence();
		}
	}

	/********************************************
	 * Annotation synthese *************
	 **************/
	public void afficherSynthese(List<AnnotationSynthese> annotationSyntheses) {
		for (AnnotationSynthese annotationSynthese : annotationSyntheses) {
			System.out.println(annotationSynthese);
		}
	}

	private List<Annotation> lineariserAnnotations(List<Annotation> annotations) {
		List<Annotation> annotationsLineaire = new ArrayList<>();
		for (Annotation annotation : annotations) {
			if (annotation instanceof AnnotationSimple) {
				annotationsLineaire.add(annotation);
			} else {
				annotationsLineaire.add(annotation);
				annotationsLineaire.addAll(lineariserAnnotations(annotation.getAnnotationsEnfant()));
			}
		}
		return annotationsLineaire;
	}

	private List<AnnotationSynthese> creerAnnotationsSyntheses(Set<AnnotationGroupe> annotationsGroupes) {

		List<AnnotationSynthese> annotationsSyntheses = new ArrayList<AnnotationSynthese>();

		for (AnnotationGroupe annotationGroupe : annotationsGroupes) {

			AnnotationSynthese resultat = creerAnnotationSynthese(annotationGroupe);
			annotationsSyntheses.add(resultat);
		}

		return annotationsSyntheses;
	}

	private AnnotationSynthese creerAnnotationSynthese(AnnotationGroupe annotationGroupe) {
		AnnotationSynthese annotationResultat = new AnnotationSynthese();
		annotationResultat.setProposition(annotationGroupe.getProposition());

		int sumLignes;
		int sumDegrees;
		int sumNbcaracteres;
		int nbAnnotationSimple = 0;
		int nbAnnotationComp = 0;

		for (Annotation annotation : annotationGroupe.getAnnotations()) {
			annotationResultat.getLstNombreDeLigne().add(annotation.getNombreDeLigne());
			annotationResultat.getLstNombreDeCaractere().add(annotation.getNombreDeCaractere());
			annotationResultat.getLstDegree().add(annotation.getDegre());
			if (annotation instanceof AnnotationSimple) {
				nbAnnotationSimple++;
			} else {

				nbAnnotationComp++;
			}

		}
		annotationResultat.setNbAnnotationSimple(nbAnnotationSimple);
		annotationResultat.setNbAnnotationComp(nbAnnotationComp);

		sumLignes = annotationResultat.getLstNombreDeLigne().stream().mapToInt(e -> e).sum();
		annotationResultat.setMoyNombreLigne(sumLignes / annotationResultat.getLstNombreDeLigne().size());

		sumDegrees = annotationResultat.getLstDegree().stream().mapToInt(e -> e).sum();
		annotationResultat.setMoyDegree(sumDegrees / annotationResultat.getLstDegree().size());

		sumNbcaracteres = annotationResultat.getLstNombreDeCaractere().stream().mapToInt(e -> e).sum();
		annotationResultat.setMoyNombreCaractere(sumNbcaracteres / annotationResultat.getLstNombreDeLigne().size());

		return annotationResultat;
	}

	private Set<AnnotationGroupe> creerAnnotationsGroupes(List<Annotation> annotations) {
		Set<AnnotationGroupe> annotationsGroupes = new HashSet<>();
		AnnotationGroupe annotationGroupe;

		for (Annotation annotation : annotations) {
			annotationGroupe = new AnnotationGroupe(annotation.getProposition());
			annotationsGroupes.add(annotationGroupe);
		}
		for (Annotation annotation : annotations) {
			for (AnnotationGroupe annotationGroupe2 : annotationsGroupes) {
				annotationGroupe2.ajouterAnnotation(annotation);
			}
		}
		return annotationsGroupes;
	}

	private static void jaxbObjectToXML(AnnotationsSynthesesWrapper annotationsSynthesesWrapper,
			String outputFileName) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AnnotationsSynthesesWrapper.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			jaxbMarshaller.marshal(annotationsSynthesesWrapper, new File(outputFileName));

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/***********************************
	 * implication des annotations
	 */
	public boolean estAnnotationComposer(Annotation annotation) {
		try {
			return !annotation.getAnnotationsEnfant().isEmpty();
		} catch (UnsupportedOperationException e) {
			return false;
		}

	}

	private void concatenerSetsString(Set<String> set1, Set<String> set2) {
		for (String str : set2) {
			set1.add(str);
		}
	}

	public Set<String> genererImplications(List<Annotation> annotations, List<Predicat> predicatsAnnotationMere) {
		Set<String> resultats = new HashSet<>();
		List<Predicat> predicatsAnnotation;
		String implication;
		for (Annotation annotation : annotations) {
			predicatsAnnotation = annotation.getProposition().getPredicats();
			for (Predicat predicatAnnotation : predicatsAnnotation) {
				for (Predicat predicatAnnotationMere : predicatsAnnotationMere) {
					implication = predicatAnnotation.getNom() + " => " + predicatAnnotationMere;
					resultats.add(implication);
				}
			}

			if (estAnnotationComposer(annotation)) {
				for (Annotation annotation2 : annotation.getAnnotationsEnfant()) {
					this.concatenerSetsString(resultats,
							genererImplications(annotation.getAnnotationsEnfant(), predicatsAnnotation));
				}
			}
		}
		return resultats;
	}

	public void creerFichierImplication(Set<String> implications, String fichierSortieImplication) {
		try {
			FileWriter fw = new FileWriter(fichierSortieImplication, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			for (String implication : implications) {
				out.println(implication);
			}
			out.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Exception écriture fichier");
			e.printStackTrace();
		}
	}
}
