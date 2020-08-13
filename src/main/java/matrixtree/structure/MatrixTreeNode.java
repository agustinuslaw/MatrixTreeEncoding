package matrixtree.structure;

import java.io.Serializable;
import java.util.List;

import matrixtree.matrices.PathMatrix;
import matrixtree.model.Ancestors;
import matrixtree.model.TreePath;

/**
 * Defines the requirements of object that
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-07
 */
public interface MatrixTreeNode<E extends Serializable> extends Serializable, Comparable<MatrixTreeNode<E>> {
	/**
	 * @return the ancestors (the path in terms of path matrices)
	 */
	Ancestors computeAncestors();

	/**
	 * @return the tree path of this node (the path in terms of index nodes)
	 */
	TreePath computeTreePath();

	/**
	 * Returns the child <code>TreeNode</code> at index <code>childIndex</code>.
	 */
	MatrixTreeNode<E> getChildAt(int childIndex);

	/**
	 * @return the number of children <code>TreeNode</code>s the receiver contains.
	 */
	int getChildCount();

	/**
	 * @return the children of this node
	 */
	List<MatrixTreeNode<E>> getChildren();

	/**
	 * @return the stored element in this node
	 */
	E getElement();

	/**
	 * @return current index
	 */
	long getIndex();

	/**
	 * @return the parent <code>TreeNode</code> of the receiver.
	 */
	MatrixTreeNode<E> getParent();

	/**
	 * @return the pre-computed path matrix of this node.
	 */
	PathMatrix getPathMatrix();

	/**
	 * @return true if the receiver is a leaf.
	 */
	boolean isLeaf();

	/**
	 * @return whether this node is root.
	 */
	boolean isRoot();

	/**
	 * @return element type
	 */
	Class<E> getType();

	/**
	 * This recursively visits the parent until there is no longer parent available. Note the returned node may not be 'root' node since the matrix
	 * encoding should designate which should be the root node.
	 * <p>
	 * To determine whether the node is root use {@link #isRoot()}
	 * 
	 * @return top level node in the tree, may not be root
	 */
	ListHazelMatrixTreeNode<E> visitTopNode();
}
