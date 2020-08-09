package matrixtree.model;

import java.io.Serializable;
import java.util.List;

import matrixtree.matrices.PathMatrix;

/**
 * Ancestors contain TreePath and precomputed
 *
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
public interface Ancestors extends Serializable, Comparable<Ancestors> {

	TreePath getTreePath();

	List<PathMatrix> getAncestorMatrices();

	PathMatrix getPathMatrix();
}
