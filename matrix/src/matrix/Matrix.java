package matrix;

import java.util.Arrays;
/**
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
	 * @invar | elements.length == rows*columns
	 */
	private double[] elements;

	
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	/**
	 * @throws | row <= 0 || row > getRows() || column <=0 || column > getColumns()
	 * @post | result == getMatrixRowMajor()[(row-1)*getColumns() + column -1]
	 */
	
	public double getElementAt(int row, int column) {
		if(row <= 0 || row > getRows() || column <=0 || column > getColumns())
			throw new IllegalArgumentException("Unvalid row or column");
		
		return elements[(row-1)*getColumns() + column -1];
	}
	
	public double[] getMatrixRowMajor() {
		return elements.clone();
	}
	/**
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
	 * @post | result.length == getRows()
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
	
}
