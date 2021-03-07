package package1;

import java.util.List;

public class CodeVariant {
	private List<String> ligneDeCode;

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

}
