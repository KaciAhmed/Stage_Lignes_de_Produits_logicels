package package1;

import java.util.List;

public class Proposition {
	private String proposition;
	private List<Variable> variables;

	public Proposition(String proposition, List<Variable> variables) {
		super();
		this.proposition = proposition;
		this.variables = variables;
	}

	public String getProposition() {
		return proposition;
	}

	public void setProposition(String proposition) {
		this.proposition = proposition;
	}
	
	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public void parserProposition() {
		// TODO
	}
	
	public boolean estParser() {
		// TODO
		return false;
	}

	@Override
	public String toString() {
		return "Proposition [proposition=" + proposition + ", variables=" + variables + ", getVariables()="
				+ getVariables() + "]";
	}	
}
