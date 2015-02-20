import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author hansgaiser
 *
 */
public class LSH {
	
	/**
	 * Computes the candidate pairs using the LSH technique.
	 * @param mhs The minhash signature object.
	 * @param bs The number of buckets to be used in the LSH table.
	 * @param r The number of rows per band to be used.
	 * @return Returns a set of indices pairs that are candidate to being similar.
	 */
	public static Set<List<Integer>> computeCandidates(MinHashSignature mhs, int bs, int r) {
		// assert that the number of rows can be evenly divided by r
		assert(mhs.rows() % r == 0);
		
		// the number of bands
		int b = mhs.rows() / r;
		
		// the result
		Set<List<Integer>> candidates = new HashSet<List<Integer>>();
		
		// ADD CODE HERE
                
                //init them buckets.
                ArrayList<ArrayList<Integer>> buckets = new ArrayList<ArrayList<Integer>>();
                for(int x = 0; x < bs; x++){
                    buckets.add(new ArrayList<Integer>());
                }
                
                //fill them buckets up.
		for(int x = 0; x < b; x++){
                    for(int y = 0; y < mhs.cols(); y++) {
                        String s = mhs.colSegment(y, x*r, (x+1)*r);
                        buckets.get(Math.abs(s.hashCode()%bs)).add(y);
                    }
                }
                
                //Retrieve them candidates
                for(int x = 0; x < b; x++){
                    for(int y = 0; y < mhs.cols(); y++) {
                        String s = mhs.colSegment(y, x*r, (x+1)*r);
                        candidates.add(buckets.get(Math.abs(s.hashCode()%bs)));
                    }
                }
                
                
		return candidates;
	}
	
}
