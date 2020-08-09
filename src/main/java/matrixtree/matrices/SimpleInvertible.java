package matrixtree.matrices;

/**
 * Represents only those matrices whose determinant is 1, -1, or cleanly divides into all element.
 * This is due to the exact integer arithmetic required for the encoding matrices purposes. <br>
 * The notation for inverted matrix is M^-1.
 *
 * @author Agustinus Lawandy
 */
public interface SimpleInvertible extends StandardMatrix {

	/**
	 * Invert the matrix M. Only possible for matrix with determinant != 0.
	 * @return M^-1
	 */
	StandardMatrix invert();
}
