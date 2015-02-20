import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Matrix extends ArrayList<Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The number of rows in this matrix.
	 */
	private int rows;

	/**
	 * The number of columns in this matrix.
	 */
	private int cols;

	/**
	 * Empty Matrix constructor.
	 */
	public Matrix() {
		rows = 0;
		cols = 0;
	}
	
	/**
	 * Constructor.
	 * @param r Number of rows.
	 * @param c Number of columns.
	 */
	public Matrix(int r, int c) {
		this(r, c, 0.0);
	}
	
	/**
	 * Constructor with initialization value.
	 * @param r Number of rows.
	 * @param c Number of columns.
	 * @param val The value to initialize with.
	 */
	public Matrix(int r, int c, double val) {
		super(r*c);
		rows = r;
		cols = c;

		for (int i = 0; i < rows*cols; i++)
			add(i, val);
	}

	
	
	/**
	 * Constructor for creating a submatrix.
	 * @param r
	 * @param c
	 * @param subList
	 */
	public Matrix(int r, int c, List<Double> subList) {
		super(subList);
		rows = r;
		cols = c;
	}

	public Matrix(Matrix m){
		cols = m.cols();
		rows = m.rows();
		addAll(m);
	}
	
	public Matrix(String dataFile) {
		this(readData(dataFile));
	}
	
	/**
	 * Sets the value in the signature matrix.
	 * @param r The row of the value to be set.
	 * @param c The column of the value to be set.
	 * @param value The value to set at (r, c).
	 */
	public void set(int r, int c, double value) {
		set((cols*r) + c, value);
	}

	/**
	 * Returns the value of the signature matrix.
	 * @param r The row of the value.
	 * @param c The column of the value.
	 * @return The value at (r, c).
	 */
	public double get(int r, int c) {
		return get((cols*r) + c);
	}
	
	/**
	 * Appends a row to the matrix.
	 * @param row The row to append.
	 */
	public void appendRow(Matrix row) {
		assert(row.rows() == 1 && (row.cols() == cols()) || cols() == 0);
		
		if (cols() == 0)
			cols = row.cols();
		
		addAll(row);
		rows++;
	}

	/**
	 * @return Returns the number of rows in the signature matrix.
	 */
	public int rows() {
		return rows;
	}

	/**
	 * @return Returns the number of columns in the signature matrix.
	 */
	public int cols() {
		return cols;
	}
	
	/**
	 * Reads in a Matrix from fileName.
	 * @param fileName The file name of the matrix.
	 * @return The parsed matrix in fileName.
	 */
	public static Matrix readData(String fileName) {
		// create the result
		Matrix result = new Matrix();
		
		try {
			// open the file
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			// parse the content
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				
				Matrix row = new Matrix(1, split.length);
				for (int i = 0; i < split.length; i++)
					row.set(0, i, Double.parseDouble(split[i]));
				result.appendRow(row);
			}

			// close the file
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
	
	/**
	 * Calculates the dot product between this matrix and the other matrix.
	 * In other words, it calculates "this * other"
	 * @param other The matrix that is to be multiplied with.
	 * @return The product of the two matrices.
	 */
	public Matrix dot(Matrix other) {
		assert(this.cols() == other.rows());

		Matrix result = new Matrix(this.rows(), other.cols());
		
		for(int r = 0; r < result.rows(); r++){
			for(int c = 0; c < result.cols(); c++){
				Double value = 0.0;	
				
				for(int i = 0; i < cols(); i++)
					value += get(r, i) * other.get(i, c);
								
				result.set(r, c, value);
			}
		}

		return result;
	}

	/**
	 * Multiplies the matrix with a scalar, scaling each element in the matrix.
	 * @param scalar The scalar with which to multiply each element.
	 * @return A new matrix with scaled elements.
	 */
	public Matrix multiply(double scalar) {
		Matrix result = (Matrix) this.clone();

		for(int i = 0; i < this.size(); i++){
			result.set(i, this.get(i)*scalar);
		}

		return result;
	}

	/**
	 * Adds one matrix to another. They need to be of the same size.
	 * @param other The other matrix.
	 * @return A new matrix where all elements of the two matrices have been added.
	 */
	public Matrix add(Matrix other) {
		assert(cols() == other.cols() && rows() == other.rows());

		Matrix result = new Matrix(rows(), cols());
		
		for(int a = 0; a < this.size(); a++)
			result.set(a, this.get(a)+other.get(a));

		return result;
	}
	
    /**
     * Transposes the matrix.
     * @return The transposed matrix.
     */
    public Matrix transpose() {
		Matrix result = new Matrix(cols(), rows());
     
		for(int c = 0; c < result.cols(); c++)
			for(int r = 0; r < result.rows(); r++){
				result.set(r, c, get(c, r));
			}
		
        return result;
    }
    
    /**
     * Calculates the Frobenius norm.
     * @return The Frobenius norm.
     */
    public double norm() {
    	double result = 0.0;
    	
    	for(Double d : this)
    		result += d*d;
    	
    	return Math.sqrt(result);
    }
    
    /**
     * Retrieves row r from this matrix.
     * @param r The index of the row to get.
     * @return The row at index r.
     */
    public Matrix row(int r) {
    	return new Matrix(1, cols(), subList(r*cols(), (r+1)*cols()));
    }
    
    public Matrix col(int r) {
    	return transpose().row(r).transpose();
    }
    
    public Double sum(){
    	Double result = 0.0;
    	for(Double v : this)
    		result+=v;
    	return result;
    }
    
    /**
     * Calculates the mean of this matrix to one row.
     * In other words, each element in the resulting row
     * is the mean of a column in this matrix.
     * @return The mean row of this matrix.
     */
    public Matrix meanRow() {
    	Matrix mean = new Matrix(1, cols());
    	
    	for(int i = 0; i < cols(); i++)
    		mean.set(i, col(i).sum() / ((double) rows()));    			
    	return mean;
    }
    
    /**
     * Subtracts a row from this matrix. r.cols() must
     * be equal to cols().
     * @param r The row to subtract.
     * @return This matrix with r subtracted from it.
     */
    public Matrix subtractRow(Matrix r) {
    	assert(r.cols() == cols());
    	assert(r.rows() == 1);
    	
    	Matrix result = new Matrix(rows(), cols());
    	
    	for(int i = 0; i < this.size(); i++)	
    		result.set(i, this.get(i) - r.get(i % cols()));
    	
    	return result;
    }

    /**
     * Compute the covariance matrix
     * @return The covariance matrix.
     */
    public Matrix cov(){
//    	mr = meanrow
    	Matrix mr = meanRow();
    	
//    	mmr = m minus mr
    	Matrix mmr = this.subtractRow(mr);
    	
    	return (mmr.transpose().dot(mmr));
    }
    
    
	/**
	 * Puts the matrix in a String format.
	 */
	@Override
	public String toString() {
		String result = "[";
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				result += get(r, c) + ", ";
			}
			result += "\n";
		}
		return result.substring(0, result.length() - 3) + "]\n";
	}

	public static void test() {
		
		Matrix template = new Matrix(2,2);
		
		for(Integer i = 0; i < template.size(); i++){
			template.set(i, (double) i+1);
		}

		System.out.println("A");
		System.out.println(template);
		
//		Dot
		{
			Matrix A = (Matrix) template.clone();
			
			A = A.dot(A);
			
			Double[] expected = {7.0, 10.0, 15.0, 22.0};

			System.out.println("A * A");
			System.out.println(A);
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));		
		}
		{
			Matrix A = (Matrix) template.clone();
			Matrix B = new Matrix(2,1);
			B.set(0, 1.0);
			B.set(1, 2.0);
			
			System.out.println("B");
			System.out.println(B);
			
			A = A.dot(B);
			
			Double[] expected = {5.0, 11.0};

			System.out.println("A * B");
			System.out.println(A);
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));
		}
	
		
//		Multiply
		{
			Matrix A = (Matrix) template.clone();
			Double scalar = 2.0;
			
			A = A.multiply(scalar);
			
			Double[] expected = {2.0, 4.0, 6.0, 8.0};

			System.out.println("A * 2");
			System.out.println(A);
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));
		}
		
//		Add
		{
			Matrix A = (Matrix) template.clone();
			
			A = A.add(A);
			
			Double[] expected = {2.0, 4.0, 6.0, 8.0};

			System.out.println("A + A");
			System.out.println(A);
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));
		}
		
//		Transpose
		{
			Matrix A = (Matrix) template.clone();
			
			A = A.transpose();
			
			Double[] expected = {1.0, 3.0, 2.0, 4.0};

			System.out.println("A transposed");
			System.out.println(A);
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));
		}
		
//		Norm
		{
			Matrix A = (Matrix) template.clone();
						
			Double expected = Math.sqrt(1.0+4.0+9.0+16.0); 

			System.out.println("Frobenius norm of A");
			System.out.println(A.norm()+"\n");
			
			assert(expected.equals(A.norm()));
		}
				
//		Col
		{
			Matrix A = (Matrix) template.clone();
		
			A = A.col(0);			
			
			Double[] expected = {1.0, 3.0};

			System.out.println("A col 1");
			System.out.println(A);
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));
		}
		
//		Sum
		{
			Matrix A = (Matrix) template.clone();
			
			Double value = A.sum();
			
			Double expected = (1.0+2.0+3.0+4.0);

			System.out.println("A sum");
			System.out.println(value+"\n");
			
			assert(expected.equals(value));		
		}
		
//		MeanRow
		{
			Matrix A = (Matrix) template.clone();
		
			A = A.meanRow();
			
			Double[] expected = {2.0, 3.0};

			System.out.println("A mean row");
			System.out.println(A);		
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));
		}
		
//		SubtractRow
		{
			Matrix A = (Matrix) template.clone();
			Matrix B = new Matrix(1,2);
			B.set(0, 1.0);
			B.set(1, 2.0);
			
			System.out.println("B");
			System.out.println(B);		
			
			A = A.subtractRow(B);
			
			Double[] expected = {0.0, 0.0, 2.0, 2.0};

			System.out.println("A subtractRow B");
			System.out.println(A);		
			
			for(int i = 0; i < expected.length; i++)
				assert(A.get(i).equals(expected[i]));
		}		
	}
	
}
