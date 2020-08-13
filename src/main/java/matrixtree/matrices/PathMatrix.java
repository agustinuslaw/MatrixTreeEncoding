package matrixtree.matrices;

import matrixtree.model.Ancestors;
import matrixtree.model.RationalInterval;

/**
 * Path matrix P that represent a single path in the tree. <br>
 * The notation is P[2.4.3] to indicate path encoding matrix for path 2.4.3.
 * <br>
 * P[2.4.3] = O.N[2].N[4].N[3] where O is Origin matrix (depends on algorithm
 * used, maybe identity I) and N is the Node matrix.
 *
 * @author Agustinus Lawandy
 */
public interface PathMatrix extends StandardMatrix, SimpleInvertible, Comparable<PathMatrix> {

	/**
	 * @return Numerator of the left/lower bound.
	 */
	long getNumerator();

	/**
	 * @return Denominator of the left/lower bound.
	 */
	long getDenominator();

	/**
	 * @return Numerator of the right/upper bound.
	 */
	long getSiblingNumerator();

	/**
	 * @return Denominator of the right/upper bound.
	 */
	long getSiblingDenominator();

	/**
	 * @return Another representation of the matrix in the form of interval [x,y)
	 */
	RationalInterval asNestedInterval();

	/**
	 * @return Compute the root matrix (matrix of the root node in the tree).
	 */
	PathMatrix computeRootMatrix();

	/**
	 * @return The depth of the matrix.
	 */
	int computeDepth();

	/**
	 * @return index among siblings
	 */
	long computeIndex();
	
	/**
	 * @return if this path matrix is at the root.
	 */
	boolean isRoot();
	
	/**
	 * @return the ancestors of this path matrix (entire path matrix to the root)
	 */
	Ancestors computeAncestors();

	/**
	 * @return parent path matrix if this matrix is not root.
	 */
	PathMatrix computeParentMatrix();

	/**
	 * @param k-th sibling from current location
	 * @return sibling matrix
	 */
	PathMatrix computeSibling(long k);

	/**
	 * @param k-th child
	 * @return child matrix
	 */
	PathMatrix computeChild(long k);
	
	
	
}
