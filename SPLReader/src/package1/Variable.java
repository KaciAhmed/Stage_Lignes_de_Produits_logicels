package package1;

public class Variable {
	private String nom;

	public Variable(String nom) {
		super();
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return "Variable [nom=" + nom + "]";
	}
}
