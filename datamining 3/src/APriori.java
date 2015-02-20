import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class APriori {
	/**
	 * The dataset. Each basket could be thought of as sentences, each entry as words.
	 */
	protected List<Set<String>> baskets;

	/**
	 * The threshold for when an item is frequent.
	 */
	protected int supportThreshold;

	public int canditests = 0;
	
	/**
	 * Constructor for the A-Priori class.
	 * @param s The support threshold value.
	 */
	public APriori(int st) {
		supportThreshold = st;
		baskets = new ArrayList<Set<String>>();
	}

	/**
	 * Adds a basket (sentence) to the list of baskets.
	 * @param basket The basket to add.
	 */
	public void addBasket(String basket) {
		baskets.add(new HashSet<String>(Arrays.asList(basket.toLowerCase().split(" "))));
	}

	/**
	 * Computes all subsets of size k from set.
	 * @param set The set of which the subsets should be computed.
	 * @param k The size of the computed subsets.
	 * @return A set of subsets.
	 */
	public static Set<StringSet> getSubsets(Set<String> set, int k) {
		Set<StringSet> result = new HashSet<StringSet>();
		StringSet setList = new StringSet(set);

		StringSet subset = new StringSet();
		getSubsets_(setList, subset, k, result);
		return result;
	}

	/**
	 * Recursive method for getSubsets.
	 */
	private static void getSubsets_(StringSet set, StringSet subset, int subsetSize, Set<StringSet> candidates) {
		if (subsetSize == subset.size()) {
			candidates.add((StringSet)subset.clone());
		} else {
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String s = it.next();
				subset.add(s);
				StringSet clone = new StringSet(set);
				clone.remove(s);
				getSubsets_(clone, subset, subsetSize, candidates);
				subset.remove(s);
			}
		}
	}

	/**
	 * Constructs candidates based on the previous set of frequent itemsets (L_k-1)
	 * @param filteredCandidates The set of frequent k-1 itemsets
	 * @return A set of candidate itemsets of size k
	 */
	public Set<StringSet> constructCandidates(Set<StringSet> filteredCandidates, int k) {
		// the result
		Set<StringSet> candidates = new HashSet<StringSet>();

		System.out.println(filteredCandidates);
		
		if (filteredCandidates == null) {
			// add all initial words to the items set
			for (Set<String> basket: baskets) {
				for (String s: basket) {
					StringSet sl = new StringSet();
					sl.add(s);
					canditests++;
					candidates.add(sl);
				}
			}
		} else {
			for (StringSet s1: filteredCandidates) {
				for (StringSet s2: filteredCandidates) {
					StringSet s3 = new StringSet();
					s3.addAll(s1);
					s3.addAll(s2);		
					
					canditests++;
					
					if(s3.size() == s1.size()+1)
						candidates.add(s3);
					
            	}
            }
		}

		return candidates;
	}

	/**
	 * Calculates the support value of each set in candidates.
	 * @param candidates The set of candidate sets.
	 * @param k The size of the candidates.
	 * @return A mapping of sets to support value.
	 */
	public Map<StringSet, Integer> countCandidates(Set<StringSet> candidates, int k) {
		// the result
		Map<StringSet, Integer> candidatesCount = new HashMap<StringSet, Integer>();
		
		for(StringSet candidate : candidates) {
			candidatesCount.put(candidate, 0);
		}
		
		for (Set<String> basket: baskets) {
			Set<StringSet> subsets = getSubsets(basket, k);
			for(StringSet subset: subsets) {
				if(candidates.contains(subset)){
					int count = candidatesCount.get(subset)+1;
					candidatesCount.put(subset, count);
				}
			}
		}

		return candidatesCount;
	}


	/**
	 * Removes those candidates that have a support value lower than supportThreshold.
	 * @param candidatesCount The map of sets to support value.
	 * @return A set of filtered candidates.
	 */
	public Set<StringSet> filterCandidates(Map<StringSet, Integer> candidatesCount) {
		// the result
		Set<StringSet> filteredCandidates = new HashSet<StringSet>();
		
		for(StringSet candidate : candidatesCount.keySet()){
			int count = candidatesCount.get(candidate);
			if(count >= supportThreshold)
				filteredCandidates.add(candidate);
		}

		return filteredCandidates;
	}

	/**
	 * Calculates frequent sets of size k from the baskets.
	 * @param k The size of the frequent sets.
	 * @return Set of frequent itemsets with size k
	 */
	public Set<StringSet> getFrequentSets(int k) {
		// the result
		Set<StringSet> filteredCandidates = null;

		// add code here
		for(int i = 1; i <= k; i++){
			filteredCandidates = constructCandidates(filteredCandidates, i);
			
//			System.out.println(filteredCandidates);
			
			Map<StringSet, Integer> counts = countCandidates(filteredCandidates, i);
			

//			System.out.println(counts);
			
			filteredCandidates = filterCandidates(counts);
		}

		return filteredCandidates;
	}


}
