package package1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationsSynthesesWrapper {

	@XmlElement(name = "synthese")
	private List<AnnotationSynthese> annotationsSyntheses = null;
	@XmlElement(name = "metrics")
	private MetricsVital metricsVital = null;

	public AnnotationsSynthesesWrapper() {
		super();
		this.annotationsSyntheses = new ArrayList<AnnotationSynthese>();
	}

	public AnnotationsSynthesesWrapper(List<AnnotationSynthese> annotationsSyntheses) {
		super();
		this.annotationsSyntheses = annotationsSyntheses;
	}

	public AnnotationsSynthesesWrapper(List<AnnotationSynthese> annotationsSyntheses, MetricsVital metricsVital) {
		super();
		this.annotationsSyntheses = annotationsSyntheses;
		this.metricsVital = metricsVital;
	}

}