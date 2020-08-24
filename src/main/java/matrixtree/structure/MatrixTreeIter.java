package matrixtree.structure;

import java.io.Serializable;
import java.util.Iterator;

import matrixtree.validation.Precondition;

/**
 * This code was adapted from iterator implementation in
 * https://github.com/gt4dev/yet-another-tree-structure
 * 
 * @since 2020-08-22
 * @param <E> node element
 */
public class MatrixTreeIter<E extends Serializable> implements Iterator<MatrixTreeNode<E>> {

	enum Stage {
		PARENT, CURRENT_CHILD, SUB_CHILD
	}

	private MatrixTreeNode<E> treeNode;

	public MatrixTreeIter(MatrixTreeNode<E> treeNode) {
		this.treeNode = treeNode;
		this.nextStage = Stage.PARENT;
		// java collection iterator
		this.childrenIt = treeNode.getChildren().iterator();
	}

	private Stage nextStage;
	private MatrixTreeNode<E> next;
	/** This is a java collection iterator */
	private Iterator<MatrixTreeNode<E>> childrenIt;
	/** This is MatrixTreeIter */
	private Iterator<MatrixTreeNode<E>> childrenNodeIt;

	String element(MatrixTreeNode<E> node) {
		return node != null ? node.getElement().toString() : "null";
	}

	@Override
	public String toString() {
		return "MatrixTreeIter [treeNode=" + element(treeNode) + ",\n stage=" + nextStage + ",\n next=" + element(next)
				+ ",\n childrenIt=" + childrenIt + ",\n childrenNodeIt=" + (childrenNodeIt != null) + "]";
	}

	@Override
	public boolean hasNext() {

		// base case: visit the first element in the tree
		if (nextStage == Stage.PARENT) {
			next = treeNode;
			nextStage = Stage.CURRENT_CHILD;
			return true;
		}

		// recursive : visit each direct children
		else if (nextStage == Stage.CURRENT_CHILD) {
			// this hasNext checks existence of more direct children, non recursive
			if (childrenIt.hasNext()) {
				MatrixTreeNode<E> childDirect = childrenIt.next();
				childrenNodeIt = childDirect.iterator();
				nextStage = Stage.SUB_CHILD;
				// this jumps to the SUB_CHILD block to begin the recursive case
				return this.hasNext();
			}
			// base case: no more direct children
			else {
				nextStage = null;
				return false;
			}
		}

		// recursive : visit child of children
		else if (nextStage == Stage.SUB_CHILD) {
			if (childrenNodeIt.hasNext()) {
				next = childrenNodeIt.next();
				return true;
			} 
			// go up as there is no more children in this level
			else {
				next = null;
				nextStage = Stage.CURRENT_CHILD;
				return hasNext();
			}
		}

		// should never be here
		throw new IllegalStateException("nextStage is unknown!");
	}

	@Override
	public MatrixTreeNode<E> next() {
		Precondition.checkElementExist(next != null, "There is no more node!");
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}