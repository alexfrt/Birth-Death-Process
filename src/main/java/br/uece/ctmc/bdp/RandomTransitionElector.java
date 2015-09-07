package br.uece.ctmc.bdp;

import java.util.Random;

import org.apache.commons.math3.util.Precision;

public class RandomTransitionElector {
	
	public enum Transition {
		RIGHT,
		LEFT
	}
	
	private Random random;
	
	public RandomTransitionElector() {
		this.random = new Random();
	}
	
	public Transition electTransition(double leftProbability, double rightProbability) throws InvalidProbabilitiesException {
		double sum = leftProbability + rightProbability;
		if (!Precision.equalsWithRelativeTolerance(1.0, sum, 0.0011)) {
			throw new InvalidProbabilitiesException(String.format("Probability sum must be equal to 1, but it was [%.2f]", sum));
		}
		
		Transition smaller = leftProbability <= rightProbability ? Transition.LEFT : Transition.RIGHT;
		Transition greater = leftProbability <= rightProbability ? Transition.RIGHT : Transition.LEFT;
		
		double randomValue = random.nextDouble();
		double valueToCompare = smaller.equals(Transition.LEFT) ? leftProbability : rightProbability;
		
		return randomValue < valueToCompare ? smaller : greater;
	}
	
}
