package matrixtree.matrices;

import matrixtree.model.Rational;

public class HazelEncodingMatrix extends BaseMatrix implements EncodingMatrix, TreeMatrix, SimpleInvertible {

	public HazelEncodingMatrix(long num, long den, long snum, long sden) {
		// a11, a12, a21, a22
		super(num, snum, den, sden);
	}

	public HazelEncodingMatrix(TreeMatrix other) {
		super(other);
	}

	public long getNumerator() {
		return getE11();
	}

	public long getDenominator() {
		return getE21();
	}

	public long getSiblingNumerator() {
		return getE12();
	}

	public long getSiblingDenominator() {
		return getE22();
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
	public TreeMatrix invert() {
		// remember det = -1
		// inv = -d(a22) b(a12) c(a21) -a(a11)
		return new BaseMatrix(-getE22(), getE12(), getE21(), -getE11());
	}

}
