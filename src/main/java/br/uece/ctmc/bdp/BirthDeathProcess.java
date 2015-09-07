package br.uece.ctmc.bdp;

import java.util.Arrays;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class BirthDeathProcess {
	
	private final double[][] rateMatrix;
	private final double[][] discreteMarkovianMatrix;
	private final double[] stationaryDistribution;
	
	public BirthDeathProcess(double[][] rateMatrix) throws InvalidBirthDeathMatrix {
		validate(rateMatrix);
		
		this.rateMatrix = Arrays.copyOf(rateMatrix, rateMatrix.length);
		this.discreteMarkovianMatrix = extractDiscreteMatrix(rateMatrix);
		this.stationaryDistribution = calculateStationaryDistribution(rateMatrix);
	}
	
	private static void validate(double[][] rateMatrix) throws InvalidBirthDeathMatrix {
		int statesCount = rateMatrix.length;
		
		for (int i = 0; i < statesCount; i++) {
			if (statesCount != rateMatrix[i].length) {
				throw new InvalidBirthDeathMatrix("The given matrix isn't quadratic");
			}
		}
		
		for (int i = 0; i < statesCount; i++) {
			double[] row = rateMatrix[i];
			
			int previousState = i - 1;
			int nextState = i + 1;
			double sum = 0;
			
			for (int j = 0; j < statesCount; j++) {
				double rate = row[j];
				
				if (i != j) {
					if (rate != 0 && j != previousState && j != nextState) {
						throw new InvalidBirthDeathMatrix("BDP states only have transitions to the previous or to the next state");
					}
					
					if (rate < 0) {
						throw new InvalidBirthRate(String.format("Transition rate from state [%s] to [%s] is less than 0", i, j));
					}
					
					if (i < j && rate > rateMatrix[j][i]) {
						throw new InvalidBirthRate(String.format("Birth rate from state [%s] to [%s] is greater than the death rate", i, j));
					}
				}
				
				sum += rate;
			}
			
			if (sum != 0) {
				throw new InvalidBirthDeathMatrix("The sum of each row of the matrix must be equal to 0");
			}
		}
	}
	
	private static double[][] extractDiscreteMatrix(double[][] rateMatrix) {
		int statesCount = rateMatrix.length;
		double[][] discreteMatrix = new double[statesCount][statesCount];
		
		for (int i = 0; i < statesCount; i++) {
			double lambda = Math.abs(rateMatrix[i][i]);
			
			for (int j = 0; j < statesCount; j++) {
				double probability = 0;
				
				if (i != j) {
					probability = rateMatrix[i][j] / lambda;
				}
				
				discreteMatrix[i][j] = probability;
			}
		}
		
		return discreteMatrix;
	}
	
	private static double[] calculateStationaryDistribution(double[][] rateMatrix) {
		int statesCount = rateMatrix.length;
		
		double[][] coefficients = new double[statesCount + 1][statesCount + 1];
		double[] constants = new double[statesCount + 1];
		
		for (int i = 0; i < statesCount; i++) {
			for (int j = 0; j < statesCount; j++) {
				coefficients[j][i] = rateMatrix[i][j];
			}
			
			constants[i] = 0;
			coefficients[statesCount][i] = 1;
		}
		
		constants[statesCount] = 1;
		
		RealMatrix coefficientsMatrix = new Array2DRowRealMatrix(coefficients, false);
		RealVector constantsVector = new ArrayRealVector(constants, false);
		RealVector resultVector = new SingularValueDecomposition(coefficientsMatrix).getSolver().solve(constantsVector);
		
		double[] result = new double[statesCount];
		for (int i = 0; i < statesCount; i++) {
			result[i] = resultVector.getEntry(i);
		}
		
		return result;
	}
	
	public double[][] getRateMatrix() {
		return Arrays.copyOf(rateMatrix, rateMatrix.length);
	}
	
	public double[][] getDiscreteMarkovianMatrix() {
		return Arrays.copyOf(discreteMarkovianMatrix, discreteMarkovianMatrix.length);
	}
	
	public double[] getStationaryDistribution() {
		return Arrays.copyOf(stationaryDistribution, stationaryDistribution.length);
	}
}
