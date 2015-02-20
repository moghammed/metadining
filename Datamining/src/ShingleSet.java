import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * The ShingleSet class contains a sorted set of shingles.
 * @author Mo
 *
 */
public class ShingleSet extends TreeSet<String> implements SortedSet<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2143400328259342668L;
	
	/**
	 * The size of the shingles.
	 */
	public int k;
        private ArrayList<String> s = new ArrayList<String>();
	
	/**
	 * Constructor for the ShingleSet.
	 * @param k The size of the shingles.
	 */
	public ShingleSet(int k) {
		this.k = k;
                this.clear();
	}
	
	/**
	 * Add shingles of size k to the set from String s.
	 * @param s The string that is to be transformed to shingles.
	 */
	public void shingleString(String s) {
            this.s.add(s);
            for(int i = 0; i <= s.length()-k; i++){
                this.add(s.substring(i,i+k));
            }
	}
	
        
	/**
	 * Add shingles of size k to the set from String s, where all white spaces from s are removed.
	 * @param s The string that is to be transformed to shingles.
	 */
	public void shingleStrippedString(String s) {
            s = s.replaceAll("\\s","");
            this.s.add(s);
            shingleString(s);
	}
	
        public ArrayList<String> getString(){
            return this.s;
        }
        
	/**
	 * Calculates the Jaccard distance between this set and the other set.
	 * @param other The other set of shingles that this set will be compared to.
	 * @return The Jaccard distance between this set and the other set.
	 */
	public double jaccardDistance(ShingleSet other) {
//          Create a temporary shingleset, so the original stays the same
            ShingleSet tmp = new ShingleSet(this.k);
            tmp.addAll(this);
            
//          make tmp the intersection of this and other
            tmp.retainAll(other);
            double intersectionSize = tmp.size();
            
            tmp = new ShingleSet(this.k);            
//          make other the union of this and other
            tmp.addAll(this);
            tmp.addAll(other);
            double unionSize = tmp.size();
            
//          compute and return jaccarddistance
            return 1.0 - (intersectionSize/unionSize); // remove when code is completed
	}

}
