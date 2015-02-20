import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class main {
	
	public static void exercise1_1() {
            int k = 5;

            ShingleSet 	s1 = new ShingleSet(k),
                        s2 = new ShingleSet(k),
                        s3 = new ShingleSet(k), 
                        s4 = new ShingleSet(k);

            s1.shingleString("The plane was ready for touch down");
            s2.shingleString("The quarterback scored a touchdown");

            System.out.println(s1);
            System.out.println(s2);
            System.out.println(s1.jaccardDistance(s2));

            s3.shingleStrippedString("The plane was ready for touch down");
            s4.shingleStrippedString("The quarterback scored a touchdown");

            System.out.println(s3);
            System.out.println(s4);
            System.out.println(s3.jaccardDistance(s4));
	}
	
	public static void exercise1_2() {
            int k = 1;
//
//            ShingleSet 	s1 = new ShingleSet(k),
//                        s2 = new ShingleSet(k),
//                        s3 = new ShingleSet(k), 
//                        s4 = new ShingleSet(k),
//                        total = new ShingleSet(k);
//            
//            s1.shingleStrippedString("je moeder is een vaatdoek");
//            s2.shingleStrippedString("de hoofdpiet neemt de bus naar zijn werk");
//            s3.shingleStrippedString("mijn vader heeft een kaas gestolen");
//            s4.shingleStrippedString("halverwege het tweede couplet verlaat de hond het podium");    
//            
//            MinHash hasher = new MinHash();
//            hasher.addHashFunction(new HashFunction(1, 1));
//            hasher.addHashFunction(new HashFunction(3, 1));
//            
//            hasher.addSet(s1);
//            hasher.addSet(s2);
//            hasher.addSet(s3);
//            hasher.addSet(s4);
//            
//            MinHashSignature signature = hasher.computeSignature();
//                        
//            System.out.println(signature);
            
            MinHash hasher = new MinHash();
            hasher.addRandomHashFunctions(1000);
            
            ArrayList<String> strings = getRandomStrings(100);
            
            for(String s : strings){
                ShingleSet ss = new ShingleSet(k);
                ss.shingleString(s);
                System.out.println(ss);
                System.out.println(ss.getString());
                
                hasher.addSet(ss);
            }
            
            MinHashSignature signature = hasher.computeSignature();
            
            exercise1_3(hasher, signature);
            
	}
        
        public static ArrayList<String> getRandomStrings(int n) {
            ArrayList<String> r = new ArrayList<String>();
            for(int c = 0; c < n; c++){
                String s = "";
                for(int a = 0; a < 10; a++){
                    Integer ascii = (int) Math.floor(Math.random()*10) + 97;
                    s += (char) (int) ascii;
                }
//                System.out.println(s);
                r.add(s);
            }
            return r;
        }
	
	public static void exercise1_3(MinHash mh, MinHashSignature mhs) {
            Set<List<Integer>> cands = LSH.computeCandidates(mhs, 1000, 5);
            
            System.out.println(cands);
            System.out.println(mh.getSets());
                    
            for(List<Integer> similarIndices : cands) {
                if(similarIndices.size()>=2){
                    ArrayList<ShingleSet> sets = new ArrayList<ShingleSet>();
                    
                    for(Integer i : similarIndices){
                        sets.add(mh.getSet(i));
                    }
                    
                    
                    for(int i = 1; i < sets.size(); i++){;
                        ShingleSet curr = sets.get(i);
                        ShingleSet prev = sets.get(i-1);
                        ShingleSet tmp = new ShingleSet(curr.k);
                        tmp.addAll(curr);
                        tmp.retainAll(prev);
                        double jac = curr.jaccardDistance(prev);
                        if(jac<0.2){
                            System.out.println(curr.getString() + " ~= " + prev.getString() + " => " + jac);
                        }
                    }
                }
            }
                    
            
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// exercise 1.1
//		exercise1_1();
		
		// exercise 1.2
		exercise1_2();
                
	}

}
