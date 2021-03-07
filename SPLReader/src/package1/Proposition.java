package package1;

import java.util.ArrayList;
import java.util.List;

public class Proposition {
	private String formule;
	private List<Predicat> predicats;

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
		String patternIF = "#if";
		String regexSeparateur = "<|>|<=|>=|==|&&|\\|\\|";
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
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.formule == null) ? 0 : this.formule.hashCode());
		result = (prime * result) + ((this.predicats == null) ? 0 : this.predicats.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
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
