package matrix;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Each instance of this class stores a matrix.
 * opmerking:
 * deel van documentatie staat hier niet in, zie branch immutable-columnmajor
 * EXTRA NOTITIES ZIE BRANCHE IMMUTABLE-COLUMNMAJOR
 * 
 * @invar | getRows() > 0
 * @invar | getColumns() > 0
 */

public class Matrix {
	/**
	 * @invar | rows > 0
	 */
	private int rows;
	/**
	 * @invar | columns > 0
	 */
	private int columns;
	
	/**
	 * @representationObject
	 * @invar | elements != null
	 * @invar | elements.length == rows*columns
	 */
	private double[] elements;

	/**
	 * @basic
	 * @immutable
	 * Door hier immutable te zetten moeten we nadien in de methodes niet meer
	 * zeggen dat de waarde van deze inspector niet wijzigt.
	 */
	
	public int getRows() {
		return rows;
	}
	/**
	 * @immutable
	 * @basic
	 */
	public int getColumns() {
		return columns;
	}
	
	/**
	 * @throws | row <= 0 || row > getRows() || column <=0 || column > getColumns()
	 * @post | result == getMatrixRowMajor()[row*getColumns() + column]
	 */
	
	public double getElementAt(int row, int column) {
		if(row <= 0 || row > getRows() || column <=0 || column > getColumns())
			throw new IllegalArgumentException("Unvalid row or column");
		
		return elements[row*getColumns() + column];
	}
	/**
	 *@post | result.length == getRows()*getColumns()
	 *@creates | result
	 */
	public double[] getMatrixRowMajor() {
		return elements.clone();
	}
	/**
	 * @creates| result
	 * @post | result.length == getRows()*getColumns()
	 */
	
	public double[] getMatrixColumnMajor() {
		double[] result = new double[elements.length];
		for(int column = 0; column < getColumns(); column++) {
			for ( int row = 0; row < getRows(); row++) {
				result[column*getRows() + row ] = elements[row*getColumns() + column];
			}
		}
		return result;
	}
	/**
	 * @basic
	 * @creates | result
	 */
	public double[][] getMatrix(){
		double[][] result = new double[getRows()][getColumns()];
		for(int row = 0;row<getRows();row++) {
			for(int column = 0; column<getColumns(); column++) {
				result[row][column] = elements[row*getColumns() + column];
			}
		}
		return result;
		
	}
	/**
	 * The elements of the matrix are given in row major order.
	 * @inspects| elements
	 * 
	 * 
	 * @throws | elements.length != rows*columns
	 * @post | Arrays.equals(elements, getMatrixRowMajor())
	 */
	
	public Matrix(double[] elements, int rows, int columns) {
		
		if(elements.length != rows*columns)
			throw new IllegalArgumentException("Impossible dimensions of matrix");
		
		this.rows = rows;
		this.columns = columns;
		this.elements = elements.clone();
	}
	/**
	 * Returns a scaled copy
	 * 
	 * @inspects | this
	 * @creates | result
	 * 
	 * @post | IntStream.range(0,getMatrixRowMajor().length).allMatch(i-> 
	 * 		| result.getMatrixRowMajor()[i] == scalor * getMatrixRowMajor()[i])
	 */
	
	public Matrix scaled(double scalor) {
		double[] newElements = new double[elements.length];
		for(int i=0; i< elements.length;i++) {
			newElements[i] = elements[i]*scalor;
		}
		return new Matrix(newElements, rows, columns);
	}
	/**
	 * Scales the elements of the given matrix (no copy)
	 * @mutates | this
	 * @post | IntStream.range(0, getRows()*getColumns()).allMatch(i-> getMatrixRowMajor()[i]
	 * 		 |		== old(getMatrixRowMajor())[i]*scaleFactor)
	 */
	public void scale(double scaleFactor) {
		for (int i=0; i<elements.length; i++)
			elements[i]*= scaleFactor;
	}
	
	/**
	 * @inspects | this, matrix2
	 * @creates | result
	 * @throws | getRows() != matrix2.getRows() || getColumns() != matrix2.getColumns()
	 * @post | IntStream.range(0,getMatrixRowMajor().length).allMatch(i-> 
	 * 		| result.getMatrixRowMajor()[i] == getMatrixRowMajor()[i] + matrix2.getMatrixRowMajor()[i])
	 */
	
	public Matrix plus(Matrix matrix2) {
		if (rows != matrix2.getRows() || columns != matrix2.getColumns())
			throw new IllegalArgumentException("Dimensions of matrices don't match.");
		
		
		double[] newElements = new double[elements.length];
		for(int i=0; i< elements.length;i++) {
			newElements[i] = elements[i] + matrix2.getMatrixRowMajor()[i];
		}
		
		return new Matrix(newElements, rows, columns);
		
	}
	/**
	 * 
	 * @mutates | this
	 * @inspects | other
	 * 
	 * @post | IntStream.range(0, getRows()*getColumns()).allMatch(i->
	 * 		 |		getMatrixRowMajor()[i] == old(getMatrixRowMajor())[i] + other.getMatrixRowMajor()[i])
	 */
	public void add(Matrix other) {
		for(int i=0; i< elements.length;i++)
			elements[i] += other.elements[i];
	}
	
	
	
}
