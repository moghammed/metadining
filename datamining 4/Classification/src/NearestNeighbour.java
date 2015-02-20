import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;


public class NearestNeighbour {
	/**
	 * List of training vectors.
	 */
	Dataset dataset;

	/**
	 * Constructor.
	 */
	public NearestNeighbour() {
		dataset = new Dataset();
	}

	/**
	 * Reads in the data from a text file.
	 * @param fileName
	 */
	public void readData(String fileName) {
		dataset.readData(fileName, false);
	}

	/**
	 * 
	 * @return
	 */
	public Dataset getDataset() {
		return dataset;
	}

	/**
	 * Classifies a query. Finds the k nearest neighbours and scales them if necessary.
	 * @param features The query features.
	 * @param k The number of neighbours to select.
	 * @return Returns the label assigned to the query.
	 */
	public int predict(List<Double> features, int k) {
		// the result
		int label = 0;
		
		HashMap<Integer, Integer> labelCount = new HashMap<>(); 
		
		ArrayList<Measurement> distances = new ArrayList<Measurement>();
		
		for(FeatureVector fv : dataset) {
			Double dist = fv.distance(features);
			Measurement distance = new Measurement(fv, dist);
			distances.add(distance);
		}
		
		Collections.sort(distances);
		List<Measurement> subset = distances.subList(0, k);
		
		for(Measurement mes : subset) {
			int lab = mes.featureVector.label;
			
			if(!labelCount.containsKey(lab))
				labelCount.put(lab, 1);
			else {
				int count = labelCount.get(lab);
				labelCount.put(lab, count+1);
			}
		}
		
		int max = 0;
		for(int lab : labelCount.keySet()) {
			if(labelCount.get(lab) > max){
				max = labelCount.get(lab);
				label = lab;
			}
		}
		
		return label;
	}
}