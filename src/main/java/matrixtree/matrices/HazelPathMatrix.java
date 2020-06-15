package matrixtree.matrices;

import matrixtree.model.Rational;

/**
 * Hazel scheme Path matrix P that represent a single path in the tree. Note for Hazel scheme the true root is
 * the Origin matrix O defined in {@link HazelOriginMatrix}. <br>
 * P[1.1.2] = O.N[1].N[1].N[2]
 *
 * @author Agustinus Lawandy
 */
public class HazelPathMatrix extends BaseMatrix implements PathMatrix, StandardMatrix, SimpleInvertible {

    /**
     * Initialize {@link HazelPathMatrix} with lower and upper bounds.
     * @param node encoding ratio
     * @param sibling encoding ratio
     */
    public HazelPathMatrix(Rational node, Rational sibling) {
        // a11, a12, a21, a22
        super(node.getNumerator(), sibling.getNumerator(), node.getDenominator(), sibling.getDenominator());
    }

    /**
     * Initialize {@link HazelPathMatrix} with individual element.
     * @param numerator (e11) node numerator
     * @param siblingNumerator (e12) node sibling numerator
     * @param denominator (e21) node denominator
     * @param siblingDenominator (e22) node sibling denominator
     */
    public HazelPathMatrix(long numerator, long siblingNumerator, long denominator, long siblingDenominator) {
        // a11, a12, a21, a22
        super(numerator, siblingNumerator, denominator, siblingDenominator);
    }

    public HazelPathMatrix(StandardMatrix other) {
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
    public StandardMatrix invert() {
        // remember det = -1
        // inv = -d(a22) b(a12) c(a21) -a(a11)
        return new BaseMatrix(-getE22(), getE12(), getE21(), -getE11());
    }

}
