import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 * @author hansgaiser
 *
 */
public class MinHash {
	
	/**
	 * The list of hash functions used to compute the minhash.
	 */
	private List<HashFunction> hashes;
	
	/**
	 * A list of ShingleSets that get hashed.
	 */
	private List<ShingleSet> sets;
	
	/**
	 * The total space of shingles contained in the sets list.
	 */
	private SortedSet<String> space;
	
	/**
	 * Constructor for the MinHash function.
	 */
	public MinHash() {
		hashes = new ArrayList<HashFunction>();
		sets = new ArrayList<ShingleSet>();
		space = new TreeSet<String>();
	}
	
	/**
	 * Adds a ShingleSet s to the list and expands the shingle space.
	 * @param s ShingleSet to be added.
	 */
	public void addSet(ShingleSet s) {
		sets.add(s);
		space.addAll(s);
	}
	
	/**
	 * Gets a ShingleSet from the list of sets.
	 * @param i The index of the ShingleSet.
	 * @return The ShingleSet.
	 */
	public ShingleSet getSet(int i) {
		return sets.get(i);
	}
	
	public List<ShingleSet> getSets() {
		return sets;
	}
	
	/**
	 * Adds HashFunction hf to the list of hash functions.
	 * @param hf The HashFunction to be added.
	 */
	public void addHashFunction(HashFunction hf) {
		hashes.add(hf);
	}
	
	/**
	 * Adds n random hash functions with the given limits.
	 * The functions will be of the form h(x) = alpha * x + beta.
	 * @param n The number of functions to add.
	 * @param alpha_max The maximum of the alpha value.
	 * @param beta_max The maximum of the beta value.
	 */
	public void addRandomHashFunctions(int n, int alpha_max, int beta_max) {
		for (int i = 0; i < n; i++) {
			addHashFunction(new HashFunction((int)(Math.random() * alpha_max), (int)(Math.random() * beta_max)));
		}
	}
	
	/**
	 * Adds n random hash functions, with alpha_max and beta_max set to n.
	 * @param n The number of functions to add.
	 */
	public void addRandomHashFunctions(int n) {
		addRandomHashFunctions(n, n, n);
	}
	
	/**
	 * Computes the MinHash signature of the given set using the hash functions.
	 * @return A MinHashSignature object containing the signature for the set.
	 */
	public MinHashSignature computeSignature() {
		
		MinHashSignature result = new MinHashSignature(hashes.size(), sets.size());
		List<String> spaceList = new ArrayList<String>(space);
		
                int x = 0;
                int y = 0;
		for(String shingle: space) {
                    y = 0;
                    for(ShingleSet set: sets){
                        if(set.contains(shingle)){
                            x = 0;
                            for(HashFunction hash: hashes){
                                int old = result.get(x, y);
                                
                                result.set(x, y, Math.min(hash.hashCode(shingle.hashCode(), spaceList.size()), old));
                                x++;
                            }
                        }
                        y++;
                    }
                }
		
		return result;
	}

}
