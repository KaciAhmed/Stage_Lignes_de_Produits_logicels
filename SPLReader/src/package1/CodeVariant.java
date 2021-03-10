package package1;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "codeVariant")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodeVariant implements Serializable {

	@XmlElementWrapper(name = "lignes")
	@XmlElement(name = "ligne")
	private List<String> ligneDeCode = null;

	public CodeVariant() {
		super();
		// TODO Auto-generated constructor stub
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
}
