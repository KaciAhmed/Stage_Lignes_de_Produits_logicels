package package1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
		String outputFile = "";
		List<Annotation> annotations;

		if (args.length > 0) {
			inputDirectory = args[0];
			if (args.length > 1) {
				outputFile = args[1];
			}else {
				outputFile += "out" + File.separator + "output_";
			}
		} else {
			System.out.println("Usage <path/to/code/> <optional/path/to/result.xml>");
		}
		
		outputFile += Instant.now().getEpochSecond() + ".xml";
		
		Parser parseur = new Parser();
		annotations = parseur.parser(inputDirectory);
		AnnotationsWrapper annotationsWrapper = new AnnotationsWrapper(annotations);
		annotationsWrapper.setNomFichierInput(inputDirectory);
		jaxbObjectToXML(annotationsWrapper, outputFile);
	}

	public List<Annotation> parser(String inputDirectory) {
		File dossier = new File(inputDirectory);
		List<Annotation> annotationFinale = lireDossier(dossier);
		return annotationFinale;
	}

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

	private void concatenerListes(List<Annotation> lst1, List<Annotation> lst2) {
		for (Annotation annotation : lst2) {
			lst1.add(annotation);
		}
	}

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
		Annotation temp;
		
		for (int i = 0; i < lstAnnotations.size(); i++) {
			annotationComposer = lstAnnotations.get(i);
			Annotation annotationSimplifier = simplifierAnnotation(annotationComposer);
			lst.add(annotationSimplifier);
		}
		return lst;
	}

	private Annotation simplifierAnnotation(Annotation annotationComposer) {
		Annotation simplification = null;
		List<Annotation> annotationsEnfantCourant = annotationComposer.getAnnotationsEnfant();
		if (annotationsEnfantCourant.isEmpty()) {
			simplification = creerAnnotationSimple(annotationComposer);
		} else {
			List<Annotation> annotationsEnfantSimplifier = simplifierAnnotations(annotationsEnfantCourant);
			annotationComposer.setAnnotationsEnfant(annotationsEnfantSimplifier);
		}
		return simplification;
	}

	private AnnotationSimple creerAnnotationSimple(Annotation annotationComposer) {
		return new AnnotationSimple(annotationComposer.getNomDeFichier(),
				annotationComposer.getDebutDeLigne(), annotationComposer.getNombreDeLigne(),
				annotationComposer.getNombreDeCaractere(), annotationComposer.getDegre(),
				annotationComposer.getCodeVariant(), annotationComposer.getProposition());
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
}
