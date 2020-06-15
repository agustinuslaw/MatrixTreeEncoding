package matrixtree.matrices;

import matrixtree.model.Rational;

/**
 * Path matrix P that represent a single path in the tree. <br>
 * The notation is P[2.4.3] to indicate path encoding matrix for path 2.4.3. <br>
 * P[2.4.3] = O.N[2].N[4].N[3] where O is Origin matrix (depends on algorithm used, maybe identity I) and N is the Node matrix.
 *
 * @author Agustinus Lawandy
 */
public interface PathMatrix extends StandardMatrix, SimpleInvertible {

    long getNumerator();

    long getDenominator();

    long getSiblingNumerator();

    long getSiblingDenominator();

    Rational getNodeEncoding();

    Rational getLowerBound();

    Rational getSiblingNodeEncoding();

    Rational getUpperBound();

}
