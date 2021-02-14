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
	public static Stack<Annotation> pileAnnotation = new Stack();

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
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
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
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void creerArborescenceDesAnnotations(List<String> lstContenu, int degree, int indice,int nbLigneTotal){
		if(toutEstVisite(lstContenu, indice)) {
			System.out.println("Tout est visite");
		}else {
			String[] motsCles = lstContenu.get(indice).trim().split(Annotation.ESPACE);
			if(motsCles[0].contains(Annotation.DEBUT_ANNOTATION)){
				annotationCourante = new Annotation();
				nbLigneTotal++;
				degree++;
				annotationCourante.setDegre(degree);
				annotationCourante.setFichier(inputFile);
				annotationCourante.setStartLine(indice);
				annotationCourante.setPredicat(lstContenu.get(indice).substring(motsCles[0].length()));
				annotationCourante.incrementNbChar(lstContenu.get(indice).length());
				String[] vars = annotationCourante.getPredicat().split("<|>|<=|>=|==|&&");
				List<String> lstVar= new ArrayList<>();
				for(int i = 0; i < vars.length; i++) {
					lstVar.add(vars[i].trim());
				}
				annotationCourante.setVariables(lstVar);
				
				pileAnnotation.push(annotationCourante);
				creerArborescenceDesAnnotations(lstContenu, degree, ++indice, nbLigneTotal);
			}else if (motsCles[0].contains(Annotation.FIN_ANNOTATION)){
				degree--;
				annotationCourante = pileAnnotation.pop();
				annotationCourante.incrementNbLine();
				annotationCourante.incrementNbChar(lstContenu.get(indice).length());
				annotations.add(annotationCourante);
				creerArborescenceDesAnnotations(lstContenu, degree, ++indice, ++nbLigneTotal);
			}else if(estMillieuAnnotation(motsCles[0])){
				if(!pileAnnotation.empty()) {
					annotationCourante = pileAnnotation.pop();
					annotationCourante.incrementNbLine();
					annotationCourante.incrementNbChar(lstContenu.get(indice).length());
					pileAnnotation.push(annotationCourante);
				creerArborescenceDesAnnotations(lstContenu, degree, ++indice, ++nbLigneTotal);	
				}else{	
					creerArborescenceDesAnnotations(lstContenu, degree, ++indice, ++nbLigneTotal);
				}
			}
		}
	}
	
	public static boolean toutEstVisite(List<String> lstContenu, int indice){
		return indice == lstContenu.size();
	}
	
	public static boolean estDebutOuFinAnnotation(String motsCle){
		return motsCle.contains(Annotation.DEBUT_ANNOTATION) || motsCle.contains(Annotation.FIN_ANNOTATION);
	}
	
	public static boolean estMillieuAnnotation(String motsCle){
		return !estDebutOuFinAnnotation(motsCle);
	}
}