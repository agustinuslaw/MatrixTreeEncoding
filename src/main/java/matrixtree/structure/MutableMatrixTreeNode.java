package matrixtree.structure;

import java.io.Serializable;

import com.google.common.annotations.Beta;

import matrixtree.matrices.PathMatrix;

/**
 * @author Agustinus Lawandy
 * @since 2020-08-09
 */
public interface MutableMatrixTreeNode<E extends Serializable> extends MatrixTreeNode<E> {
	/**
	 * @return re-compute path matrix of this node.
	 */
	PathMatrix computePathMatrix();

	/**
	 * Adds <code>child</code> to the receiver at the latest <code>index</code>.
	 * <code>child</code> will be messaged with <code>setParent</code>.
	 */
	MutableMatrixTreeNode<E> add(E childElement);

	/**
	 * Adds <code>child</code> to the receiver at <code>index</code>.
	 * <code>child</code> will be messaged with <code>setParent</code>.
	 */
	MutableMatrixTreeNode<E> insert(E childElement, int childIndex);

	/**
	 * Insert child directly to parent.
	 * @param child node
	 * @return reference to inserted child
	 */
	MutableMatrixTreeNode<E> insert(MutableMatrixTreeNode<E> child);

	/**
	 * Adds <code>child</code> to the receiver at <code>index</code>.
	 * <code>child</code> will be messaged with <code>setParent</code>.
	 * 
	 * This feature is experimental!
	 */
	@Beta
	MutableMatrixTreeNode<E> buildTree(MutableMatrixTreeNode<E> childNode);

	/**
	 * Removes the child at <code>index</code> from the receiver.
	 */
	MutableMatrixTreeNode<E> remove(int childIndex);

	/**
	 * Removes <code>node</code> from the receiver. <code>setParent</code> will be
	 * messaged on <code>node</code>.
	 */
	boolean remove(MutableMatrixTreeNode<E> node);

	/**
	 * Resets the user object of the receiver to <code>object</code>.
	 */
	void setNodeElement(E element);

	/**
	 * Removes the receiver from its parent.
	 */
	void removeFromParent();

	/**
	 * Sets the parent of the receiver to <code>newParent</code>. index will be kept
	 * the same. This should only be a simple parent set. The node must be a proper
	 * parent (correct path matrix).
	 */
	void setParent(MutableMatrixTreeNode<E> newParent);

}
