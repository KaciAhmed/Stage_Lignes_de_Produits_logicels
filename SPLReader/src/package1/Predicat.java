package package1;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "predicat")
@XmlAccessorType(XmlAccessType.FIELD)
public class Predicat implements Serializable {
	private String nom;

	public Predicat() {
		super();
	}

	public Predicat(String nom) {
		super();
		this.nom = nom;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String creerImplicationAvec(Predicat predicatAnnotationMere) {
		return this.getNom() + " => " + predicatAnnotationMere;
	}

	@Override
	public String toString() {
		return "[ " + this.nom + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.nom);
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
		Predicat other = (Predicat) obj;
		if (this.nom == null) {
			if (other.nom != null) {
				return false;
			}
		} else if (!this.nom.equals(other.nom)) {
			return false;
		}
		return true;
	}
}
