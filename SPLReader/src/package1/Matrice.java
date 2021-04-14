package package1;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public abstract class Matrice {

	private double[][] matrice;
	private int nblignes;
	private int nbColonnes;

	public abstract void calculerMatrice();

	public abstract void afficher();

	public abstract void creerImageMatrice(String cheminFichier)
			throws FileNotFoundException, UnsupportedEncodingException;

	public void insererElement(int i, int j, double elt) {
		this.matrice[i][j] = elt;
	}

	public double[][] getMatrice() {
		return this.matrice;
	}

	public void setMatrice(double[][] matrice) {
		this.matrice = matrice;
	}

	public int getNblignes() {
		return this.nblignes;
	}

	public void setNblignes(int nblignes) {
		this.nblignes = nblignes;
	}

	public int getNbColonnes() {
		return this.nbColonnes;
	}

	public void setNbColonnes(int nbColonnes) {
		this.nbColonnes = nbColonnes;
	}
}