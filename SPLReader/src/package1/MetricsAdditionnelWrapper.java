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
	@XmlElement(name = "Simplifiable")
	private List<Annotation> annotationsSimplifiable = null;
	@XmlElement(name = "Eliminable")
	private List<Annotation> annotationsEliminable = null;
	@XmlElement(name = "proportionSimplifiable")
	private double proportionAnnotationSimplifiable = 0;
	@XmlElement(name = "proportionEliminable")
	private double proprtionAnnotationEliminable = 0;
	@XmlElement(name = "nombreAnnotationsFusionnable")
	private Long nbAnnotationFusionnable;

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
		this.nbAnnotationFusionnable = nbAnnotationConcatenable;
	}

}
