package br.uece.ctmc.bdp;

import org.junit.Test;

import br.uece.ctmc.bdp.util.MatrixAssert;

public class BirthDeathProcessBuilderTestCase {
	
	//Validation tests...
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRate() {
		BirthDeathProcessBuilder.buildHomogeneousMatrix(2, 0, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNoState() {
		BirthDeathProcessBuilder.buildHomogeneousMatrix(0, 1, 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSingleState() {
		BirthDeathProcessBuilder.buildHomogeneousMatrix(1, 1, 1);
	}
	
	//Matrix build test...
	
	@Test
	public void testWith2States() {
		double[][] processMatrix = BirthDeathProcessBuilder.buildHomogeneousMatrix(2, 2, 4);
		double[][] expectedMatrix = new double[][] {{-2, 2}, {4, -4}};
		
		MatrixAssert.assertMatrix(expectedMatrix, processMatrix);
	}
	
	@Test
	public void testWith5States() {
		double[][] processMatrix = BirthDeathProcessBuilder.buildHomogeneousMatrix(5, 2, 3);
		double[][] expectedMatrix = new double[][] {
			{-2, 2, 0, 0, 0},
			{3, -5, 2, 0, 0},
			{0, 3, -5, 2, 0}, 
			{0, 0, 3, -5, 2}, 
			{0, 0, 0, 3, -3}
		};
		
		MatrixAssert.assertMatrix(expectedMatrix, processMatrix);
	}
	
	//BDP tests...
	
	@Test
	public void testBDP() {
		double[][] expectedMatrix = new double[][] {
			{-2, 2, 0, 0, 0},
			{3, -5, 2, 0, 0},
			{0, 3, -5, 2, 0}, 
			{0, 0, 3, -5, 2}, 
			{0, 0, 0, 3, -3}
		};
		
		BirthDeathProcess birthDeathProcess = BirthDeathProcessBuilder.buildHomogeneousBirthDeathProcess(5, 2, 3);
		
		MatrixAssert.assertMatrix(expectedMatrix, birthDeathProcess.getRateMatrix());
	}

}
