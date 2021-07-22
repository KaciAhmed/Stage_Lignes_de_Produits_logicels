
package package1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class MatriceSimilarite extends Matrice {
	List<Annotation> dictionnaireAnnotations;

	public MatriceSimilarite() {
		super();
		this.dictionnaireAnnotations = new ArrayList<Annotation>();
	}

	public MatriceSimilarite(List<Annotation> dictionaireAnnotations) {
		super();
		this.dictionnaireAnnotations = dictionaireAnnotations;
		super.setNblignes(dictionaireAnnotations.size());
		super.setNbColonnes(dictionaireAnnotations.size());
		super.setMatrice(new double[super.getNblignes()][super.getNbColonnes()]);
		for (int i = 0; i < super.getNblignes(); i++) {
			for (int j = 0; j < super.getNbColonnes(); j++) {
				super.getMatrice()[i][j] = 0;
			}
		}
	}

	@Override
	public void calculerMatrice() {
		double pourcentage = 0;
		for (int i = 0; i < super.getNblignes(); i++) {
			for (int j = 0; j < super.getNbColonnes(); j++) {
				pourcentage = this.getPourcentageSimilariteCodeVariant(
						this.dictionnaireAnnotations.get(i).getCodeVariant(),
						this.dictionnaireAnnotations.get(j).getCodeVariant());
				this.insererElement(i, j, pourcentage);
			}
		}
	}

	private double getPourcentageSimilariteCodeVariant(CodeVariant codeVariant1, CodeVariant codeVariant2) {
		double pourcentage = 0;
		long nbLigneSimilaire = 0;

		this.supprimerLignesVideCodeVariant(codeVariant1);
		this.supprimerLignesVideCodeVariant(codeVariant2);

		int sizeVariant1 = codeVariant1.getLigneDeCode().size();
		int sizeVariant2 = codeVariant2.getLigneDeCode().size();

		long minNbLigne = Math.min(sizeVariant1, sizeVariant2);
		long maxNbLigne = Math.max(sizeVariant1, sizeVariant2);

		if ((maxNbLigne == 0) && (minNbLigne == 0)) {
			return 1;
		} else {
			for (int i = 0; i < minNbLigne; i++) {
				this.pretraitmentLigneCodeVariant(codeVariant1.getLigneDeCode().get(i),
						codeVariant2.getLigneDeCode().get(i));
				if (codeVariant1.getLigneDeCode().get(i).equals(codeVariant2.getLigneDeCode().get(i))) {
					nbLigneSimilaire++;
				}
			}
			pourcentage = (nbLigneSimilaire + 0.0) / (maxNbLigne + 0.0);
			return pourcentage;
		}
	}

	private void pretraitmentLigneCodeVariant(String ligne1, String ligne2) {
		ligne1.trim();
		ligne1.replaceAll("\\s+", "\\s");
		ligne1.replaceAll("\\t+", "");
		ligne1.toLowerCase();

		ligne2.trim();
		ligne2.replaceAll("\\s+", "\\s");
		ligne2.replaceAll("\\t+", "");
		ligne2.toLowerCase();
	}

	private void supprimerLignesVideCodeVariant(CodeVariant codeVariant) {
		for (int i = 0; i < codeVariant.getLigneDeCode().size(); i++) {
			if (codeVariant.getLigneDeCode().get(i).isEmpty()) {
				codeVariant.getLigneDeCode().remove(i);
			}
		}
	}

	public double getPourcentageSimilarite(Annotation annotation1, Annotation annotation2) {
		int index1 = this.dictionnaireAnnotations.indexOf(annotation1);
		int index2 = this.dictionnaireAnnotations.indexOf(annotation2);

		return this.getMatrice()[index1][index2];
	}

	@Override
	public void afficher() {
		System.out.println(super.getNblignes() + " " + super.getNbColonnes());
		for (int i = 0; i < super.getNblignes(); i++) {
			for (int j = 0; j < super.getNbColonnes(); j++) {
				System.out.print((int) super.getMatrice()[i][j] + " ");
			}
			System.out.println();
		}
	}

	@Override
	public void creerImageMatrice(String cheminFichier) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(cheminFichier, "UTF-8");
		int couleurMax = 100;
		StringBuilder lignes = new StringBuilder();
		lignes.append("P3\n");
		lignes.append("#Un exemple en couleur\n");
		lignes.append(super.getNblignes()).append(" ").append(super.getNbColonnes()).append("\n");
		lignes.append(couleurMax).append("\n");
		for (int i = 0; i < super.getNblignes(); i++) {
			for (int j = 0; j < super.getNbColonnes(); j++) {

				boolean estSurLaDiagonaleEtFichierDifferent = (i > 1) && (i == j) && !this.dictionnaireAnnotations
						.get(i).getNomDeFichier().equals(this.dictionnaireAnnotations.get(i - 1).getNomDeFichier());
				if (estSurLaDiagonaleEtFichierDifferent) {
					String pixelChangementDeFichier = (int) (super.getMatrice()[i][j] * 100) + " 0 0 ";
					lignes.append(pixelChangementDeFichier);
				} else {
					String composanteRouge = (couleurMax - (int) (super.getMatrice()[i][j] * 100)) + "";
					String composanteVert = composanteRouge;
					String composanteBleu = composanteRouge;
					lignes.append(composanteRouge).append(" ").append(composanteVert).append(" ").append(composanteBleu)
							.append(" ");

				}
			}
			lignes.append("\n");
		}
		writer.println(lignes.toString());
		writer.close();
		try {
			BufferedImage myPicture = ImageIO.read(new File(cheminFichier));
			String extensionPng = cheminFichier + ".png";
			if (!ImageIO.write(myPicture, "PNG", new File(extensionPng))) {
				// Handle image not written case
				System.out.println("Aucune image convertie");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}