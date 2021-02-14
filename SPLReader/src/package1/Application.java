package package1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Application {
	
	public static List <Annotation> annotations = new ArrayList<Annotation>();
	public static String classpath = System.getProperty("java.class.path");
	static String inputFile = "";
	static String outputFile = classpath + File.separator + "out/output";
	

	public static void main(String[] args) {

		File resultat = new File("test");
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
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
		}
	}

	public static void readFile() {

		final String ESPACE = " ";
		final String DEBUT_ANNOTATION = "//#if";
		final String FIN_ANNOTATION = "//#endif";
		int cptLigne = 1;
		int cptLigneLocal = 0;
		int degreCourant = 0;
		int cptCharLocal = 0;
		

		Annotation annotationCourante = new Annotation();

		try {
			FileReader fileReader = new FileReader(inputFile);
			InputStreamReader iReader = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(iReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
	            String[] motsCles = line.trim().split(ESPACE);

	            if(motsCles[0].contains(DEBUT_ANNOTATION)){	
					annotationCourante = new Annotation(); // diverge cas lastAnnotationCourante terminer ?
					annotationCourante.setDegre(degreCourant);
	            	degreCourant++;
					annotationCourante.setFichier(inputFile);
					annotationCourante.setStartLine(cptLigne);
					annotationCourante.setPredicat(line.substring(motsCles[0].length()));
					cptCharLocal+=line.length();
					String[] vars = annotationCourante.getPredicat().split("<|>|<=|>=|==|&&");
					List<String> lstVar= new ArrayList<>();

					for(int i = 0; i < vars.length; i++) {
						lstVar.add(vars[i].trim());
					}
					annotationCourante.setVariables(lstVar);
					annotations.add(annotationCourante);
				}
				else if (motsCles[0].contains(FIN_ANNOTATION)){
					annotationCourante.setNbLine(cptLigneLocal-1);
					cptLigneLocal = 0;
					degreCourant--;
					annotationCourante.setNbChar(cptCharLocal);
					cptCharLocal = 0;
					annotations.add(annotationCourante);
				}
				cptLigne++;
	            cptLigneLocal++;
				
	          
	        }
			iReader.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}