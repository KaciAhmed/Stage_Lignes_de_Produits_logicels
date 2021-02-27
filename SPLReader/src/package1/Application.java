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
	static String inputDirectory =Annotation.STRING_VIDE;
	static String inputFile = Annotation.STRING_VIDE;
	static String outputFile = classpath + File.separator + "out/output";
	public static List<String> contenuFichier;
	public static List<Annotation> annotations = new ArrayList<Annotation>();
	public static Annotation annotationCourante = new Annotation();
	public static Stack<Annotation> pileAnnotation = new Stack<Annotation>();
	
	public static void main(String[] args) {
		if (args.length > 0) {
			//inputFile = args[0];
			inputDirectory=args[0];
			if (args.length > 1) {
				outputFile = args[1];
			}
		} else {
			System.out.println("Usage <path/to/code/> <optional/path/to/result.xml>");
		}
		// read directory
		File folder = new File(inputDirectory);
		findAllFilesInFolder(folder);
	}

	public static void creerArborescenceDesAnnotations(List<String> codeParserParLigne, int degre,
			int indiceCurseurDeLigne, int nbLigneTotal) {
		if (estCurseurFin(codeParserParLigne, indiceCurseurDeLigne)) {
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
				indiceCurseurDeLigne++;
				setupAnnotationCouranteAvec(degre, indiceCurseurDeLigne, nbCharLigneCourante, predicat);
				pileAnnotation.push(annotationCourante);
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			} else if (estFinAnnotation(motsCles)) {
				miseAjourPileAnnotation(nbCharLigneCourante);
				annotationCourante = pileAnnotation.pop();
				annotations.add(annotationCourante);
				degre--;
				indiceCurseurDeLigne++;
				nbLigneTotal++;
				
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			} else if (estMillieuAnnotation(motsCles)) {
				miseAjourPileAnnotation(nbCharLigneCourante);
				indiceCurseurDeLigne++;
				nbLigneTotal++;
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			}
		}
	}

	public static boolean estCurseurFin(List<String> lstContenu, int indice) {
		return indice == lstContenu.size();
	}
	
	private static boolean estDebutAnnotation(String[] motsCles) {
		return motsCles[0].contains(Annotation.DEBUT_ANNOTATION);
	}

	private static boolean estFinAnnotation(String[] motsCles) {
		return motsCles[0].contains(Annotation.FIN_ANNOTATION);
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
			contenuFichier = new ArrayList<String>();
			while ((line = bufferedReader.readLine()) != null) {
				contenuFichier.add(line);
			}
			iReader.close();
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void setupAnnotationCouranteAvec(int degree, int indice, int nbCharLigneCourante, String predicat) {
		annotationCourante = new Annotation();
		annotationCourante.setDegre(degree);
		annotationCourante.setFichier(inputFile);
		annotationCourante.setDebutDeLigne(indice);
		annotationCourante.setPredicat(predicat);
		annotationCourante.incrementNbChar(nbCharLigneCourante);
		annotationCourante.setNbLine(1);
		setupAnnotationCouranteVariablesAvecRegex("<|>|<=|>=|==|&&");
	}
	/*
	 * initialiser la liste des variables de l'annotation à partir de son prédicat.
	 */
	public static void setupAnnotationCouranteVariablesAvecRegex(String regex) {
		String[] vars = annotationCourante.getPredicat().split(regex);
		List<String> lstVar = new ArrayList<>();
		for (int i = 0; i < vars.length; i++) {
			lstVar.add(vars[i].trim());
		}
		annotationCourante.setVariables(lstVar);
	}

	private static void affichageAnnotations(List<Annotation>lstAnnotations) {
		for (Annotation annotation : lstAnnotations) {
			System.out.println(annotation);
		}
	}
/*******************************Lecture dossier***********************************************/
	public static void findAllFilesInFolder(File folder) {
		for (File file : folder.listFiles()) {
			if (!file.isDirectory()) {
				if(file.getName().substring(file.getName().length()-5, file.getName().length()).equals(".java")) {
					System.out.println("-------------------------"+file.getName()+"-----------------------------");
					annotations = new ArrayList<Annotation>();
					annotationCourante = new Annotation();
					pileAnnotation = new Stack<Annotation>();
					inputFile=file.getAbsolutePath();
					readFile();
					creerArborescenceDesAnnotations(contenuFichier, 0, 0, 0);
					affichageAnnotations(annotations);
				}	
			} else {
				findAllFilesInFolder(file);
			}
		}
	}
	
}