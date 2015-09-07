package br.uece.ctmc.bdp;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BirthDeathProcessWalkSimulatorTestCase {
	
	@Test
	public void testSimpleWalk() throws Exception {
		BirthDeathProcess birthDeathProcess = BirthDeathProcessBuilder.buildHomogeneousBirthDeathProcess(2, 2, 3);
		BirthDeathProcessWalkSimulator simulator = new BirthDeathProcessWalkSimulator(birthDeathProcess);
		
		List<Integer> walk = simulator.walk(10);
		
		assertThat(walk, is(Arrays.asList(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)));
	}
	
	@Test
	public void testHopEquality() throws Exception {
		BirthDeathProcess birthDeathProcess = BirthDeathProcessBuilder.buildHomogeneousBirthDeathProcess(100, 4, 10);
		BirthDeathProcessWalkSimulator simulator = new BirthDeathProcessWalkSimulator(birthDeathProcess);
		
		List<Integer> firstWalk = simulator.walk(10);
		assertThat(firstWalk, is(simulator.walk(10)));
		
		List<Integer> secondWalk = simulator.walk(15);
		assertThat(secondWalk, is(simulator.walk(15)));
		
		assertThat(firstWalk, is(secondWalk.subList(0, 10)));
	}

}
