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
	static String inputFile = "";
	static String outputFile = classpath + File.separator + "out/output";
	public static List <String> contenuFichier = new ArrayList<String>();
	public static List <Annotation> annotations = new ArrayList<Annotation>();
	public static Annotation annotationCourante = new Annotation();
	public static Stack<Annotation> pileAnnotation = new Stack<Annotation>();

	public static void main(String[] args) {
		if(args.length > 0){
			inputFile = args[0];
			if (args.length > 1){
				outputFile = args[1];
			}
		}
		else {
			System.out.println("Usage <path/to/code.java> <optional/path/to/result.xml>");
		}

		readFile();
	    creerArborescenceDesAnnotations(contenuFichier,0,0,0);
		affichageAnnotations();
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
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void creerArborescenceDesAnnotations(List<String> codeParserParLigne, int degre, int indiceCurseurDeLigne, int nbLigneTotal){
		if(toutEstVisite(codeParserParLigne, indiceCurseurDeLigne)) {
			System.out.println("Tout est visite");
		}else {
			String ligneCourante = codeParserParLigne.get(indiceCurseurDeLigne);
			int nbCharLigneCourante = ligneCourante.length();
			String[] motsCles = ligneCourante.trim().split(Annotation.ESPACE);
			if(estDebutAnnotation(motsCles)){
				String predicat = ligneCourante.substring(motsCles[0].length());
				nbLigneTotal++;
				degre++;
				setupAnnotationCouranteAvec(degre, indiceCurseurDeLigne, nbCharLigneCourante, predicat);
				pileAnnotation.push(annotationCourante);
				indiceCurseurDeLigne++;
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			}else if (estFinAnnotation(motsCles)){
				annotationCourante = pileAnnotation.pop();
				annotationCourante.incrementNbLigne();
				setupAnnotationAvecIncrementNbChar(nbCharLigneCourante);
				annotations.add(annotationCourante);
				degre--;
				indiceCurseurDeLigne++;
				nbLigneTotal++;
				creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
			}else if(estMillieuAnnotation(motsCles)){
				if(!pileAnnotation.empty()) {
					annotationCouranteDePileIncrementNbLigneAvecNbChar(nbCharLigneCourante);
					indiceCurseurDeLigne++;
					nbLigneTotal++;
					creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
				}else{
					indiceCurseurDeLigne++;
					nbLigneTotal++;
					creerArborescenceDesAnnotations(codeParserParLigne, degre, indiceCurseurDeLigne, nbLigneTotal);
				}
			}
		}
	}

	public static void annotationCouranteDePileIncrementNbLigneAvecNbChar(int nbCharLigneCourante) {
		annotationCourante = pileAnnotation.pop();
		annotationCourante.incrementNbLigne();
		setupAnnotationAvecIncrementNbChar(nbCharLigneCourante);
		pileAnnotation.push(annotationCourante);
	}

	public static boolean toutEstVisite(List<String> lstContenu, int indice){
		return indice == lstContenu.size();
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
		List<String> lstVar= new ArrayList<>();
		for(int i = 0; i < vars.length; i++) {
			lstVar.add(vars[i].trim());
		}
		annotationCourante.setVariables(lstVar);
	}

	public static void setupAnnotationAvecIncrementNbChar(int nbCharLigneCourante) {
		annotationCourante.incrementNbChar(nbCharLigneCourante);
	}

	public static void setupAnnotationAvecPredicat(String predicat) {
		annotationCourante.setPredicat(predicat);
	}

	public static void setupAnnotationAvecDebutLigne(int indice) {
		annotationCourante.setStartLigne(indice+1);
	}

	public static void setupAnnotationAvecNomFichier() {
		annotationCourante.setFichier(inputFile);
	}

	public static void setupAnnotationAvecDegree(int degree) {
		annotationCourante.setDegre(degree);
	}

	private static boolean estFinAnnotation(String[] motsCles) {
		return motsCles[0].contains(Annotation.FIN_ANNOTATION);
	}

	private static boolean estDebutAnnotation(String[] motsCles) {
		return motsCles[0].contains(Annotation.DEBUT_ANNOTATION);
	}

	public static boolean estMillieuAnnotation(String[] motsCle){
		return !estDebutOuFinAnnotation(motsCle);
	}

	public static boolean estDebutOuFinAnnotation(String[] motsCle){
		return estDebutAnnotation(motsCle) || estFinAnnotation(motsCle);
	}

	private static void affichageAnnotations() {
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
		}
	}
}