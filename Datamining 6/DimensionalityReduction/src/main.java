import java.util.ArrayList;
import java.util.List;


public class main {
	
	/**
	 * Computes nrVectors eigen vectors of m where e is the
	 * stopping criterion for the norm of the difference for an
	 * eigenvector in between two rounds.
	 * @param m The matrix of which eigenvectors should be computed.
	 * @param nrVectors The number of eigenvectors to compute.
	 * @param e The threshold for the stopping criterion.
	 * @return A list of eigenvectors in m.
	 */
	public static List<Matrix> powerIteration(Matrix m, int nrVectors, double e) {
		assert(m.cols() == m.rows() && m.cols() >= nrVectors);
		
		List<Matrix> eigenvectors = new ArrayList<Matrix>();
		
		for(int i = 0; i < nrVectors; i++){
			System.out.println("Computing eigenvector "+(i+1)+" of "+nrVectors);
			Matrix v = new Matrix(m.rows(), 1, 1.0);
			Double prevNorm = v.norm();
			boolean c = true;

			Matrix mv = m.dot(v);
			while(c) {
				mv = m.dot(v);
				v = mv.multiply(1.0/mv.norm());
				Double norm = v.norm();
				if(Math.abs(prevNorm - norm) < e)
					c = false;
				prevNorm = norm;
			}
			
			eigenvectors.add(v);
			
			Double lambda = v.transpose().dot(mv).get(0);
			Matrix vt = v.transpose();
			
			m = m.add(v.dot(vt).multiply(-1*lambda));
			
		}
		
		return eigenvectors;
	}
	
	/**
	 * Computes two eigenvectors of a small matrix example.
	 */
	public static void powerIterationTest() {
		Matrix m = new Matrix("data/matrix.txt");
				
		ArrayList<Matrix> evectors = (ArrayList<Matrix>) powerIteration(m, 2, (1.0/1000000.0));
		
		System.out.println(evectors);
	}

	/**
	 * Computes the principal components from a Gaussian
	 * distributed dataset.
	 */
	public static void pca() {
		Matrix m = new Matrix("data/gaussian.txt");
				
		Matrix cov = m.cov();
		
		ArrayList<Matrix> evectors = (ArrayList<Matrix>) powerIteration(cov, 2, (1.0/100000.0));
		
		System.out.println(evectors);

		PCAPlotter pcap = new PCAPlotter();
		Matrix plot = new Matrix();
		for(Matrix evector : evectors)
			 plot.appendRow(evector.transpose());
		pcap.plotData(plot);
		
	}
	
	/**
	 * Computes some principal components from a dataset
	 * of face images.
	 */
	public static void pcaFaces() {
		Matrix m = Matrix.readData("data/faces.txt");
		
		Matrix cov = m.cov();
		
		ArrayList<Matrix> evectors = (ArrayList<Matrix>) powerIteration(cov, 10, (1.0/100000.0));
		
		for(Matrix evector : evectors){
			ImageFrame img = new ImageFrame("je moeder");
			img.showImage(evector, 32, 32);
		}
		
	}

	public static void main(String[] args) {
//		Matrix.test();
//		powerIterationTest();
//		pca();
		pcaFaces();
	}

}
