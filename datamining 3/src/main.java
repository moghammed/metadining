import java.util.Set;


public class main {

	public static void main(String[] args) {
		int threshold = 3;
		int freqSetSize = 3;
		int buckets = 256;
		
		APriori AP = new PCY(threshold, buckets);
//		APriori AP = new APriori(threshold);
		
		AP.addBasket("Cat and dog bites");
		AP.addBasket("Yahoo news claims a cat mated with a dog and produced viable offspring");
		AP.addBasket("Cat killer likely is a big dog");
		AP.addBasket("Professional free advice on dog training puppy training");
		AP.addBasket("Cat and kitten training and behavior");
		AP.addBasket("Dog & Cat provides dog training in Eugene Oregon");
		AP.addBasket("Dog and cat is a slang term used by police officers for a male female relationship");
		AP.addBasket("Shop for your show dog grooming and pet supplies");

		Set<StringSet> freqSets = AP.getFrequentSets(freqSetSize);
		System.out.println(freqSets);
		
		System.out.println("Number of candidate tests: "+AP.canditests);
	}

}