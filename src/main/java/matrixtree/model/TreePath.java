package matrixtree.model;

import java.util.List;

import matrixtree.matrices.EncodingMatrix;

/**
 * @author Agustinus Lawandy
 * @since 
 */
public interface TreePath {

	/**
	 * @return
	 */
	List<Long> asList();

	/**
	 * @return
	 */
	EncodingMatrix getEncodingMatrix();

	/**
	 * @return
	 */
	TreePath getParentPath();

}
