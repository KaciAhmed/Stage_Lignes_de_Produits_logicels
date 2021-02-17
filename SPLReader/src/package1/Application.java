package package1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Application {


	public static String classpath = System.getProperty("java.class.path");
	static String inputFile = Annotation.STRING_VIDE;
	static String outputFile = classpath + File.separator + "out/output";
	public static List<String> contenuFichier = new ArrayList<String>();
	public static List<Annotation> annotations = new ArrayList<Annotation>();
	public static Annotation annotationCourante = new Annotation();
	public static Stack<Annotation> pileAnnotation = new Stack<Annotation>();

	public static void main(String[] args) {
		if (args.length > 0) {
			inputFile = args[0];
			if (args.length > 1) {
				outputFile = args[1];
			}
		} else {
			System.out.println("Usage <path/to/code.java> <optional/path/to/result.xml>");
		}

		readFile();
		creerArborescenceDesAnnotations(contenuFichier, 0, 0, 0);
		affichageAnnotations();
	}
	
	public static void annotationCouranteDePileIncrementNbLigneAvecNbChar(int nbCharLigneCourante) {
		annotationCourante = pileAnnotation.pop();
		annotationCourante.incrementNbLigne();
		setupAnnotationAvecIncrementNbChar(nbCharLigneCourante);
		pileAnnotation.push(annotationCourante);
	}

	public static void creerArborescenceDesAnnotations(List<String> codeParserParLigne, int degre,
			int indiceCurseurDeLigne, int nbLigneTotal) {
		if (toutEstVisite(codeParserParLigne, indiceCurseurDeLigne)) {
			System.out.println("Tout est visite");
		} else {
			String ligneCourante = codeParserParLigne.get(indiceCurseurDeLigne);
			ligneCourante = ligneCourante.replaceAll(Annotation.REGEX_TAB, Annotation.STRING_VIDE);
			int nbCharLigneCourante = ligneCourante.length();
			String[] motsCles = ligneCourante.trim().split(Annotation.ESPACE);
			if (estDebutAnnotation(motsCles)) {
				miseAjourPileAnnotation(nbCharLigneCourante);
				String predicat = ligneCourante.substring(motsCles[0].trim().length()).trim();
				nbLigneTotal++;
				degre++;
				setupAnnotationCouranteAvec(degre, indiceCurseurDeLigne, nbCharLigneCourante, predicat);
				pileAnnotation.push(annotationCourante);
				indiceCurseurDeLigne++;
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			} else if (estFinAnnotation(motsCles)) {
				annotationCourante = pileAnnotation.pop();
				annotationCourante.incrementNbLigne();
				setupAnnotationAvecIncrementNbChar(nbCharLigneCourante);
				annotations.add(annotationCourante);
				degre--;
				indiceCurseurDeLigne++;
				nbLigneTotal++;
				miseAjourPileAnnotation(nbCharLigneCourante);
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			} else if (estMillieuAnnotation(motsCles)) {
				miseAjourPileAnnotation(nbCharLigneCourante);
				indiceCurseurDeLigne++;
				nbLigneTotal++;
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			}
		}
	}

	public static boolean estDebutOuFinAnnotation(String[] motsCle) {
		return estDebutAnnotation(motsCle) || estFinAnnotation(motsCle);
	}

	public static boolean estMillieuAnnotation(String[] motsCle) {
		return !estDebutOuFinAnnotation(motsCle);
	}

	public static void miseAjourPileAnnotation(int nbCharLigneCourante) {
		for (Annotation annotation : pileAnnotation) {
			annotation.incrementNbLigne();
			annotation.incrementNbChar(nbCharLigneCourante);
		}
	}

	public static void readFile() {
		try {
			FileReader fileReader = new FileReader(inputFile);
			InputStreamReader iReader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
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
	}

	public static void setupAnnotationAvecDebutLigne(int indice) {
		annotationCourante.setDebutDeLigne(indice + 1);
	}

	public static void setupAnnotationAvecDegree(int degree) {
		annotationCourante.setDegre(degree);
	}

	public static void setupAnnotationAvecIncrementNbChar(int nbCharLigneCourante) {
		annotationCourante.incrementNbChar(nbCharLigneCourante);
	}

	public static void setupAnnotationAvecNomFichier() {
		annotationCourante.setFichier(inputFile);
	}

	public static void setupAnnotationAvecPredicat(String predicat) {
		annotationCourante.setPredicat(predicat);
	}

	public static void setupAnnotationCouranteAvec(int degree, int indice, int nbCharLigneCourante, String predicat) {
		annotationCourante = new Annotation();
		setupAnnotationAvecDegree(degree);
		setupAnnotationAvecNomFichier();
		setupAnnotationAvecDebutLigne(indice);
		setupAnnotationAvecPredicat(predicat);
		setupAnnotationAvecIncrementNbChar(nbCharLigneCourante);
		setupAnnotationCouranteVariablesAvecRegex("<|>|<=|>=|==|&&");
	}

	public static void setupAnnotationCouranteVariablesAvecRegex(String regex) {
		String[] vars = annotationCourante.getPredicat().split(regex);
		List<String> lstVar = new ArrayList<>();
		for (int i = 0; i < vars.length; i++) {
			lstVar.add(vars[i].trim());
		}
		annotationCourante.setVariables(lstVar);
	}

	public static boolean toutEstVisite(List<String> lstContenu, int indice) {
		return indice == lstContenu.size();
	}

	private static void affichageAnnotations() {
		List<Annotation> annotationParcourue = new ArrayList<Annotation>();
		for (Annotation annotationItt : annotations) {
			boolean present = false;
			for (Annotation annotationItt2 : annotationParcourue) {
				if(estAnnotationEgal(annotationItt, annotationItt2)) {
					present = true;
					annotationItt2.incrementNbChar(annotationItt.getNbChar());
					annotationItt2.incrementNbLigne(annotationItt.getNbLine());
				}
			}
			if(false == present) {
				annotationParcourue.add(Annotation.makeAnnotation(annotationItt));
			}
		}
		for (Annotation annotation : annotationParcourue) {
			System.out.println(annotation);
		}
	}

	public static boolean estAnnotationEgal(Annotation annotation1, Annotation annotation2) {
		return annotation1.equals(annotation2);
	}

	private static boolean estDebutAnnotation(String[] motsCles) {
		return motsCles[0].contains(Annotation.DEBUT_ANNOTATION);
	}

	private static boolean estFinAnnotation(String[] motsCles) {
		return motsCles[0].contains(Annotation.FIN_ANNOTATION);
	}
}