package package1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "proposition")
@XmlAccessorType(XmlAccessType.FIELD)
public class Proposition {
	private String formule;
	@XmlElementWrapper(name = "predicats")
	@XmlElement(name = "predicat")
	private List<Predicat> predicats = null;

	public Proposition() {
		super();
		this.predicats = new ArrayList<>();
	}

	public String getFormule() {
		return this.formule;
	}

	public void setFormule(String formule) {
		this.formule = formule;
	}

	public List<Predicat> getPredicats() {
		return this.predicats;
	}

	public void setPredicats(List<Predicat> predicats) {
		this.predicats = predicats;
	}

	public void parserFormule(String ligne) {
		final String patternIF = "#if";
		final String regexSeparateur = "<|>|<=|>=|==|&&|\\|\\|";
		int positionIF = ligne.indexOf(patternIF);
		positionIF += patternIF.length();
		String contenuFormule = ligne.substring(positionIF);
		this.setFormule(contenuFormule.trim());
		String[] tabPredicats = this.formule.split(regexSeparateur);
		Predicat predicat;
		for (int i = 0; i < tabPredicats.length; i++) {
			predicat = new Predicat(tabPredicats[i].trim());
			this.predicats.add(predicat);
		}
	}

	@Override
	public String toString() {
		return "Proposition [formule= " + this.formule + ", predicats= " + this.predicats + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.formule, this.predicats);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Proposition other = (Proposition) obj;
		if (this.formule == null) {
			if (other.formule != null) {
				return false;
			}
		} else if (!this.formule.equals(other.formule)) {
			return false;
		}
		if (this.predicats == null) {
			if (other.predicats != null) {
				return false;
			}
		} else if (!this.predicats.equals(other.predicats)) {
			return false;
		}
		return true;
	}

}
