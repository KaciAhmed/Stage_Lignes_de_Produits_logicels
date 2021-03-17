package package1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "annotationsSyntheses" })
public class AnnotationsSynthesesWrapper {

	@XmlElement(name = "synthese")
	private List<AnnotationSynthese> annotationsSyntheses = null;

	public AnnotationsSynthesesWrapper() {
		super();
		this.annotationsSyntheses = new ArrayList<AnnotationSynthese>();
	}

	public AnnotationsSynthesesWrapper(List<AnnotationSynthese> annotationsSyntheses) {
		super();
		this.annotationsSyntheses = annotationsSyntheses;
	}

}
