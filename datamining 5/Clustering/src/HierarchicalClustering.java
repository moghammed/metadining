import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class HierarchicalClustering {
	/**
	 * The number of clusters to detect.
	 */
	private int k;
	
	/**
	 * The original unclustered data.
	 */
	private Cluster data;
	
	/**
	 * A collection of clusters.
	 */
	private List<Cluster> clusters;
	
	/**
	 * Constructor.
	 * @param k The number of clusters to detect.
	 * @param fileName The filename at which the data is stored.
	 */
	public HierarchicalClustering(int k, String fileName) {
		this.k = k;
		clusters = new ArrayList<Cluster>();
		
		data = new Cluster();
		
		readData(fileName);
	}
	
	/**
	 * Reads in data from a filename.
	 * @param fileName The filename that needs to be read.
	 */
	public void readData(String fileName) {
		data.readData(fileName);
		
		for (FeatureVector fv: data) {
			Cluster c = new Cluster();
			c.add(fv);
			clusters.add(c);
		}
	}
	
	/**
	 * @return The unclustered data.
	 */
	public Cluster getData() {
		return data;
	}
	
	/**
	 * @return The clusters that have been computed so far.
	 */
	public List<Cluster> getClusters() {
		return clusters;
	}
	
	/**
	 * 
	 * @return Returns the number of clusters in the list of clusters.
	 */
	public int getClusterSize() {
		return clusters.size();
	}
	
	/**
	 * 
	 * @param i Index of the cluster that is to be retrieved.
	 * @return Returns the cluster belonging to index i.
	 */
	public Cluster getCluster(int i) {
		return clusters.get(i);
	}
	
	private class Triple implements Comparable<Triple>{
		public Cluster a;
		public Cluster b;
		Double distance;
		
		public Triple(Cluster a, Cluster b, Double d){
			this.a = a;
			this.b = b;
			this.distance = d;
		}
		
		@Override
		public int compareTo(Triple o) {
			if (o.distance.equals(distance))
				return 0;
			return o.distance > distance ? -1 : 1;
		}
		
		public String toString(){
			return "distance: "+distance;
		}
	}
	
	/**
	 * Performs one update step of the algorithm.
	 */
	public void update() {
		
		if(clusters.size() == this.k)
			return;
		
		List<Triple> distances = new ArrayList<Triple>();
		
		for(int i = 0; i < clusters.size(); i++){
			for(int j = i+1; j < clusters.size(); j++){
				
				Cluster a = clusters.get(i);
				Cluster b = clusters.get(j);
				
				double distance = a.minDistanceTo(b);
				
				distances.add(new Triple(a,b,distance));
			}
		}
		
		Collections.sort(distances);
		
		Triple t = distances.get(0);
		
		if(t.b.size() > t.a.size()) {
			t.b.addAll(t.a);
			clusters.remove(t.a);
		} else {
			t.a.addAll(t.b);
			clusters.remove(t.b);
		}
			
	}
}
