package package1;

import java.util.ArrayList;
import java.util.List;

public class Proposition {
	private String formule;
	private List<Predicat> predicats;
	
	public Proposition() {
		super();
		predicats=new ArrayList<>();
	}
	
	public String getFormule() {
		return formule;
	}

	public void setFormule(String formule) {
		this.formule = formule;
	}

	public List<Predicat> getPredicats() {
		return predicats;
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
		String[] tabPredicats = formule.split(regexSeparateur);
		Predicat predicat;
		for (int i = 0; i < tabPredicats.length; i++) {
			predicat = new Predicat(tabPredicats[i].trim());
			predicats.add(predicat);
		}
	}

	@Override
	public String toString() {
		return "Proposition [formule= " + formule + ", predicats= " + predicats + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formule == null) ? 0 : formule.hashCode());
		result = prime * result + ((predicats == null) ? 0 : predicats.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proposition other = (Proposition) obj;
		if (formule == null) {
			if (other.formule != null)
				return false;
		} else if (!formule.equals(other.formule))
			return false;
		if (predicats == null) {
			if (other.predicats != null)
				return false;
		} else if (!predicats.equals(other.predicats))
			return false;
		return true;
	}	
	
	
}
