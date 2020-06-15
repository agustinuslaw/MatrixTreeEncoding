package matrixtree.model;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.ArithmeticUtils;

/**
 * Adapted from {@link Fraction}, but uses long instead of int to accomodate
 * larger number but without resorting to {@link BigInteger} as in
 * {@link BigFraction}.
 * 
 * @author agustinus lawandy
 */
public class Rational extends Number implements Comparable<Rational>, Serializable {

	private static final long serialVersionUID = 8840749863385350219L;

	/** A fraction representing "0". */
	public static final Rational ZERO = new Rational(0, 1);

	/** The denominator. */
	private final long denominator;

	/** The numerator. */
	private final long numerator;

	/**
	 * Create a fraction from an int. The fraction is num / 1.
	 * 
	 * @param num the numerator.
	 */
	public Rational(long num) {
		this(num, 1);
	}

	/**
	 * Create a fraction given the numerator and denominator. The fraction is
	 * reduced to lowest terms.
	 * 
	 * @param num the numerator.
	 * @param den the denominator.
	 * @throws MathArithmeticException if the denominator is {@code zero}
	 */
	public Rational(long num, long den) {
		if (den == 0) {
			throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, num, den);
		}
		if (den < 0) {
			if (num == Integer.MIN_VALUE || den == Integer.MIN_VALUE) {
				throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, num, den);
			}
			num = -num;
			den = -den;
		}
		// reduce numerator and denominator by greatest common denominator.
		final long d = ArithmeticUtils.gcd(num, den);
		if (d > 1) {
			num /= d;
			den /= d;
		}

		// move sign to numerator.
		if (den < 0) {
			num = -num;
			den = -den;
		}
		this.numerator = num;
		this.denominator = den;
	}

	/**
	 * Returns the absolute value of this fraction.
	 * 
	 * @return the absolute value.
	 */
	public Rational abs() {
		Rational ret;
		if (numerator >= 0) {
			ret = this;
		} else {
			ret = negate();
		}
		return ret;
	}

	/**
	 * Compares this object to another based on size.
	 * 
	 * @param object the object to compare to
	 * @return -1 if this is less than {@code object}, +1 if this is greater than
	 *         {@code object}, 0 if they are equal.
	 */
	@Override
	public int compareTo(Rational object) {
		long nOd = numerator * object.denominator;
		long dOn = denominator * object.numerator;
		return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
	}

	/**
	 * Gets the fraction as a {@code double}. This calculates the fraction as the
	 * numerator divided by denominator.
	 * 
	 * @return the fraction as a {@code double}
	 */
	@Override
	public double doubleValue() {
		return (double) numerator / (double) denominator;
	}

	/**
	 * Test for the equality of two fractions. If the lowest term numerator and
	 * denominators are the same for both fractions, the two fractions are
	 * considered to be equal.
	 * 
	 * @param other fraction to test for equality to this fraction
	 * @return true if two fractions are equal, false if object is {@code null}, not
	 *         an instance of {@link Rational}, or not equal to this fraction
	 *         instance.
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other instanceof Rational) {
			// since fractions are always in lowest terms, numerators and
			// denominators can be compared directly for equality.
			Rational rhs = (Rational) other;
			return (numerator == rhs.numerator) && (denominator == rhs.denominator);
		}
		return false;
	}

	/**
	 * Gets the fraction as a {@code float}. This calculates the fraction as the
	 * numerator divided by denominator.
	 * 
	 * @return the fraction as a {@code float}
	 */
	@Override
	public float floatValue() {
		return (float) doubleValue();
	}

	/**
	 * Access the denominator.
	 * 
	 * @return the denominator.
	 */
	public long getDenominator() {
		return denominator;
	}

	/**
	 * Access the numerator.
	 * 
	 * @return the numerator.
	 */
	public long getNumerator() {
		return numerator;
	}

	/**
	 * Gets a hashCode for the fraction.
	 * 
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode() {
		return (int) (37 * (37 * 17 + numerator) + denominator);
	}

	/**
	 * Gets the fraction as an {@code int}. This returns the whole number part of
	 * the fraction.
	 * 
	 * @return the whole number fraction part
	 */
	@Override
	public int intValue() {
		return (int) doubleValue();
	}

	/**
	 * Gets the fraction as a {@code long}. This returns the whole number part of
	 * the fraction.
	 * 
	 * @return the whole number fraction part
	 */
	@Override
	public long longValue() {
		return (long) doubleValue();
	}

	/**
	 * Return the additive inverse of this fraction.
	 * 
	 * @return the negation of this fraction.
	 */
	public Rational negate() {
		if (numerator == Integer.MIN_VALUE) {
			throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, numerator, denominator);
		}
		return new Rational(-numerator, denominator);
	}

	/**
	 * Return the multiplicative inverse of this fraction.
	 * 
	 * @return the reciprocal fraction
	 */
	public Rational reciprocal() {
		return new Rational(denominator, numerator);
	}

	/**
	 * <p>
	 * Adds the value of this fraction to another, returning the result in reduced
	 * form. The algorithm follows Knuth, 4.5.1.
	 * </p>
	 *
	 * @param fraction the fraction to add, must not be {@code null}
	 * @return a {@code RationalNumber} instance with the resulting values
	 * @throws NullArgumentException   if the fraction is {@code null}
	 * @throws MathArithmeticException if the resulting numerator or denominator
	 *                                 exceeds {@code Integer.MAX_VALUE}
	 */
	public Rational add(Rational fraction) {
		return addSub(fraction, true /* add */);
	}

	/**
	 * Add an integer to the fraction.
	 * 
	 * @param i the {@code integer} to add.
	 * @return this + i
	 */
	public Rational add(final long i) {
		return new Rational(numerator + i * denominator, denominator);
	}

	/**
	 * <p>
	 * Subtracts the value of another fraction from the value of this one, returning
	 * the result in reduced form.
	 * </p>
	 *
	 * @param fraction the fraction to subtract, must not be {@code null}
	 * @return a {@code RationalNumber} instance with the resulting values
	 * @throws NullArgumentException   if the fraction is {@code null}
	 * @throws MathArithmeticException if the resulting numerator or denominator
	 *                                 cannot be represented in an {@code int}.
	 */
	public Rational subtract(Rational fraction) {
		return addSub(fraction, false /* subtract */);
	}

	/**
	 * Subtract an integer from the fraction.
	 * 
	 * @param i the {@code integer} to subtract.
	 * @return this - i
	 */
	public Rational subtract(final long i) {
		return new Rational(numerator - i * denominator, denominator);
	}

	/**
	 * Implement add and subtract using algorithm described in Knuth 4.5.1.
	 *
	 * @param fraction the fraction to subtract, must not be {@code null}
	 * @param isAdd    true to add, false to subtract
	 * @return a {@code RationalNumber} instance with the resulting values
	 * @throws NullArgumentException   if the fraction is {@code null}
	 * @throws MathArithmeticException if the resulting numerator or denominator
	 *                                 cannot be represented in an {@code int}.
	 */
	private Rational addSub(Rational fraction, boolean isAdd) {
		if (fraction == null) {
			throw new NullArgumentException(LocalizedFormats.FRACTION);
		}
		// zero is identity for addition.
		if (numerator == 0) {
			return isAdd ? fraction : fraction.negate();
		}
		if (fraction.numerator == 0) {
			return this;
		}
		// if denominators are randomly distributed, d1 will be 1 about 61%
		// of the time.
		long d1 = ArithmeticUtils.gcd(denominator, fraction.denominator);
		if (d1 == 1) {
			// result is ( (u*v' +/- u'v) / u'v')
			long uvp = ArithmeticUtils.mulAndCheck(numerator, fraction.denominator);
			long upv = ArithmeticUtils.mulAndCheck(fraction.numerator, denominator);
			return new Rational(isAdd ? ArithmeticUtils.addAndCheck(uvp, upv) : ArithmeticUtils.subAndCheck(uvp, upv),
					ArithmeticUtils.mulAndCheck(denominator, fraction.denominator));
		}
		// the quantity 't' requires 65 bits of precision; see knuth 4.5.1
		// exercise 7. we're going to use a BigInteger.
		// t = u(v'/d1) +/- v(u'/d1)
		BigInteger uvp = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(fraction.denominator / d1));
		BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf(denominator / d1));
		BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
		// but d2 doesn't need extra precision because
		// d2 = gcd(t,d1) = gcd(t mod d1, d1)
		long tmodd1 = t.mod(BigInteger.valueOf(d1)).longValue();
		long d2 = (tmodd1 == 0) ? d1 : ArithmeticUtils.gcd(tmodd1, d1);

		// result is (t/d2) / (u'/d1)(v'/d2)
		BigInteger w = t.divide(BigInteger.valueOf(d2));
		if (w.bitLength() > 31) {
			throw new MathArithmeticException(LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY, w);
		}
		return new Rational(w.longValue(), ArithmeticUtils.mulAndCheck(denominator / d1, fraction.denominator / d2));
	}

	/**
	 * <p>
	 * Multiplies the value of this fraction by another, returning the result in
	 * reduced form.
	 * </p>
	 *
	 * @param fraction the fraction to multiply by, must not be {@code null}
	 * @return a {@code RationalNumber} instance with the resulting values
	 * @throws NullArgumentException   if the fraction is {@code null}
	 * @throws MathArithmeticException if the resulting numerator or denominator
	 *                                 exceeds {@code Integer.MAX_VALUE}
	 */
	public Rational multiply(Rational fraction) {
		if (fraction == null) {
			throw new NullArgumentException(LocalizedFormats.FRACTION);
		}
		if (numerator == 0 || fraction.numerator == 0) {
			return ZERO;
		}
		// knuth 4.5.1
		// make sure we don't overflow unless the result *must* overflow.
		long d1 = ArithmeticUtils.gcd(numerator, fraction.denominator);
		long d2 = ArithmeticUtils.gcd(fraction.numerator, denominator);
		return getReducedRationalNumber(ArithmeticUtils.mulAndCheck(numerator / d1, fraction.numerator / d2),
				ArithmeticUtils.mulAndCheck(denominator / d2, fraction.denominator / d1));
	}

	/**
	 * Multiply the fraction by an integer.
	 * 
	 * @param i the {@code integer} to multiply by.
	 * @return this * i
	 */
	public Rational multiply(final long i) {
		return multiply(new Rational(i));
	}

	/**
	 * <p>
	 * Divide the value of this fraction by another.
	 * </p>
	 *
	 * @param fraction the fraction to divide by, must not be {@code null}
	 * @return a {@code RationalNumber} instance with the resulting values
	 * @throws IllegalArgumentException if the fraction is {@code null}
	 * @throws MathArithmeticException  if the fraction to divide by is zero
	 * @throws MathArithmeticException  if the resulting numerator or denominator
	 *                                  exceeds {@code Integer.MAX_VALUE}
	 */
	public Rational divide(Rational fraction) {
		if (fraction == null) {
			throw new NullArgumentException(LocalizedFormats.FRACTION);
		}
		if (fraction.numerator == 0) {
			throw new MathArithmeticException(LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY, fraction.numerator,
					fraction.denominator);
		}
		return multiply(fraction.reciprocal());
	}

	/**
	 * Divide the fraction by an integer.
	 * 
	 * @param i the {@code integer} to divide by.
	 * @return this * i
	 */
	public Rational divide(final long i) {
		return divide(new Rational(i));
	}

	/**
	 * <p>
	 * Gets the fraction percentage as a {@code double}. This calculates the
	 * fraction as the numerator divided by denominator multiplied by 100.
	 * </p>
	 *
	 * @return the fraction percentage as a {@code double}.
	 */
	public double percentageValue() {
		return 100 * doubleValue();
	}

	/**
	 * <p>
	 * Creates a {@code RationalNumber} instance with the 2 parts of a fraction Y/Z.
	 * </p>
	 *
	 * <p>
	 * Any negative signs are resolved to be on the numerator.
	 * </p>
	 *
	 * @param numerator   the numerator, for example the three in 'three sevenths'
	 * @param denominator the denominator, for example the seven in 'three sevenths'
	 * @return a new fraction instance, with the numerator and denominator reduced
	 * @throws MathArithmeticException if the denominator is {@code zero}
	 */
	public static Rational getReducedRationalNumber(long numerator, long denominator) {
		if (denominator == 0) {
			throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, numerator, denominator);
		}
		if (numerator == 0) {
			return ZERO; // normalize zero.
		}
		// allow 2^k/-2^31 as a valid fraction (where k>0)
		if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
			numerator /= 2;
			denominator /= 2;
		}
		if (denominator < 0) {
			if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
				throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, numerator, denominator);
			}
			numerator = -numerator;
			denominator = -denominator;
		}
		// simplify fraction.
		long gcd = ArithmeticUtils.gcd(numerator, denominator);
		numerator /= gcd;
		denominator /= gcd;
		return new Rational(numerator, denominator);
	}

	/**
	 * <p>
	 * Returns the {@code String} representing this fraction, ie "num / dem" or just
	 * "num" if the denominator is one.
	 * </p>
	 *
	 * @return a string representation of the fraction.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = null;
		if (denominator == 1) {
			str = Long.toString(numerator);
		} else if (numerator == 0) {
			str = "0";
		} else {
			str = numerator + " / " + denominator;
		}
		return str;
	}

}
