package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoordinateTest {

	@Test
	public void testEquals() {
		Coordinate one = new Coordinate(1, 3);
		assertFalse(one.equals(null));
		assertFalse(one.equals(new Integer(3)));
		assertFalse(one.equals(new Coordinate(0, 0)));
		assertFalse(one.equals(new Coordinate(1, 0)));
		assertFalse(one.equals(new Coordinate(0, 3)));
		assertTrue(one.equals(new Coordinate(1, 3)));
	}
}
