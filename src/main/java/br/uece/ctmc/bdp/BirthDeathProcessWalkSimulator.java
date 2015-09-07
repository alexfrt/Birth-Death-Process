package br.uece.ctmc.bdp;

import java.util.ArrayList;
import java.util.List;

import br.uece.ctmc.bdp.RandomTransitionElector.Transition;

public class BirthDeathProcessWalkSimulator {
	
	private final double[][] transitionMatrix;
	private final int statesCount;
	
	private int currentState;
	private final List<Integer> takenPath;
	private final RandomTransitionElector transitionElector;
	
	public BirthDeathProcessWalkSimulator(BirthDeathProcess birthDeathProcess) {
		this.transitionMatrix = birthDeathProcess.getDiscreteMarkovianMatrix();
		this.statesCount = transitionMatrix.length;
		
		this.takenPath = new ArrayList<>();
		this.transitionElector = new RandomTransitionElector();
		
		this.takenPath.add(currentState);
	}
	
	public List<Integer> walk(int hops) {
		List<Integer> takenPath;
		
		if (this.takenPath.size() < hops) {
			for (int i = this.takenPath.size(); i < hops; i++) {
				this.currentState = doWalk();
				this.takenPath.add(currentState);
			}
			
			takenPath = new ArrayList<>(this.takenPath);
		}
		else {
			takenPath = new ArrayList<>(this.takenPath.subList(0, hops));
		}
		
		return takenPath;
	}
	
	private int doWalk() {
		double forwardProbability = currentState == statesCount - 1 ? 0 : transitionMatrix[currentState][currentState + 1];
		double backwardProbability = currentState == 0 ? 0 : transitionMatrix[currentState][currentState -1];
		
		Transition transition = transitionElector.electTransition(backwardProbability, forwardProbability);
		return currentState + (Transition.LEFT.equals(transition) ? -1 : 1);
	}

}
