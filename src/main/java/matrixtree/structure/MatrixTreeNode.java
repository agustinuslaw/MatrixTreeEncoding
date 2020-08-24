package matrixtree.structure;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import matrixtree.matrices.PathMatrix;
import matrixtree.model.Ancestors;
import matrixtree.model.RationalInterval;
import matrixtree.model.TreePath;

/**
 * Defines the requirements of object that
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-07
 */
public interface MatrixTreeNode<E extends Serializable>
		extends Serializable, Comparable<MatrixTreeNode<E>>, Iterable<MatrixTreeNode<E>> {

	/**
	 * Important for sorting only with respect to path matrices.
	 * 
	 * {@inheritDoc}
	 */
	default int compareTo(MatrixTreeNode<E> o) {
		return getPathMatrix().compareTo(o.getPathMatrix());
	}

	/**
	 * @return the ancestors (the path in terms of path matrices)
	 */
	default Ancestors computeAncestors() {
		return getPathMatrix().computeAncestors();
	}

	/**
	 * @return the tree path of this node (the path in terms of index nodes)
	 */
	default TreePath computeTreePath() {
		return computeAncestors().getTreePath();
	}

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
	Collection<MatrixTreeNode<E>> getChildren();

	/**
	 * @return the stored element in this node
	 */
	E getElement();

	/**
	 * @return current index
	 */
	long getIndex();

	/**
	 * @return the interval for this path matrix
	 */
	RationalInterval getInterval();

	/**
	 * @return the parent <code>TreeNode</code> of the receiver.
	 */
	MatrixTreeNode<E> getParent();

	/**
	 * @return the pre-computed path matrix of this node.
	 */
	PathMatrix getPathMatrix();

	/**
	 * @return element type
	 */
	Class<E> getType();

	/**
	 * @return true if the receiver is a leaf.
	 */
	default boolean isLeaf() {
		return getChildren().isEmpty();
	}

	/**
	 * @return whether this node is root.
	 */
	default boolean isRoot() {
		return getParent() == null && getPathMatrix().isRoot();
	}

	default Iterator<MatrixTreeNode<E>> iterator() {
		return new MatrixTreeIter<>(this);
	}

	/**
	 * @return the string representation of a single node (without tree structure)
	 */
	default String lineRepresentation() {
		String parentRef = getParent() != null ? "exist" : "null";

		return getClass().getSimpleName() + "{" + "elem:" + getElement() + ", " + "idx:" + getIndex() + ", " + "mat:" + getPathMatrix()
				+ ", " + "interval:" + getInterval().toDoubleStr(4) + ", parentRef:" + parentRef + "}\n";
	}

	/**
	 * Returns a sequential {@code Stream} with this collection as its source.
	 *
	 * <p>
	 * This method should be overridden when the {@link #spliterator()} method
	 * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT}, or
	 * <em>late-binding</em>. (See {@link #spliterator()} for details.)
	 *
	 * @implSpec The default implementation creates a sequential {@code Stream} from
	 *           the collection's {@code Spliterator}.
	 *
	 * @return a sequential {@code Stream} over the elements in this collection
	 * @since 1.8
	 */
	default Stream<MatrixTreeNode<E>> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	/**
	 * This recursively visits the parent until there is no longer parent available.
	 * Note the returned node may not be 'root' node since the matrix encoding
	 * should designate which should be the root node.
	 * <p>
	 * To determine whether the node is root use {@link #isRoot()}
	 * 
	 * @return top level node in the tree, may not be root
	 */
	default MatrixTreeNode<E> visitTopNode() {
		// base case
		if (getParent() == null)
			return this;
		// recursive case
		else
			return getParent().visitTopNode();
	}

}
