package matrixtree.model;

import java.util.List;

import matrixtree.matrices.PathMatrix;

/**
 * Tree path represent both the node child positions c and the encoding Path matrix P.
 *
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
public interface TreePath {

	/**
	 * @return tree path in terms of child positions c e.g. 2.4.3
	 */
	List<Long> asList();

	/**
	 * @return the Path matrix P encoding of this path e.g. P[2.4.3]
	 */
	PathMatrix computePathMatrix();

	/**
	 * @return the parent tree path e.g. T[2.4.3] -> T[2.4]
	 */
	TreePath getParentPath();

	/**
	 *
	 * @return depth of the {@link TreePath}
	 */
	int depth();

}
