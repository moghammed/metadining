import java.util.ArrayList;
import java.util.List;

public class KMeans {
	/**
	 * The number of clusters to detect.
	 */
	private int k;
	
	/**
	 * A collection of (k) clusters.
	 */
	private List<Cluster> clusters;
	
	/**
	 * The unclustered data.
	 */
	private Cluster data;

	/**
	 * Constructor.
	 * @param k The number of clusters to detect.
	 * @param fileName The filename which holds the cluster data.
	 */
	public KMeans(int k, String fileName) {
		this.k = k;
		clusters = new ArrayList<Cluster>();
		data = new Cluster();

		readData(fileName);
	}

	/**
	 * @param i The index of the cluster that is to be retrieved.
	 * @return The cluster at index i.
	 */
	public Cluster getCluster(int i) {
		return clusters.get(i);
	}
	
	/**
	 * @return The unclustered data.
	 */
	public Cluster getData() {
		return data;
	}
	
	/**
	 * @return The collection of clusters.
	 */
	public List<Cluster> getClusters() {
		return clusters;
	}
	
	/**
	 * @return The number of clusters.
	 */
	public int getClusterSize() {
		return clusters.size();
	}

	/**
	 * Reads in the data of filename.
	 * @param fileName The file which is to be read.
	 */
	private void readData(String fileName) {
		data.readData(fileName);
	}

	/**
	 * Adds a new init point which is furthest away from the existing init
	 * points.
	 */
	private Cluster getInitPoint() {
		int r = (int) Math.floor(Math.random()*data.size());
		
		FeatureVector fv = data.get(r);
		
		Cluster c = new Cluster();
		c.add(fv);
		
		return c;
	}
	
	/**
	 * Clears all elements from the clusters.
	 */
	private void clearClusters() {
		for (Cluster c: clusters) {
			c.clear();
		}
	}
	
	public Double MMRSS() {
		Double result = 0.0;
		for(Cluster c : getClusters()) {
			result += c.calculateAverageRSS();
		}
		return result / k;
	}
	
	public void train() {
		update();
		
		Double status = 0.0;
		
		while(!status.equals(MMRSS())){
//			System.out.println(status);
//			System.out.println(MMRSS());
			status = MMRSS();
			update();
		}
	}
	
	/**
	 * Computes clusters based on the centroids of the clusters in the previous round.
	 * If no such clusters exist yet, it will select k random points.
	 */
	public void update() {
		ArrayList<FeatureVector> centroids = new ArrayList<>();
		if(clusters.isEmpty()){
			for(int i = 0; i < k; i++){
				clusters.add(getInitPoint());
			}
		}
		
		for(Cluster c : getClusters()){
			centroids.add(c.centroid());
			c.clear();
		}
		
		for(FeatureVector point : data){
			Double minDistance = Double.POSITIVE_INFINITY;
			int label = 0;
			for(FeatureVector centroid : centroids){
				Double distance = point.distance(centroid);
				if(distance < minDistance){
					minDistance = distance;
					point.label = label;
				}
				label++;
			}
			clusters.get(point.label).add(point);
		}
		
		for(int i = 0; i < k; i++){
			Cluster c = getCluster(i);
			if(c.isEmpty()){
				clusters.set(i, getInitPoint());
			}
		}		
		
	}
}
