package matrixtest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import matrix.Matrix;

class MatrixTest {

	@Test
	void test() {
		Matrix mymatrix = new Matrix(new double[] {1,2,4,3,4,2,9,0,1}, 3, 3);
		double[] rowmajor = mymatrix.getMatrixRowMajor();
		
		assertArrayEquals(new double[] {1,2,4,3,4,2,9,0,1}, rowmajor);
		double[] columnmajor = mymatrix.getMatrixColumnMajor();
		assertArrayEquals(new double[] {1,3,9,2,4,0,4,2,1}, columnmajor);
		
		double[][] matrixArray = mymatrix.getMatrix();
		assertArrayEquals(new double[][] {{1,2,4},{3,4,2},{9,0,1}},matrixArray);
		
		rowmajor[3] = 7;
		assertEquals(3,mymatrix.getElementAt(2, 1));
		
		assertEquals(3, mymatrix.getColumns());
		assertEquals(3, mymatrix.getRows());
		
		Matrix scaledmatrix = mymatrix.scaled(2);
		assertArrayEquals(new double[] {2,4,8,6,8,4,18,0,2}, scaledmatrix.getMatrixRowMajor());		
		Matrix mymatrix2 = new Matrix(new double[] {1,1,1,1,1,1,1,1,1},3,3);
		
		Matrix summatrix = mymatrix.plus(mymatrix2);
		assertArrayEquals(new double[] {2,3,5,4,5,3,10,1,2}, summatrix.getMatrixRowMajor());
		

		
		
		
	}

}
