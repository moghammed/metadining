import java.awt.Color;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jfree.util.ShapeUtilities;

public class Cluster extends ArrayList<FeatureVector> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7478823693516467914L;
	
	/**
	 * Is true when an element has changed since last computing the centroid.
	 */
	private boolean changed;
	
	/**
	 * The cached centroid.
	 */
	private FeatureVector centroid;
	
	/**
	 * The color of this cluster in the plot.
	 */
	private Color color;
	
	/**
	 * The shape of this cluster in the plot.
	 */
	private Shape shape;
	
	/**
	 * Constructor.
	 * @param key
	 */
	public Cluster() {
		changed = true;
		color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
		shape = ShapeUtilities.createDiagonalCross(3, 1);
		centroid = new FeatureVector();
	}
	
	/**
	 * @return Returns the color of this cluster.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @return Returns the shape of this cluster.
	 */
	public Shape getShape() {
		return shape;
	}
	
	/**
	 * Parses a text file with features and stores them in a cluster.
	 * @param fileName
	 */
	public void readData(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			// every line is in the form of "x y", where x and y are double values
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				FeatureVector fv = new FeatureVector();
				for (int i = 0; i < split.length; i++)
					fv.add(Double.parseDouble(split[i]));
				add(fv);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * Overrides List.add, in order to set the changed variable.
	 */
	public boolean add(FeatureVector fv) {
		changed = true;
		return super.add(fv);
	}
	
	/**
	 * Overrides List.add, in order to set the changed variable.
	 */
	@Override
	public boolean addAll(Collection<? extends FeatureVector> c) {
		changed = true;
		return super.addAll(c);
	}
	
	/**
	 * If the cluster did not change since it has calculated its previous centroid,
	 * it will return the buffered centroid point. Otherwise it will recalculate the
	 * centroid and return that point.
	 * @return The centroid of the cluster.
	 */
	public FeatureVector centroid() {
		if (size() == 0)
			return null;
		
		if (!changed)
			return centroid;
		
		
		for(FeatureVector fv : this) {
			if(centroid.isEmpty()){
				centroid.addAll(fv);
			}
			for(int i = 0; i < fv.size(); i++) {
				Double curr = centroid.get(i);
				centroid.set(i, fv.get(i) + curr);
			}
		}
	
		centroid.divide(this.size());
		
		changed = false;
		
		return centroid;
	}
	
	/**
	 * Calculates the (mean) distance of a cluster with this cluster.
	 * In other words, it calculates the Euclidean distance between
	 * the two centroids.
	 * @param other The other cluster to which the distance is calculated.
	 * @return The distance between this cluser and the other cluster.
	 */
	public double meanDistanceTo(Cluster other) {
		double dist = 0.0;
		
		for(int i = 0; i < this.centroid().size(); i++) {
			double mine = this.centroid().get(i);
			double theirs = other.centroid().get(i);
			
			dist += Math.pow(theirs - mine, 2);
		}
		
		dist = Math.sqrt(dist);
		
		return dist;
	}
	
	/**
	 * Calculates each pairwise distance from this cluster to another.
	 * @param other The cluster to calculate the distances to.
	 * @return A list of distances from this cluster to other.
	 */
	public List<Double> distancesToCluster(Cluster other) {
		List<Double> result = new ArrayList<Double>();
		
		for(FeatureVector from : this){
			for (FeatureVector to : other){
				result.add(from.distance(to));
			}
		}
		
		return result;
	}
	
	/**
	 * @return Returns the minimum distance between two clusters.
	 */
	public double minDistanceTo(Cluster other) {
		assert (size() != 0 && other.size() != 0);
		
		List<Double> distances = distancesToCluster(other);
		
		Collections.sort(distances);
		
		return distances.get(0);
	}
	
	/**
	 * Calculates the residual sum of squares (squares of difference with centroid).
	 * @return The RSS.
	 */
	public double calculateAverageRSS() {
		if (size() == 0){
			System.out.println(this);
			return Double.NaN;	
		}
		
		
		
		double rss = 0.0;
		for(FeatureVector point : this){
			rss += Math.pow(point.distance(this.centroid()), 2);
		}
		return rss / size();
	}
}
