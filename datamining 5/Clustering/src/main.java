
public class main {
	
	private static void hierarchical() {
		HierarchicalClusteringPlotter hcp = new HierarchicalClusteringPlotter(3, "data/cluster.txt");
	}
	
	private static void hierarchicalDigits() {
		int k = 10;
		HierarchicalClustering hc = new HierarchicalClustering(k, "data/train_digits.txt");
		while(hc.getClusterSize() > k)
			hc.update();
		for(int i = 0; i < k; i++){
			DigitFrame df = new DigitFrame("je moeder");
			df.showImage(hc.getCluster(i).centroid(), 8, 8);
		}
	}
	
	private static void kmeans() {
		KMeansPlotter kmp = new KMeansPlotter(3, "data/cluster.txt");			
	}
	
	private static void kmeansTuneK() {
		for(int k = 1; k <= 10; k++){
			KMeans km = new KMeans(k, "data/cluster.txt");
			km.train();
			System.out.println(k+": "+km.MMRSS());
		}
	}
	
	private static void kmeansDigits() {
		KMeans km = new KMeans(10, "data/train_digits.txt");
		km.train();
		for(Cluster c : km.getClusters()){
			System.out.println(c);
			DigitFrame df = new DigitFrame("Je vader");
			df.showImage(c.centroid(), 8, 8);
		}
	}

	public static void main(String[] args) {
//		hierarchical();
//		hierarchicalDigits();
		kmeans();
//		kmeansTuneK();
//		kmeansDigits();
	}

}
