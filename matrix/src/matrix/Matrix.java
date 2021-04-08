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
	 * @invar | columns > 0
	 */
	private int columns;
	
	/**
	 * @representationObject
	 * @representationObjects
	 * Deze tag betekent dat de elementen van het representationobject ook representationobjects
	 * zijn. Hier van toepassaing aangezien we het opslagen als een lijst van lijsten
	 * @invar | elements != null
	 * @invar | Arrays.stream(elements).allMatch(row-> row != null && row.length == columns)
	 */
	private double[][] elements;

	/**
	 * @basic
	 * @immutable
	 * Door hier immutable te zetten moeten we nadien in de methodes niet meer
	 * zeggen dat de waarde van deze inspector niet wijzigt.
	 */
	
	public int getRows() {
		return elements.length;
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
		
		return elements[row][column];
	}
	/**
	 *@post | result.length == getRows()*getColumns()
	 *@creates | result
	 */
	public double[] getMatrixRowMajor() {
		double[] result = new double[getRows()*getColumns()];
		for(int column = 0; column < getColumns(); column++) {
			for ( int row = 0; row < getRows(); row++) {
				result[row*columns + column] = elements[row][column];
			}
		}
		return result;
	}
	/**
	 * @creates| result
	 * @post | result.length == getRows()*getColumns()
	 */
	
	public double[] getMatrixColumnMajor() {
		double[] result = new double[getRows()*getColumns()];
		for(int column = 0; column < getColumns(); column++) {
			for ( int row = 0; row < getRows(); row++) {
				result[column*getRows() + row ] = elements[row][column];
			}
		}
		return result;
	}
	/**
	 * @basic
	 * @creates | result
	 * BELANGRIJKE OPMERKING:
	 * hier mag je voor de implemantie niet gwn schrijven:
	 * return elements.copy
	 * Waarom? Dit neemt een "shallow copy". De klant krijgt weliswaar array die een copy is
	 * van elements (die dus niet dezelfde is als de elements in de representatie), maar
	 * deze copy is 'shallow'. De lijsten die elementen zijn van de array elements zitten wel
	 * nog in deze copy. De klant heeft dus een referentie van deze lijsten en wanneer hij deze
	 * verandert veranderen ook de lijsten in de interne representatie van de matrix.
	 */
	public double[][] getMatrix(){
		double[][] result = new double[elements.length][columns];
		for(int column = 0; column < columns; column++) {
			for ( int row = 0; row < elements.length; row++) {
				result[row][column] = elements[row][column];
			}
		}
		return result;
		// alternatieve implementatie 1:
		// return Arrays.stream(elements).map(row->row.clone()).toArray(n -> new double[n][])
		// toArray(...), deze ... zegt hoe de array aangemaakt moet worden voor n elementen.
		
		// alternatieve implementatie 2:
		// double[][] result = result.clone();
		// for (int i = 0; i<result.length; i++)
		// 		result[i] = result[i].clone();
		// return result
		
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
		
		this.columns = columns;
		this.elements = new double[rows][columns];
		for(int column = 0; column < columns; column++) {
			for ( int row = 0; row < rows; row++) {
				this.elements[row][column] = elements[row*columns + column];
			}
		}
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
		double[] elementsRowMajor = getMatrixRowMajor();
		double[] newElements = new double[elementsRowMajor.length];
		for(int i=0; i< newElements.length;i++) {
			newElements[i] = elementsRowMajor[i]*scalor;
		}
		return new Matrix(newElements,getRows(), columns);
	}
	/**
	 * Scales the elements of the given matrix (no copy)
	 * @mutates | this
	 * @post | IntStream.range(0, getRows()*getColumns()).allMatch(i-> getMatrixRowMajor()[i]
	 * 		 |		== old(getMatrixRowMajor())[i]*scaleFactor)
	 */
	public void scale(double scaleFactor) {
		for(int column = 0; column < columns; column++) {
			for ( int row = 0; row < elements.length; row++) {
				this.elements[row][column] *= scaleFactor;
			}
		}
	}
	
	/**
	 * @inspects | this, matrix2
	 * @creates | result
	 * @throws | getRows() != matrix2.getRows() || getColumns() != matrix2.getColumns()
	 * @post | IntStream.range(0,getMatrixRowMajor().length).allMatch(i-> 
	 * 		| result.getMatrixRowMajor()[i] == getMatrixRowMajor()[i] + matrix2.getMatrixRowMajor()[i])
	 */
	
	public Matrix plus(Matrix matrix2) {
		if (elements.length != matrix2.getRows() || columns != matrix2.getColumns())
			throw new IllegalArgumentException("Dimensions of matrices don't match.");
		
		double[] elementsRowMajor = getMatrixRowMajor();
		double[] elementsRowMajor2 = matrix2.getMatrixRowMajor();
		double[] newElements = new double[elementsRowMajor.length];
		for(int i=0; i< newElements.length;i++) {
			newElements[i] = elementsRowMajor[i] + elementsRowMajor2[i];
		}
		
		return new Matrix(newElements, elements.length, columns);
		
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
		for(int column = 0; column < columns; column++) {
			for ( int row = 0; row < elements.length; row++) {
				this.elements[row][column] += other.getElementAt(row, column);
			}
		}
	}
	
	
}
