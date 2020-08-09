package matrixtree.structure;

import java.io.Serializable;

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
     * Adds <code>child</code> to the receiver at <code>index</code>.
     * <code>child</code> will be messaged with <code>setParent</code>.
     */
    void add(E childElement);
	
    /**
     * Adds <code>child</code> to the receiver at the latest <code>index</code>.
     * <code>child</code> will be messaged with <code>setParent</code>.
     */
    MutableMatrixTreeNode<E> insert(E childElement);
    
    /**
     * Adds <code>child</code> to the receiver at <code>index</code>.
     * <code>child</code> will be messaged with <code>setParent</code>.
     */
    MutableMatrixTreeNode<E> insert(E childElement, int childIndex);
    
	/**
     * Adds <code>child</code> to the receiver at <code>index</code>.
     * <code>child</code> will be messaged with <code>setParent</code>.
     */
    MutableMatrixTreeNode<E> insert(MutableMatrixTreeNode<E> childNode);

    /**
     * Removes the child at <code>index</code> from the receiver.
     */
    void remove(int childIndex);

    /**
     * Removes <code>node</code> from the receiver. <code>setParent</code>
     * will be messaged on <code>node</code>.
     */
    void remove(MutableMatrixTreeNode<E> node);

    /**
     * Resets the user object of the receiver to <code>object</code>.
     */
    void setNodeElement(E element);

    /**
     * Removes the receiver from its parent.
     */
    void removeFromParent();

    /**
     * Sets the parent of the receiver to <code>newParent</code>. 
     * index will be kept the same.
     */
    void setParent(MutableMatrixTreeNode<E> newParent);
}
