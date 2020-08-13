package matrixtree.model;

import java.io.Serializable;
import java.util.List;

import matrixtree.matrices.PathMatrix;

/**
 * Ancestors contain TreePath and ancestor matrices both computed from the reference path matrix.
 *
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
public interface Ancestors extends Serializable, Comparable<Ancestors> {

	/**
	 * @return the tree path of the node. This contains the path in the form of indexes. e.g. 50.1.2.2
	 */
	TreePath getTreePath();

	/**
	 * @return the ancestor matrices. This contains all path matrix from root path matrix to this path matrix.
	 */
	List<PathMatrix> getAncestorMatrices();

	/**
	 * @return the reference path matrix
	 */
	PathMatrix getPathMatrix();
}
