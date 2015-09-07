package br.uece.ctmc.bdp;

import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import br.uece.ctmc.bdp.util.MatrixAssert;

@RunWith(MockitoJUnitRunner.class)
public class BirthDeathProcessValidationTestCase {
	
	//Validation tests...
	
	@Test(expected = InvalidBirthDeathMatrix.class)
	public void testDiscreteMatrix() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] {{0, 1}, {1, 0}});
	}
	
	@Test(expected = InvalidBirthDeathMatrix.class)
	public void testInvalidRateMatrix() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] {{0, 1}, {1, 1}});
	}
	
	@Test(expected = InvalidBirthDeathMatrix.class)
	public void testNonQuadraticMatrix() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] {{0, 0}, {0, 0}, {0}});
	}
	
	@Test(expected = InvalidBirthDeathMatrix.class)
	public void testNonBirthDeathMatrix() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] {{-2, 1, 1}, {1, -2, 1}, {0, 1, -1}});
	}
	
	@Test(expected = InvalidBirthRateException.class)
	public void testInvalidBirthRate() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] {{4, -4}, {2, -2}});
	}
	
	@Test(expected = InvalidBirthRateException.class)
	public void testInvalidBirthRateWith2Stages() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] {{-4, 4}, {2, -2}});
	}
	
	@Test(expected = InvalidBirthRateException.class)
	public void testInvalidBirthRateWith3Stages() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] { {-2, 2, 0}, {1, -3, 2}, {0, 1, -1} });
	}
	
	@Test(expected = InvalidBirthRateException.class)
	public void testInvalidBirthRateWith4Stages() throws InvalidBirthDeathMatrix {
		new BirthDeathProcess(new double[][] { {-2, 2, 0, 0}, {1, -3, 2, 0}, {0, 1, -3, 2}, {0, 0, 1, -1} });
	}
	
	//Discrete matrix test...
	
	@Test
	public void testDiscreteMatrixProcessing() throws Exception {
		BirthDeathProcess birthDeathProcess = new BirthDeathProcess(new double[][] {{-2, 2}, {3, -3}});
		MatrixAssert.assertMatrix(new double[][] { {0, 1}, {1, 0} }, birthDeathProcess.getDiscreteMarkovianMatrix());
		
		birthDeathProcess = new BirthDeathProcess(new double[][] {{-2, 2, 0}, {2, -4, 2}, {0, 2, -2}});
		MatrixAssert.assertMatrix(new double[][] { {0, 1, 0}, {0.5, 0, 0.5}, {0, 1, 0} }, birthDeathProcess.getDiscreteMarkovianMatrix());
		
		birthDeathProcess = new BirthDeathProcess(new double[][] { {-2, 2, 0, 0}, {2, -4, 2, 0}, {0, 2, -4, 2}, {0, 0, 2, -2} });
		MatrixAssert.assertMatrix(new double[][] { {0, 1, 0, 0}, {0.5, 0, 0.5, 0}, {0, 0.5, 0, 0.5}, {0, 0, 1, 0} }, birthDeathProcess.getDiscreteMarkovianMatrix());
	}

	//Stationary distribution test...
	
	@Test
	public void testStationaryDistribution() throws Exception {
		BirthDeathProcess birthDeathProcess = new BirthDeathProcess(new double[][] {{-2, 2, 0}, {2, -4, 2}, {0, 2, -2}});
		double[] distribution = birthDeathProcess.getStationaryDistribution();
		
		for (double value : distribution) {
			assertTrue(Precision.equalsWithRelativeTolerance(0.3333, value, 0.001));
		}
		
		birthDeathProcess = new BirthDeathProcess(new double[][] {{-3, 3, 0, 0}, {4, -7, 3, 0}, {0, 4, -7, 3}, {0, 0, 4, -4}});
		distribution = birthDeathProcess.getStationaryDistribution();
		
		assertTrue(Precision.equalsWithRelativeTolerance(0.365, distribution[0], 0.01));
		assertTrue(Precision.equalsWithRelativeTolerance(0.274, distribution[1], 0.01));
		assertTrue(Precision.equalsWithRelativeTolerance(0.205, distribution[2], 0.01));
		assertTrue(Precision.equalsWithRelativeTolerance(0.153, distribution[3], 0.01));
	}
	
}
