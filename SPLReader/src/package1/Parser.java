package package1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
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
	public static final String REGEX_TAB = "\\t";
	public static final String STRING_VIDE = "";
	public static final String ESPACE = " ";

	public static void main(String[] args) {
		String inputDirectory = STRING_VIDE;
		String fichierSortiePrincipal = STRING_VIDE;
		String fichierSortieSynthese = STRING_VIDE;
		final long timestamp = Instant.now().getEpochSecond();
		final String nomDossierPrincipalParDefaut = "out";
		File dossierPrincipal = new File(nomDossierPrincipalParDefaut);
		dossierPrincipal.mkdir();

		final String nomDossier = "fichiersSortie" + timestamp;
		File dossier = new File(nomDossierPrincipalParDefaut + File.separator + nomDossier);
		dossier.mkdir();

		final String nomFichierSortieParDefaut = "output_";
		final String nomFichierSyntheseParDefaut = "output_synthese";
		List<Annotation> annotations;
		List<AnnotationSynthese> annotationsSyntheses;

		if (args.length > 0) {
			inputDirectory = args[0];
			if (args.length > 1) {
				fichierSortiePrincipal = args[1];
			} else {
				fichierSortiePrincipal = nomDossierPrincipalParDefaut + File.separator + nomDossier + File.separator
						+ nomFichierSortieParDefaut;
				fichierSortieSynthese = nomDossierPrincipalParDefaut + File.separator + nomDossier + File.separator
						+ nomFichierSyntheseParDefaut;
			}
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
		List<Annotation> annotationsIntermediaire = parseur.lineariserAnnotations(annotations);
		annotationsSyntheses = parseur.creerAnnotationsSyntheses(annotationsIntermediaire);
		AnnotationsSynthesesWrapper annotationsSynthesesWrapper = new AnnotationsSynthesesWrapper(annotationsSyntheses);
		jaxbObjectToXML(annotationsSynthesesWrapper, fichierSortieSynthese);

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

	/*********************
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
			String[] motsCles = ligneCourante.trim().split(ESPACE);
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
		return motsCles[0].contains(debutAnnotation);
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

	private List<AnnotationSynthese> creerAnnotationsSyntheses(List<Annotation> annotations) {

		List<AnnotationSynthese> annotationsSyntheses = new ArrayList<AnnotationSynthese>();
		Set<AnnotationGroupe> annotationsGroupes = creerAnnotationsGroupes(annotations);

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
}
