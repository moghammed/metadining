import java.util.ArrayList;
import java.util.List;


public class main {

	public static void perceptron() {
		Perceptron p = new Perceptron(1.0);
		Dataset d = new Dataset();
		PerceptronPlotter pp = new PerceptronPlotter("je moeder", "ja toch");
		
		d.readData("data/gaussian.txt", true);
		
		p.updateWeights(d);
		
		pp.plotData(d, p);
		
	}

	public static void perceptronDigits() {
		Dataset train = new Dataset();
		train.readData("data/train_digits.txt", true);
		
		Dataset test = new Dataset();
		test.readData("data/test_digits.txt", true);
		
		
		
		Perceptron p = new Perceptron(1.0);
		
		p.updateWeights(train);
		p.updateWeights(train);		
		
		int errors = 0;
		
		for(FeatureVector fv : test)
			if(p.predict(fv) != fv.label)
				errors++;
		
		System.out.printf("Errors: %d, out of %d. Error rate: %f", errors, test.size(), (double) errors/test.size());
		
		
	}

	public static void nearestNeighbour() {
		NearestNeighbour nn = new NearestNeighbour();
		NearestNeighbourPlotter nnp = new NearestNeighbourPlotter(3);
		
		nn.readData("data/banana.txt");
		
		nnp.plotData(nn);
	}
	
	public static void nearestNeighbourDigits() {		
		NearestNeighbour nn = new NearestNeighbour();
		
		nn.readData("data/train_digits.txt");
		
		Dataset test = new Dataset();
		test.readData("data/test_digits.txt", true);
		
		int errors = 0;
		
		for(FeatureVector fv : test)
			if(nn.predict(fv, 3) != fv.label)
				errors++;
		
		System.out.printf("Errors: %d, out of %d. Error rate: %f", errors, test.size(), (double) errors/test.size());
		
	}

	public static void main(String[] args) {
//		perceptron();
//		perceptronDigits();
		nearestNeighbour();
//		nearestNeighbourDigits();
	}

}
