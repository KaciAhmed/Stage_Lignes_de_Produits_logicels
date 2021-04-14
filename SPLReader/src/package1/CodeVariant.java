package package1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "codeVariant")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeVariant implements Serializable {

	@XmlElementWrapper(name = "lignes")
	@XmlElement(name = "ligne")
	private List<String> ligneDeCode = null;

	public CodeVariant() {
		super();
		this.ligneDeCode = new ArrayList<String>();
	}

	public CodeVariant(List<String> ligneDeCode) {
		super();
		this.ligneDeCode = ligneDeCode;
	}

	public List<String> getLigneDeCode() {
		return this.ligneDeCode;
	}

	public void setLigneDeCode(List<String> ligneDeCode) {
		this.ligneDeCode = ligneDeCode;
	}

	public void ajouter(String ligne) {
		this.ligneDeCode.add(ligne);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.ligneDeCode == null) ? 0 : this.ligneDeCode.hashCode());
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
		CodeVariant other = (CodeVariant) obj;
		if (this.ligneDeCode == null) {
			if (other.ligneDeCode != null) {
				return false;
			}
		} else if (!this.ligneDeCode.equals(other.ligneDeCode)) {
			return false;
		}
		return true;
	}
}
