package br.uece.ctmc.bdp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.uece.ctmc.bdp.InvalidProbabilitiesException;
import br.uece.ctmc.bdp.RandomTransitionElector;
import br.uece.ctmc.bdp.RandomTransitionElector.Transition;

@RunWith(MockitoJUnitRunner.class)
public class RandomTransitionTestCase {
	
	@Mock
	private Random random;
	
	@InjectMocks
	private RandomTransitionElector elector;
	
	@Test
	public void testElection() {
		when(random.nextDouble()).thenReturn(0.449);
		assertEquals(Transition.RIGHT, elector.electTransition(0.55, 0.45));
		assertEquals(Transition.LEFT, elector.electTransition(0.45, 0.55));
		
		when(random.nextDouble()).thenReturn(0.5);
		assertEquals(Transition.RIGHT, elector.electTransition(0, 1));
		assertEquals(Transition.LEFT, elector.electTransition(1, 0));
		
		when(random.nextDouble()).thenReturn(0.0);
		assertEquals(Transition.RIGHT, elector.electTransition(0, 1));
		assertEquals(Transition.LEFT, elector.electTransition(1, 0));
		
		when(random.nextDouble()).thenReturn(0.7);
		assertEquals(Transition.RIGHT, elector.electTransition(0.5, 0.5));
	}
	
	@Test(expected = InvalidProbabilitiesException.class)
	public void testInvalidProbabilityGT() {
		elector.electTransition(0.5, 0.51);
		fail("Election should have failed due to probability sum greater than 1");
	}
	
	@Test(expected = InvalidProbabilitiesException.class)
	public void testInvalidProbabilityLT() {
		elector.electTransition(0.49, 0.5);
		fail("Election should have failed due to probability sum less than 1");
	}
	
	@Test
	public void testProbabilityTolerance() {
		elector.electTransition(0.499, 0.5);
		elector.electTransition(0.5, 0.501);
	}
}
