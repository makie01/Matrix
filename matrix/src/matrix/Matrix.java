package matrix;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Each instance of this class represents a matrix.
 * @invar | getRows() > 0
 * @invar | getColumns() > 0
 * 
 * @immutable
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
	 * Je zou deze eig ook niet perse als basic moeten beschouwen, want je kan het
	 * resultaat ook definieren adhv getMatrix()
	 * @post | result == getMatrix().length
	 */
	
	public int getRows() {
		return rows;
	}
	/**
	 * @basic
	 * Je zou hier (en ook bij getRows() nog een post conditie kunnen toevoegen die zegt
	 * dat het resultaat >= 0 is, maar dit is dus een abstracte toestandsinvariant en die 
	 * heb je al gespecifieerd bovenaan.
	 */
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
		
		return getMatrixRowMajor()[(row-1)*getColumns() + column -1];
	}
	/**
	 *@post | result != null
	 *@post | result.length == getRows()*getColumns()
	 *@post | IntStream.range(0, getRows()).allMatch(rowIndex ->
	 *		| 	IntStream.range(0, getColumns()).allMatch( columnIndex ->
	 *		|		result[rowIndex*getColumns() + columnIndex] == 
	 *		|			getMatrix()[rowIndex][columnIndex]))
	 *
	 *@creates | result
	 */
	public double[] getMatrixRowMajor() {
		double[] result = new double[elements.length];
		for(int column = 0; column < columns; column++) {
			for ( int row = 0; row < rows; row++) {
				result[row*columns + column] = elements[column*rows + row];
			}
		}
		return result;
	}
	/**
	 * @creates| result
	 * @post | result.length == getRows()*getColumns()
	 */
	
	public double[] getMatrixColumnMajor() {
		return elements.clone();
	}
	/**
	 * @basic
	 * @creates | result, ...result
	 * Er staat hier result, ... result
	 * Dit is om aan te duiden dat niet enkel de overkoepelende array een nieuw object is,
	 * maar ook de elementen (de geneste arrays) nieuwe objecten zijn.
	 * @post | result != null
	 * @post | Arrays.stream(result).allMatch(row -> row!= null && row.length == getColumns())
	 * 
	 * Bij basic inspectoren maak je best geen gebruik van andere inspectoren, anders kan je
	 * in de documentatie in een oneindige lus terecht komen. Als je bv ipv rows getRows()
	 * gebruikt zal deze oproep de documentatie van getRows() checken (de post conditie),
	 * maar deze maakt gebruik van getMatrix() enzovoort...
	 * 
	 */
	public double[][] getMatrix(){
		double[][] result = new double[rows][columns];
		for(int row = 0;row<rows;row++) {
			for(int column = 0; column<columns; column++) {
				result[row][column] = elements[column*rows + row];
			}
		}
		return result;
		
	}
	/**
	 * Initializes this object so that it represents a matrix with the given number
	 * of rows and columns and the given elements.
	 * 
	 * @param elements The elements for the array, in row major order.
	 * @throws | rows <0
	 * @throws | columns <0
	 * @throws | elements == null
	 * @throws | elements.length != rows*columns
	 * @throws | rows == 0 ? true : Integer.MAX_VALUE/rows < columns
	 * Deze laatste throws is om rekening te houden met arithmetic overflow (zie notities)
	 * Bovendien wordt er gebruik gemaakt van de volgende vorm:
	 * conditie? waar-waarde : onwaar-waarde
	 * is eigenlijk een soort van if constructie equivalent aan de volgende:
	 * if (conditie) waar-waarde else onwaar-waarde
	 * 
	 * @post | getRows() == rows
	 * @post | getColumns() == columns
	 * @post | Arrays.equals(elements, getMatrixRowMajor())
	 */
	
	public Matrix(double[] elements, int rows, int columns) {
		if(elements == null)
			throw new IllegalArgumentException("Elements can not be a null pointer");
		
		if(elements.length != rows*columns || rows <0 || columns<0)
			throw new IllegalArgumentException("Impossible dimensions of matrix");
		
		this.rows = rows;
		this.columns = columns;
		this.elements = new double[rows*columns];
		for (int row = 0; row < getRows(); row++)
			for (int column = 0; column < getColumns(); column++)
				this.elements[column*getRows() + row] = elements[row*getColumns()+column];
	}
	/**
	 * Deze private constructor die elements in column major order accepteert even toegevoegd.
	 * Deze is handig voor de implementatie van scaled en plus. Je kan meerdere constructoren
	 * hebben, maar dan moet je er wel voor zorgen dat eclipse weet welke van de 2 je oproept.
	 * Daarom als laatste argument Void dummy toegevoegd. Dit kan enkel als waarde null hebben
	 * en dus als we nu Matrix(..,..,..,null) geven weet eclipse dat we deze constructor willen
	 * en niet de publieke. Zonder dit argument zouden we 2 constructors hebben met elks 3 
	 * argumenten die hetzelfde type zijn en kan eclipse dus onmogelijk weten welke van de 2
	 * we nu oproepen. Zie methode plus voor voorbeeld oproep van deze private constructor
	 */
	
	private Matrix(int nbRows, int nbColumns, double[] elementsColumnMajor, Void dummy) {
		this.rows = nbRows;
		this.columns = nbColumns;
		this.elements = elementsColumnMajor.clone();
	}
	
	/**
	 * Returns a copy of this matrix where each element has been multiplied by the given scale
	 * factor
	 * 
	 * @post | result != null
	 * @post | result.getRows() == getRows()
	 * @post | result.getColumns() == getColumns()
	 * @post | IntStream.range(0,getMatrixRowMajor().length).allMatch(i-> 
	 * 		| result.getMatrixRowMajor()[i] == scalor * getMatrixRowMajor()[i])
	 * 
	 */
	public Matrix scaled(double scalor) {
		double[] newElements = new double[elements.length];
		for (int row = 0; row < getRows(); row++)
			for (int column = 0; column < getColumns(); column++)
				newElements[row*getColumns()+column] = scalor*elements[column*getRows() + row];
		return new Matrix(newElements, rows, columns);
	}
	
	/**
	 * @pre | matrix2 != null
	 * @throws | getRows() != matrix2.getRows() || getColumns() != matrix2.getColumns()
	 * (even niet op letten dat hier nu throws of pre staat)
	 * 
	 * @post | result != null
	 * @post | result.getRows() == getRows()
	 * @post | result.getColumns() == getColumns()
	 * @post | IntStream.range(0,getMatrixRowMajor().length).allMatch(i-> 
	 * 		| result.getMatrixRowMajor()[i] == getMatrixRowMajor()[i] + matrix2.getMatrixRowMajor()[i])
	 */
	
	public Matrix plus(Matrix matrix2) {
		if (rows != matrix2.getRows() || columns != matrix2.getColumns())
			throw new IllegalArgumentException("Dimensions of matrices don't match.");
		
		
		double[] newElements = new double[elements.length];
		for(int i=0; i< elements.length;i++) {
			newElements[i] = elements[i] + matrix2.getMatrixColumnMajor()[i];
		}
		
		return new Matrix(rows, columns,newElements, null);
		
	}
	
}
