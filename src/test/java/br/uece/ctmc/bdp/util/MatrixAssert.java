package br.uece.ctmc.bdp.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.ArrayUtils;

public final class MatrixAssert {
	
	private MatrixAssert() {}
	
	public static void assertMatrix(double[][] expected, double[][] actual) {
		assertEquals(expected.length, actual.length);
		
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(ArrayUtils.toObject(expected[i]), ArrayUtils.toObject(actual[i]));
		}
	}

}
