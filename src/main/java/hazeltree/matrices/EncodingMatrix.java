package hazeltree.matrices;

import hazeltree.model.Rational;

public class EncodingMatrix extends BaseMatrix implements SimpleInvertible {

	public EncodingMatrix(long num, long den, long snum, long sden) {
		// a11, a12, a21, a22
		super(num, snum, den, sden);
	}

	public long getNumerator() {
		return e11;
	}

	public long getDenominator() {
		return e21;
	}

	public long getSiblingNumerator() {
		return e12;
	}

	public long getSiblingDenominator() {
		return e22;
	}

	public Rational getNodeEncoding() {
		return new Rational(getNumerator(), getDenominator());
	}

	public Rational getLowerBound() {
		return getNodeEncoding();
	}

	public Rational getSiblingNodeEncoding() {
		return new Rational(getSiblingNumerator(), getSiblingDenominator());
	}

	public Rational getUpperBound() {
		return getSiblingNodeEncoding();
	}

	/**
	 * Determinant is always -1 because <br>
	 * Encoding Matrix = Origin Matrix * Node Matrices... <br>
	 * det = -1 * ( 1 * 1 * ...) = -1
	 */
	@Override
	public long determinant() {
		return -1;
	}

	@Override
	public HazelMatrix invert() {
		// remember det = -1
		// inv = -d(a22) b(a12) c(a21) -a(a11)
		return new BaseMatrix(-e22, e12, e21, -e11);
	}
}
