package package1;

import java.util.List;

public class CodeVariant {
	private List<String> ligneDeCode;
	
	public CodeVariant(List<String> ligneDeCode) {
		super();
		this.ligneDeCode = ligneDeCode;
	}

	public List<String> getLigneDeCode() {
		return ligneDeCode;
	}

	public void setLigneDeCode(List<String> ligneDeCode) {
		this.ligneDeCode = ligneDeCode;
	}

	@Override
	public String toString() {
		return "CodeVariant [ligneDeCode=" + ligneDeCode + "]";
	}
}
