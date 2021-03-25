package package1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationsWrapper {
	@SuppressWarnings("unused")
	private String input = "";

	@XmlElement(name = "annotations")
	private List<Annotation> annotations = null;

	public AnnotationsWrapper() {
		super();
		this.annotations = new ArrayList<Annotation>();
	}

	public AnnotationsWrapper(List<Annotation> annotations) {
		super();
		this.annotations = annotations;
	}

	public void setNomFichierInput(String input) {
		this.input = input;
	}
}
