package matrixtree.structure;

import java.io.Serializable;

import matrixtree.matrices.PathMatrix;

/**
 * Represent modifiable tree structure.
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-09
 */
public interface MutableMatrixTreeNode<E extends Serializable> extends MatrixTreeNode<E> {
	/**
	 * @return re-compute path matrix of this node.
	 */
	PathMatrix computePathMatrix();

	/**
	 * Adds an element as a direct child to this node. With index one greater than the largest index.
	 * 
	 * @param childElement to add
	 * @return the created and inserted node
	 */
	MutableMatrixTreeNode<E> add(E childElement);

	/**
	 * Insert an element as a direct child to this node with a specified index. If there is already a child at
	 * childIndex, it is overwritten.
	 * 
	 * @param childElement to add
	 * @param childIndex   index of element
	 * @return the created and inserted node
	 */
	MutableMatrixTreeNode<E> insert(E childElement, int childIndex);

	/**
	 * Insert child node directly to parent.
	 * 
	 * @param child node to add
	 * @return reference to inserted child
	 */
	MutableMatrixTreeNode<E> insert(MutableMatrixTreeNode<E> child);

	/**
	 * Remove direct children if exists. May return null if there is no child at index.
	 * 
	 * @param childIndex to be removed
	 * @return the removed child or null.
	 */
	MutableMatrixTreeNode<E> remove(int childIndex);

	/**
	 * Remove direct children if exists.
	 * 
	 * @param node to be removed
	 * @return true if this list contained the specified element
	 */
	boolean remove(MutableMatrixTreeNode<E> node);

	/**
	 * Change the element the node contains.
	 * 
	 * @param element the node contains.
	 */
	void setNodeElement(E element);

	/**
	 * Removes the receiver from its parent.
	 */
	void removeFromParent();

	/**
	 * Sets the parent of the receiver to <code>newParent</code>. index will be kept the same. This should only be a
	 * simple parent setting. The node must be a proper parent (correct path matrix).
	 */
	void setParent(MutableMatrixTreeNode<E> newParent);

}
