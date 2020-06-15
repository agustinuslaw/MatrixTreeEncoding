package matrixtree.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import matrixtree.model.Rational;

class RationalTest {

	@Test
	void testWhole() {
		Rational m = new Rational(2);
		Rational n = new Rational(0);
		
		assertEquals(new Rational(2), m.add(n));
		assertEquals(new Rational(0), m.multiply(n));
		assertEquals(new Rational(0), n.divide(m));
		assertEquals(2, m.intValue());
		assertEquals(0, n.intValue());
	}

	@Test
	void testAlias() {
		Rational m = new Rational(1,2);
		Rational m_alias = new Rational(2,4);
		
		assertEquals(m_alias,m);
	}
	
	@Test
	void testInverse() {
		Rational m = new Rational(3,7);
		Rational n = new Rational(7,3);
		
		assertEquals(new Rational(1),m.multiply(n));
	}
}
