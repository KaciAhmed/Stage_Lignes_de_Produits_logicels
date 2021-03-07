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

public class Parser {
	public static final String REGEX_TAB = "\\t";
	public static final String STRING_VIDE = "";
	public static final String ESPACE = " ";

	public static void main(String[] args) {
		String inputDirectory = STRING_VIDE;
		String classpath = System.getProperty("java.class.path");
		String outputFile = classpath + File.separator + "output" + Instant.now().getEpochSecond() + ".xml";
		if (args.length > 0) {
			inputDirectory = args[0];
			if (args.length > 1) {
				outputFile = args[1];
			}
		} else {
			System.out.println("Usage <path/to/code/> <optional/path/to/result.xml>");
		}
		Parser parseur = new Parser();
		parseur.parser(inputDirectory);
	}

	public List<String> lireFichier(String nomDeFichier) {
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

	public void lireDossier(File dossier) {
		String nomDeFichier;
		List<Annotation> annotations;
		Annotation annotationCourante;
		Stack<Annotation> pileAnnotation;
		for (File contenuDossier : dossier.listFiles()) {
			if (!contenuDossier.isDirectory()) {
				if (contenuDossier.getName()
						.substring(contenuDossier.getName().length() - 5, contenuDossier.getName().length())
						.equals(".java")) {
					System.out.println(
							"-------------------------" + contenuDossier.getName() + "-----------------------------");
					annotations = new ArrayList<Annotation>();
					pileAnnotation = new Stack<Annotation>();
					nomDeFichier = contenuDossier.getAbsolutePath();
					annotationCourante = null;
					List<String> lignesFichier = lireFichier(nomDeFichier);
					creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
							annotationCourante, 0, 0, 0);
					List<Annotation> annotationsProv = simplifierAnnotations(annotations);
					afficherAnnotations(annotationsProv);

				}
			} else {
				lireDossier(contenuDossier);
			}
		}
	}

	private void creerArborescenceDesAnnotations(String nomDeFichier, List<String> lignesFichier,
			List<Annotation> annotations, Stack<Annotation> pileAnnotation, Annotation annotationCourante, int degre,
			int indiceCurseurDeLigne, int nbLigneTotal) {
		if (estCurseurFin(lignesFichier, indiceCurseurDeLigne)) {
			System.out.println("Tout est visite");
		} else {
			String ligneCourante = lignesFichier.get(indiceCurseurDeLigne);
			ligneCourante = ligneCourante.replaceAll(REGEX_TAB, STRING_VIDE);
			int nbCharLigneCourante = ligneCourante.length();
			String[] motsCles = ligneCourante.trim().split(ESPACE);
			if (estDebutAnnotation(motsCles)) {
				miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				nbLigneTotal++;
				degre++;
				indiceCurseurDeLigne++;
				annotationCourante = setupAnnotation(nomDeFichier, ligneCourante, degre, indiceCurseurDeLigne,
						nbCharLigneCourante);
				pileAnnotation.push(annotationCourante);
				creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne, nbLigneTotal);
			} else if (estFinAnnotation(motsCles)) {
				miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
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
				nbLigneTotal++;
				creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne, nbLigneTotal);
			} else if (estMillieuAnnotation(motsCles)) {
				miseAjourPileAnnotation(pileAnnotation, nbCharLigneCourante);
				indiceCurseurDeLigne++;
				nbLigneTotal++;
				creerArborescenceDesAnnotations(nomDeFichier, lignesFichier, annotations, pileAnnotation,
						annotationCourante, degre, indiceCurseurDeLigne, nbLigneTotal);
			}
		}
	}

	private boolean estCurseurFin(List<String> lstContenu, int indice) {
		return indice == lstContenu.size();
	}

	private boolean estDebutAnnotation(String[] motsCles) {
		String debutAnnotation = "//#if";
		return motsCles[0].contains(debutAnnotation);
	}

	private boolean estFinAnnotation(String[] motsCles) {
		String finAnnotation = "//#endif";
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

	private Annotation setupAnnotation(String nomDeFichier, String ligne, int degre, int indice,
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

	public void afficherAnnotations(List<Annotation> lstAnnotations) {
		for (Annotation annotation : lstAnnotations) {
			annotation.afficherArborescence();
		}
	}

	public List<Annotation> simplifierAnnotations(List<Annotation> lstAnnotations) {
		List<Annotation> lst = new ArrayList<Annotation>();
		Annotation temp;
		for (int i = 0; i < lstAnnotations.size(); i++) {
			if (lstAnnotations.get(i).getAnnotationsEnfant().isEmpty()) {
				temp = new AnnotationSimple(lstAnnotations.get(i).getNomDeFichier(),
						lstAnnotations.get(i).getDebutDeLigne(), lstAnnotations.get(i).getNombreDeLigne(),
						lstAnnotations.get(i).getNombreDeCaractere(), lstAnnotations.get(i).getDegre(),
						lstAnnotations.get(i).getCodeVariant(), lstAnnotations.get(i).getProposition());
				lst.add(temp);
			} else {
				lst.add(lstAnnotations.get(i));
				lstAnnotations.get(i)
						.setAnnotationsEnfant(simplifierAnnotations(lstAnnotations.get(i).getAnnotationsEnfant()));
			}
		}
		return lst;
	}

	public void parser(String inputDirectory) {
		File dossier = new File(inputDirectory);
		lireDossier(dossier);
	}

	public static Proposition creerProposition(String ligne) {
		Proposition proposition = new Proposition();
		proposition.parserFormule(ligne);
		return proposition;
	}

}
