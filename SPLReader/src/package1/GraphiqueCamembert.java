package package1;

import org.jfree.chart.ChartFactory;
import org.jfree.data.general.DefaultPieDataset;

public class GraphiqueCamembert extends Graphique {
	DefaultPieDataset pieDataset = new DefaultPieDataset();

	public GraphiqueCamembert() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GraphiqueCamembert(DefaultPieDataset pieDataset) {
		super();
		this.pieDataset = pieDataset;
	}

	public void creerCamembert(double proportionFusionable, double proportionSimplifiable, double proportionEliminable,
			double proportionOrdinaire) {
		this.pieDataset.setValue("Fusionnable", proportionFusionable);
		this.pieDataset.setValue("Simplifiable", proportionSimplifiable);
		this.pieDataset.setValue("Eliminable", proportionEliminable);
		this.pieDataset.setValue("Ordinaire", proportionOrdinaire);
		// Create the chart
		super.graphique = ChartFactory.createPieChart("Répartitions des Annotations", this.pieDataset, true, true,
				true);
	}

}
