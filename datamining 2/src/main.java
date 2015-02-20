import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class main {

	public static void main(String[] args) {

        // FILL IN YOUR CODE HERE
            
            PageRank pr = new PageRank();
            TaxationPageRank tpr = new TaxationPageRank(0.5);
            TaxationPageRank airports = new TaxationPageRank(1);
            
            pr.importData("//home/mo/TU/datamining 2/data/example2.txt");
            tpr.importData("//home/mo/TU/datamining 2/data/example2.txt");
            airports.importData("//home/mo/TU/datamining 2/data/flight_data.txt");
            
            System.out.println(pr.calculatePageRank(10));
            System.out.println(tpr.calculatePageRank(10));
            System.out.println(sortByValues(airports.calculatePageRank(10)));
            
	}

	/*
	 * Java method to sort Map in Java by value e.g. HashMap or Hashtable
	 * throw NullPointerException if Map contains null values
	 * It also sort values even if they are duplicates
	 */
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
		List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		//LinkedHashMap will keep the keys in the order they are inserted
		//which is currently sorted on natural ordering
		Map<K,V> sortedMap = new LinkedHashMap<K,V>();

		for(Map.Entry<K,V> entry: entries){
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

}
