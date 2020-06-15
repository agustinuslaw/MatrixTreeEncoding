package matrixtree.matrices;

import matrixtree.model.Rational;

/**
 * @author Agustinus Lawandy
 */
public interface EncodingMatrix extends TreeMatrix, SimpleInvertible {

	public long getNumerator();

	public long getDenominator();

	public long getSiblingNumerator();

	public long getSiblingDenominator();

	public Rational getNodeEncoding();

	public Rational getLowerBound();

	public Rational getSiblingNodeEncoding();

	public Rational getUpperBound();

}
