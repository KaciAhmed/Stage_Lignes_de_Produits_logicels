package package1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
	private static final String DEBUT_ANNOTATION_ESPACER = "//\\s++";
	public static final String REGEX_TAB = "\\t++";
	public static final String STRING_VIDE = "";
	public static final String ESPACE = " ";
	public static int nbFichierParser = 0;
	public static String inputDirectory = STRING_VIDE;
	private static List<String> lignesTotalsFichier = null;

	public static void main(String[] args) {
		List<Annotation> annotations;
		Parser parseur = new Parser();
		if (args.length > 0) {
			inputDirectory = args[0];
			lignesTotalsFichier = new ArrayList<>();
			annotations = parseur.parser(inputDirectory);
			parseur.calculerEtEcrireFichiersResultat(args, annotations);
		} else {
			System.out.println("Usage <path/to/code/> <optional/path/to/result.xml>");
		}
	}

	public List<Annotation> parser(String inputDirectory) {
		File dossier = new File(inputDirectory);
		List<Annotation> annotationFinale = this.lireDossier(dossier);
		return annotationFinale;
	}

	public void calculerEtEcrireFichiersResultat(String[] args, List<Annotation> annotations) {
		StringBuilder builder = new StringBuilder();

		final long timestamp = Instant.now().getEpochSecond();

		String cheminDossier = STRING_VIDE;
		String cheminFichierAnnotations = STRING_VIDE;
		String cheminFichierSynthese = STRING_VIDE;
		String cheminFichierImplication = STRING_VIDE;
		String cheminFichierLite = STRING_VIDE;
		String cheminFichierMetricsAdditionnel = STRING_VIDE;
		String cheminFichierImage = STRING_VIDE;

		final String nomDossierRacine = "out";
		File dossierParDefaut = new File(nomDossierRacine);
		dossierParDefaut.mkdir();

		final String nomDossierSortie = "Sortie" + timestamp;

		builder = new StringBuilder();
		builder.append(nomDossierRacine).append(File.separator).append(nomDossierSortie);
		cheminDossier = builder.toString();

		File dossier = new File(cheminDossier);
		dossier.mkdir();

		final String nomFichierAnnotations = "output";
		final String nomFichierSynthese = "output_synthese";
		final String nomFichierImplication = "output_implication";
		final String nomFichierSortieLite = "output_lite";
		final String nomFichierMetricsAdditionnel = "output_MetricsAdditionnel";
		final String nomFichierImage = "imageMatriceSimilarite";

		builder = new StringBuilder();
		if (args.length > 1) {
			cheminFichierAnnotations = args[1];
		} else {
			builder.append(nomDossierRacine).append(File.separator).append(nomDossierSortie).append(File.separator)
					.append(nomFichierAnnotations);

		}
		builder.append(".xml");
		cheminFichierAnnotations = builder.toString();

		builder = new StringBuilder();
		builder.append(nomDossierRacine).append(File.separator).append(nomDossierSortie).append(File.separator)
				.append(nomFichierSynthese).append(".xml");
		cheminFichierSynthese = builder.toString();

		builder = new StringBuilder();
		builder.append(nomDossierRacine).append(File.separator).append(nomDossierSortie).append(File.separator)
				.append(nomFichierImplication).append(".txt");
		cheminFichierImplication = builder.toString();

		builder = new StringBuilder();
		builder.append(nomDossierRacine).append(File.separator).append(nomDossierSortie).append(File.separator)
				.append(nomFichierSortieLite).append(".txt");
		cheminFichierLite = builder.toString();

		builder = new StringBuilder();
		builder.append(nomDossierRacine).append(File.separator).append(nomDossierSortie).append(File.separator)
				.append(nomFichierMetricsAdditionnel).append(".xml");
		cheminFichierMetricsAdditionnel = builder.toString();

		builder = new StringBuilder();
		builder.append(nomDossierRacine).append(File.separator).append(nomDossierSortie).append(File.separator)
				.append(nomFichierImage).append(".pgm");
		cheminFichierImage = builder.toString();
		this.ecrireFichierAnnotations(annotations, cheminFichierAnnotations);
		this.ecrireFichierSynthese(annotations, cheminFichierSynthese);
		this.ecrireFichierImplication(annotations, cheminFichierImplication);
		this.ecrireFichierAnnotationsLite(annotations, cheminFichierLite);
		this.ecrireFichierMetricsAdditionnel(annotations, cheminFichierMetricsAdditionnel, cheminFichierImage);
	}

	/****************************
	 * Lecture dossier
	 ***************************************/

	private List<Annotation> lireDossier(File dossier) {
		String nomDeFichier;
		List<String> lignesFichier = null;
		List<Annotation> annotations = new ArrayList<>();
		List<Annotation> annotationsCalculer = new ArrayList<>();
		Annotation annotationCourante;
		Stack<Annotation> pileAnnotation;
		List<Annotation> annotationsPrecalculer = null;
		final int indiceCurseurDeLigne = 0;
		final int degre = 0;

		for (File fichier : dossier.listFiles()) {
			if (!fichier.isDirectory()) {
				if (this.estFichierVoulu(fichier)) {
					String nomFichier = "-------------------------" + fichier.getName()
							+ "-----------------------------";
					System.out.println(nomFichier);
					nbFichierParser++;
					nomDeFichier = fichier.getAbsolutePath();
					annotations = new ArrayList<Annotation>();
					pileAnnotation = new Stack<Annotation>();
					annotationCourante = null;
					lignesFichier = this.lireFichier(nomDeFichier);
					lignesTotalsFichier.addAll(lignesFichier);
					this.creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
							annotationCourante, degre, indiceCurseurDeLigne);
					annotationsPrecalculer = this.simplifierAnnotations(annotations);
					annotationsCalculer.addAll(annotationsPrecalculer);
				}
			} else {
				annotationsPrecalculer = this.lireDossier(fichier);
				annotationsCalculer.addAll(annotationsPrecalculer);
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

	/****************************
	 * Lecture fichier
	 ***************************************/

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

	/****************************
	 * creer arborescence annotation
	 ***************************************/
	private void ecrireFichierAnnotations(List<Annotation> annotations, String cheminFichierSortieAnnotations) {
		AnnotationsWrapper annotationsWrapper = new AnnotationsWrapper(annotations);
		annotationsWrapper.setNomFichierInput(inputDirectory);
		jaxbObjectToXML(annotationsWrapper, cheminFichierSortieAnnotations);
	}

	private void creerArborescenceDesAnnotations(String nomDeFichier, List<String> lignesFichier,
			List<Annotation> annotations, Stack<Annotation> pileAnnotation, Annotation annotationCourante, int degre,
			int indiceCurseurDeLigne) {
		if (this.estCurseurFin(lignesFichier, indiceCurseurDeLigne)) {
			// Tout est visité
		} else {
			String ligneCourante = lignesFichier.get(indiceCurseurDeLigne);
			ligneCourante = ligneCourante.replaceAll(REGEX_TAB, STRING_VIDE);
			int nbCharLigneCourante = ligneCourante.length();
			String[] motsCles = ligneCourante.trim().replaceAll(DEBUT_ANNOTATION_ESPACER, DEBUT_ANNOTATION_NON_ESPACER)
					.split(ESPACE);
			if (this.estDebutAnnotation(motsCles)) {
				this.miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				degre++;
				indiceCurseurDeLigne++;
				annotationCourante = this.creerAnnotation(nomDeFichier, ligneCourante, degre, indiceCurseurDeLigne,
						nbCharLigneCourante);
				pileAnnotation.push(annotationCourante);
				this.creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne);
			} else if (this.estFinAnnotation(motsCles)) {
				this.miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				annotationCourante = pileAnnotation.pop();
				if (!pileAnnotation.isEmpty()) {
					Annotation annotationParent = pileAnnotation.pop();
					annotationParent.ajouterEnfant(annotationCourante);
					pileAnnotation.push(annotationParent);
				} else {
					annotations.add(annotationCourante);
				}
				degre--;
				indiceCurseurDeLigne++;
				this.creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne);
			} else if (this.estMillieuAnnotation(motsCles)) {
				this.miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				if (!pileAnnotation.isEmpty()) {
					Annotation annotationParent = pileAnnotation.pop();
					annotationParent.ajouterLigneDeCodeVariant(ligneCourante);
					pileAnnotation.push(annotationParent);
				}
				indiceCurseurDeLigne++;
				this.creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne);
			}
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
			concat += string;
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
		return this.estDebutAnnotation(motsCle) || this.estFinAnnotation(motsCle);
	}

	private boolean estMillieuAnnotation(String[] motsCle) {
		return !this.estDebutOuFinAnnotation(motsCle);
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
		ligne.trim();
		CodeVariant codeVariant = new CodeVariant();
		Proposition proposition = this.creerProposition(ligne);
		Annotation annotationCourante = new AnnotationComposer(nomDeFichier, indice, NB_LIGNE_DEBUT,
				nbCharLigneCourante, degre, codeVariant, proposition);
		return annotationCourante;
	}

	private Proposition creerProposition(String ligne) {
		Proposition proposition = new Proposition();
		proposition.parserFormule(ligne);
		return proposition;
	}

	private List<Annotation> simplifierAnnotations(List<Annotation> lstAnnotations) {
		List<Annotation> lst = new ArrayList<Annotation>();
		Annotation annotationComposer = null;
		for (int i = 0; i < lstAnnotations.size(); i++) {
			annotationComposer = lstAnnotations.get(i);
			Annotation annotationSimplifier = this.simplifierUneAnnotation(annotationComposer);
			lst.add(annotationSimplifier);
		}
		return lst;
	}

	private Annotation simplifierUneAnnotation(Annotation annotationComposer) {
		Annotation annotationRetourner = null;
		List<Annotation> annotationsEnfantCourant = annotationComposer.getAnnotationsEnfant();
		if (annotationsEnfantCourant.isEmpty()) {
			annotationRetourner = this.creerAnnotationSimple(annotationComposer);
		} else {
			List<Annotation> annotationsEnfantSimplifier = this.simplifierAnnotations(annotationsEnfantCourant);
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

	/****************************
	 * Annotation synthese
	 ***************************************/
	private void ecrireFichierSynthese(List<Annotation> annotations, String cheminFichierSortieSynthese) {
		List<AnnotationSynthese> annotationsSyntheses;
		List<Annotation> annotationsLinearisees = this.lineariserAnnotations(annotations);
		Set<AnnotationGroupe> annotationsGroupes = this.creerAnnotationsGroupes(annotations);
		annotationsSyntheses = this.creerAnnotationsSyntheses(annotationsGroupes);
		MetricsVital metricsVital = new MetricsVital();
		metricsVital.calculerMetrics(annotationsSyntheses, annotationsGroupes, annotationsLinearisees, nbFichierParser);
		long score = this.calculerScore(annotationsLinearisees);
		AnnotationsSynthesesWrapper annotationsSynthesesWrapper = new AnnotationsSynthesesWrapper(annotationsSyntheses,
				metricsVital, score);
		jaxbObjectToXML(annotationsSynthesesWrapper, cheminFichierSortieSynthese);
	}

	private List<AnnotationSynthese> creerAnnotationsSyntheses(Set<AnnotationGroupe> annotationsGroupes) {
		List<AnnotationSynthese> annotationsSyntheses = new ArrayList<AnnotationSynthese>();
		for (AnnotationGroupe annotationGroupe : annotationsGroupes) {
			AnnotationSynthese resultat = this.creerAnnotationSynthese(annotationGroupe);
			annotationsSyntheses.add(resultat);
		}
		return annotationsSyntheses;
	}

	private AnnotationSynthese creerAnnotationSynthese(AnnotationGroupe annotationGroupe) {
		AnnotationSynthese annotationSyntheseResultat = new AnnotationSynthese();
		final Proposition proposition = annotationGroupe.getProposition();
		annotationSyntheseResultat.setProposition(proposition);

		int nbAnnotationSimple = 0;
		int nbAnnotationComp = 0;

		List<Annotation> annotationsDuGroupe = annotationGroupe.getAnnotations();
		for (Annotation annotation : annotationsDuGroupe) {
			annotationSyntheseResultat.ajouterNombresDeAnnotation(annotation);
			if (annotation.estComposer()) {
				nbAnnotationComp++;
			} else {
				nbAnnotationSimple++;
			}
		}

		annotationSyntheseResultat.setNombreAnnotationSimple(nbAnnotationSimple);
		annotationSyntheseResultat.setNombreAnnotationComposer(nbAnnotationComp);

		annotationSyntheseResultat.calculerMoyenneNombreLigne();
		annotationSyntheseResultat.calculerMoyenneNombreCaractere();
		annotationSyntheseResultat.calculerMoyenneDegree();

		return annotationSyntheseResultat;
	}

	private Set<AnnotationGroupe> creerAnnotationsGroupes(List<Annotation> annotations) {
		Set<AnnotationGroupe> annotationsGroupes = new HashSet<>();
		AnnotationGroupe annotationGroupe;

		for (Annotation annotation : annotations) {
			Proposition proposition = annotation.getProposition();
			annotationGroupe = new AnnotationGroupe(proposition);
			annotationsGroupes.add(annotationGroupe);
		}
		for (Annotation annotation : annotations) {
			for (AnnotationGroupe annotationGroupe2 : annotationsGroupes) {
				annotationGroupe2.ajouterAnnotation(annotation);
			}
		}
		return annotationsGroupes;
	}

	private List<Annotation> lineariserAnnotations(List<Annotation> annotations) {
		List<Annotation> annotationsLineaireResultat = new ArrayList<Annotation>();
		List<Annotation> annotationsEnfantCourant = new ArrayList<Annotation>();
		List<Annotation> annotationsLineraireResultatEnfant = new ArrayList<Annotation>();
		for (Annotation annotation : annotations) {
			if (annotation.estComposer()) {
				annotationsEnfantCourant = annotation.getAnnotationsEnfant();
				annotationsLineraireResultatEnfant = this.lineariserAnnotations(annotationsEnfantCourant);
				annotationsLineaireResultat.add(annotation);
				annotationsLineaireResultat.addAll(annotationsLineraireResultatEnfant);
			} else {
				annotationsLineaireResultat.add(annotation);
			}
		}
		return annotationsLineaireResultat;
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

	public void afficherSynthese(List<AnnotationSynthese> annotationSyntheses) {
		for (AnnotationSynthese annotationSynthese : annotationSyntheses) {
			System.out.println(annotationSynthese);
		}
	}

	/****************************
	 * implication des annotations
	 ***************************************/

	private void ecrireFichierImplication(List<Annotation> annotations, String cheminFichierSortieImplication) {
		Set<String> implications = this.genererImplications(annotations, new ArrayList<Predicat>());
		this.creerFichierImplication(implications, cheminFichierSortieImplication);
	}

	private Set<String> genererImplications(List<Annotation> annotations, List<Predicat> predicatsAnnotationMere) {
		Set<String> resultats = new HashSet<>();
		Set<String> resultatsEnfant = new HashSet<>();
		List<Predicat> predicatsAnnotationCourant;

		String implication;
		for (Annotation annotation : annotations) {
			predicatsAnnotationCourant = annotation.getProposition().getPredicats();
			for (Predicat predicatAnnotation : predicatsAnnotationCourant) {
				for (Predicat predicatAnnotationMere : predicatsAnnotationMere) {
					implication = predicatAnnotation.creerImplicationAvec(predicatAnnotationMere);
					resultats.add(implication);
				}
			}
			if (annotation.estComposer()) {
				List<Annotation> annotationsEnfant = annotation.getAnnotationsEnfant();
				resultatsEnfant = this.genererImplications(annotationsEnfant, predicatsAnnotationCourant);
				resultats.addAll(resultatsEnfant);
			}
		}
		return resultats;
	}

	private void creerFichierImplication(Set<String> implications, String fichierSortieImplication) {
		try {
			FileWriter fw = new FileWriter(fichierSortieImplication, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			for (String implication : implications) {
				out.println(implication);
			}
			out.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Exception ecriture fichier");
			e.printStackTrace();
		}
	}

	/****************************
	 * fichier annotations lite
	 ***************************************/

	private void ecrireFichierAnnotationsLite(List<Annotation> annotations, String fichierSortieAnnotationLite) {
		int nbIndentation = 0;
		String nomFichierComparer = "";
		List<String> listeResultat = this.creerListeAnnotationsLite(annotations, nbIndentation, nomFichierComparer);

		try {
			FileWriter fw = new FileWriter(fichierSortieAnnotationLite, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			for (String elt : listeResultat) {
				out.println(elt);
			}
			out.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("Exception ecriture fichier");
			e.printStackTrace();
		}
	}

	private List<String> creerListeAnnotationsLite(List<Annotation> annotations, int nbIndentation, String nomFichier) {
		List<String> listeAnnotationsLite = new ArrayList<>();
		List<Annotation> annotationsEnfantCourant = new ArrayList<>();
		List<String> AnnotationsLiteEnfant = new ArrayList<>();
		String indentation = this.faireIndentationTabulation(nbIndentation);
		String nomDeFichierCourant = "";
		nbIndentation++;
		for (Annotation annotation : annotations) {
			nomDeFichierCourant = annotation.getNomDeFichier();
			if (!this.estMemeNomDeFichier(nomFichier, nomDeFichierCourant)) {
				listeAnnotationsLite.add("FICHIER : " + nomDeFichierCourant);
				nomFichier = nomDeFichierCourant;
			}
			String ligneIndenter = indentation + annotation.getProposition().getFormule() + "\t"
					+ annotation.getNombreDeLigne();
			if (annotation.estComposer()) {
				listeAnnotationsLite.add(ligneIndenter);
				annotationsEnfantCourant = annotation.getAnnotationsEnfant();
				AnnotationsLiteEnfant = this.creerListeAnnotationsLite(annotationsEnfantCourant, nbIndentation,
						nomFichier);
				listeAnnotationsLite.addAll(AnnotationsLiteEnfant);
			} else {
				listeAnnotationsLite.add(ligneIndenter);
			}
		}
		return listeAnnotationsLite;
	}

	private String faireIndentationTabulation(int nbIndentation) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < nbIndentation; i++) {
			str.append("\t");
		}
		str.append("|--");
		return str.toString();
	}

	private boolean estMemeNomDeFichier(String fichier1, String fichier2) {
		return fichier1.equals(fichier2);
	}

	/************************************************
	 *** calcule du score
	 ************************************************/
	public long calculerScore(List<Annotation> annotationsLinearisee) {
		long score = 0;
		for (Annotation annotation : annotationsLinearisee) {
			score += annotation.getDegre() * annotation.getNombreDeLigne();
		}
		return score;
	}

	/****************************
	 * Metrics additionnel
	 ***************************************/

	private void ecrireFichierMetricsAdditionnel(List<Annotation> annotations,
			String cheminFichierSortieMetricsAdditionnel, String cheminFichierImage) {

		List<Annotation> annotationsRedondanteEliminable = new ArrayList<>();
		List<Annotation> annotationsRedondanteSimplifiable = new ArrayList<>();
		this.identifierAnnotationsRedondantes(annotations, annotationsRedondanteSimplifiable,
				annotationsRedondanteEliminable);

		MetricsAdditionnel metricsAdditionnel = new MetricsAdditionnel();
		double proprotionAnnotationsSimplifiable = metricsAdditionnel
				.calculerProportionAnnotationSimplifiable(annotations, annotationsRedondanteSimplifiable);
		proprotionAnnotationsSimplifiable = Math.round(proprotionAnnotationsSimplifiable * 100.0) / 100.0;

		double proprotionAnnotationsEliminable = metricsAdditionnel.calculerProportionAnnotationEliminable(annotations,
				annotationsRedondanteEliminable);
		proprotionAnnotationsEliminable = Math.round(proprotionAnnotationsEliminable * 100.0) / 100.0;

		List<String> lignesSansVides = obtenirCodeSansLignesVides();
		List<Annotation> annotationsSansVides = new ArrayList<>();
		creerArborescenceDesAnnotations("", lignesSansVides, annotationsSansVides, new Stack<>(), null, 0, 0);
		Long nbAnnotationsConcatenable = metricsAdditionnel.compterNombreAnnotationConcatenable(annotationsSansVides);

		List<Annotation> annotationsLineariser = this.lineariserAnnotations(annotations);
		Matrice matriceSimilariteCode = metricsAdditionnel.getMatriceSimilariteCode(annotationsLineariser);

		try {
			matriceSimilariteCode.creerImageMatrice(cheminFichierImage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MetricsAdditionnelWrapper metricsAdditionnelWrapper = new MetricsAdditionnelWrapper(
				annotationsRedondanteSimplifiable, annotationsRedondanteEliminable, proprotionAnnotationsSimplifiable,
				proprotionAnnotationsEliminable, nbAnnotationsConcatenable);
		jaxbObjectToXML(metricsAdditionnelWrapper, cheminFichierSortieMetricsAdditionnel);
	}

	private void identifierAnnotationsRedondantes(List<Annotation> annotations,
			List<Annotation> annotationsRedondanteSimplifiable, List<Annotation> annotationsRedondanteEliminable) {
		for (Annotation annotation : annotations) {
			if (annotation.estComposer()) {
				if (formuleConjonctiveEtPredicatEgaux(annotation.getProposition().getFormule(),
						annotation.getProposition().getPredicats())) {
					for (Annotation annotationEnfant : annotation.getAnnotationsEnfant()) {
						if (annotation.getProposition().getFormule()
								.equals(annotationEnfant.getProposition().getFormule())) {
							annotationsRedondanteEliminable.add(annotationEnfant);
						} else {
							for (Predicat predicat : annotationEnfant.getProposition().getPredicats()) {
								if (annotation.getProposition().getPredicats().contains(predicat)) {
									annotationsRedondanteSimplifiable.add(annotationEnfant);
								}
							}

						}
					}
				}
			}
		}
	}

	public boolean formuleConjonctiveEtPredicatEgaux(String formule, List<Predicat> predicats) {
		String[] tabPredicat = formule.split("&&");
		if (tabPredicat.length != predicats.size()) {
			return false;
		} else {
			for (int i = 0; i < predicats.size(); i++) {
				if (!(tabPredicat[i].equals(predicats.get(i).getNom()))) {
					return false;
				}
			}
		}
		return true;
	}

	public List<String> obtenirCodeSansLignesVides() {
		List<String> ligneSansVides = new ArrayList<>();
		for (String ligne : lignesTotalsFichier) {
			if (!ligne.trim().isEmpty()) {
				ligneSansVides.add(ligne);
			}
		}
		return ligneSansVides;
	}

	private static void jaxbObjectToXML(MetricsAdditionnelWrapper metricsAdditionnelWrapper, String outputFileName) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MetricsAdditionnelWrapper.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(metricsAdditionnelWrapper, new File(outputFileName));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}