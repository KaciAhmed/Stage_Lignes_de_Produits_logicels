package package1;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "annotations")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationsWrapper {
	@XmlElement(name = "annotation")
	private List<Annotation> annotations = null;

	public AnnotationsWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnnotationsWrapper(List<Annotation> annotations) {
		super();
		this.annotations = annotations;
	}

	public List<Annotation> getAnnotations() {
		return this.annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}
}
