package package1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricsAdditionnelWrapper {
	@XmlElement(name = "annotationsSimplifiable")
	private List<Annotation> annotationsSimplifiable = null;
	@XmlElement(name = "annotationsEliminable")
	private List<Annotation> annotationsEliminable = null;
	@XmlElement(name = "proportionAnnotationsSimplifiable")
	private double proportionAnnotationSimplifiable = 0;
	@XmlElement(name = "proportionAnnotationsEliminable")
	private double proprtionAnnotationEliminable = 0;
	@XmlElement(name = "nombreAnnotationsConcatenable")
	private Long nbAnnotationConcatenable;

	public MetricsAdditionnelWrapper() {
		super();
		this.annotationsEliminable = new ArrayList<Annotation>();
		this.annotationsSimplifiable = new ArrayList<Annotation>();
	}

	public MetricsAdditionnelWrapper(List<Annotation> annotationsSimplifiable, List<Annotation> annotationsEliminable,
			double proportionAnnotationSimplifiable, double proprtionAnnotationEliminable,
			Long nbAnnotationConcatenable) {
		super();
		this.annotationsSimplifiable = annotationsSimplifiable;
		this.annotationsEliminable = annotationsEliminable;
		this.proportionAnnotationSimplifiable = proportionAnnotationSimplifiable;
		this.proprtionAnnotationEliminable = proprtionAnnotationEliminable;
		this.nbAnnotationConcatenable = nbAnnotationConcatenable;
	}

}
