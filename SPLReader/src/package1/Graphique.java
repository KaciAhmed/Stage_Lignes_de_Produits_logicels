package package1;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

public abstract class Graphique {
	public JFreeChart graphique;

	public void exporterCommeImage(String cheminFichier, int longueur, int largeur) {
		File fichier = new File(cheminFichier);
		try {
			ChartUtils.saveChartAsPNG(fichier, this.graphique, largeur, longueur);
		} catch (IOException e) {
			System.out.println("Erreur lors de la sauvegarde de l'image ...");
			e.printStackTrace();
		}
	}
}