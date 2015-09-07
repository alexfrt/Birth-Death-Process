package br.uece.ctmc.bdp;

public final class BirthDeathProcessBuilder {
	
	private BirthDeathProcessBuilder() {}
	
	public static final double[][] buildHomogeneousMatrix(int states, double lambda, double mu) {
		if (states < 2) {
			throw new IllegalArgumentException("It must have at least 2 states");
		}
		
		if (lambda <= 0 || mu <= 0) {
			throw new IllegalArgumentException("The lambda and mu values must be greater than 0");
		}
		
		if (lambda > mu) {
			throw new IllegalArgumentException("The lambda must be less than or equal to mu");
		}
		
		double[][] matrix = new double[states][states];
		
		for (int i = 0; i < states; i++) {
			double sum = 0d;
			int previous = i - 1;
			int next = i + 1;
			
			if (previous >= 0) {
				matrix[i][previous] = mu;
				sum += mu;
			}
			
			if (next < states) {
				matrix[i][next] = lambda;
				sum += lambda;
			}
			
			matrix[i][i] = -sum;
		}
		
		return matrix;
	}
	
	public static final BirthDeathProcess buildHomogeneousBirthDeathProcess(int states, double lambda, double mu) {
		try {
			double[][] matrix = buildHomogeneousMatrix(states, lambda, mu);
			return new BirthDeathProcess(matrix);
		}
		catch (InvalidBirthDeathMatrix e) {
			throw new RuntimeException("Unexpected exception when building a Birth-Death Process", e);
		}
	}
	
}
