package package1;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "annotations")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "input", "annotations" })
public class AnnotationsWrapper {
	private String input = "";
	
	@XmlElement(name = "annotations")
	private List<Annotation> annotations = null;

	public AnnotationsWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnnotationsWrapper(List<Annotation> annotations) {
		super();
		this.annotations = annotations;
	}
	
	public String getNomFichierInput() {
		return input;
	}

	public void setNomFichierInput(String nomFichierOutput) {
		this.input = nomFichierOutput;
	}

	public List<Annotation> getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
}
