package matrixtree.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import matrixtree.matrices.HazelPathMatrix;
import matrixtree.model.HazelAncestors;
import matrixtree.model.HazelTreePath;
import matrixtree.model.TreePath;

/**
 * List based matrix tree node rearranges automatically after remove and insert
 * operations. Thus there are no gaps between indexes.
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-09
 */
public class OrderedHazelMatrixTreeNode<E extends Serializable> implements MutableMatrixTreeNode<E> {

	private static final long serialVersionUID = 1L;

	private E element;
	private long index;
	private transient OrderedHazelMatrixTreeNode<E> parent;
	private HazelPathMatrix pathMatrix;
	private List<OrderedHazelMatrixTreeNode<E>> children;

	private final transient Supplier<List<OrderedHazelMatrixTreeNode<E>>> supplier = ArrayList::new;

	public OrderedHazelMatrixTreeNode(OrderedHazelMatrixTreeNode<E> parent, E element, long index) {
		super();
		this.parent = parent;

		// simple setting
		this.index = index;
		this.element = element;
		this.children = supplier.get();

		// require parent and index
		this.pathMatrix = computePathMatrix();
	}

	@Override
	public HazelPathMatrix getPathMatrix() {
		return pathMatrix;
	}

	@Override
	public HazelPathMatrix computePathMatrix() {
		if (parent != null) {
			HazelPathMatrix parentM = parent.computePathMatrix();
			return new HazelPathMatrix(parentM, index);
		} else {
			return new HazelTreePath(index).computePathMatrix();
		}
	}

	@Override
	public HazelTreePath computeTreePath() {
		return computeAncestors().getTreePath();
	}

	@Override
	public HazelAncestors computeAncestors() {
		return computePathMatrix().computeAncestors();
	}

	@Override
	public E getElement() {
		return element;
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public long getIndex() {
		return index;
	}

	@Override
	public OrderedHazelMatrixTreeNode<E> getChildAt(int childIndex) {
		// this downcast is appropriate since only root elements can be long
		return children.get(positionOf(childIndex));
	}

	@Override
	public OrderedHazelMatrixTreeNode<E> getParent() {
		return parent;
	}

	public List<MatrixTreeNode<E>> getChildren() {
		// safety copy to avoid aliasing error
		return children.stream()//
				.map(e -> ((MatrixTreeNode<E>) e))//
				.collect(Collectors.toList());
	}

	@Override
	public boolean isRoot() {
		return parent == null && pathMatrix.isRoot();
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public void add(E childElement) {
		int childIndex = getChildCount();
		insert(childElement, childIndex);
	}
	
	@Override
	public MutableMatrixTreeNode<E> insert(E childElement) {
		
		return insert(childElement, indexOf(getChildCount()));
	}

	private int positionOf(int index)
	{
		return index - 1;
	}
	
	private int indexOf(int position)
	{
		return position + 1;
	}
	
	@Override
	public MutableMatrixTreeNode<E> insert(E childElement, int childIndex) {
		OrderedHazelMatrixTreeNode<E> inserted = new OrderedHazelMatrixTreeNode<>(this, childElement, childIndex);

		return insert(inserted);
	}

	@Override
	public MutableMatrixTreeNode<E> insert(MutableMatrixTreeNode<E> childNode) {
		// check if this node can contain childNode
		if (!pathMatrix.asNestedInterval().contains(childNode.getPathMatrix().asNestedInterval()))
			throw new IllegalArgumentException(
					"Child interval " + childNode.getPathMatrix() + " is not contained in parent " + pathMatrix);

		// get the parent of the child node if exist, if not throw an exception
		OrderedHazelMatrixTreeNode<E> parentNode = parentOf(childNode);

		// remove old child
		int childIndex = (int) childNode.getIndex();
		if (!this.isLeaf() && getChildCount() > childIndex)
			parentNode.remove(childIndex);
		// add the new child
		parentNode.children.add(positionOf(childIndex), (OrderedHazelMatrixTreeNode<E>) childNode);

		childNode.setParent(parentNode);

		return childNode;
	}

	/**
	 * @param childNode
	 * @return the parent node of childNode if contained in this tree
	 */
	private OrderedHazelMatrixTreeNode<E> parentOf(MatrixTreeNode<E> childNode) {
		// at this point child and node is guaranteed to share path/ancestors
		TreePath childPath = childNode.computeAncestors().getTreePath();
		TreePath nodePath = this.computeAncestors().getTreePath();

		int depthDifference = childPath.depth() - nodePath.depth();

		// get the parent of the child node if exist, if not throw an exception
		OrderedHazelMatrixTreeNode<E> parentNode;
		if (depthDifference <= 0) {
			throw new IllegalArgumentException(
					"Not a child element! Child " + childNode.getPathMatrix() + " This " + getPathMatrix());
		} else if (depthDifference == 1) {
			// direct insert
			parentNode = this;
		} else {
			int depth = nodePath.depth();
			int candidateParentIdx;
			parentNode = null;
			while (depth < childPath.depth()) {
				candidateParentIdx = childPath.asList().get(depth).intValue();

				if (candidateParentIdx >= getChildCount())
					throw new IndexOutOfBoundsException("Ancestor nodes are not yet in place!");

				parentNode = getChildAt(candidateParentIdx);

				depth++;
			}
		}

		if (parentNode == null)
			throw new NullPointerException("Parent node for " + childPath + " is not found!");

		return parentNode;
	}

	@Override
	public void remove(int childIndex) {
		OrderedHazelMatrixTreeNode<E> child = getChildAt(childIndex);
		children.remove(positionOf(childIndex));
		child.setParent(null);
	}

	@Override
	public void remove(MutableMatrixTreeNode<E> node) {
		children.remove(node);
		node.setParent(null);
	}

	@Override
	public void setNodeElement(E element) {
		this.element = element;
	}

	@Override
	public void removeFromParent() {
		if (parent != null) {
			parent.remove(this);
		}
	}

	@Override
	public void setParent(MutableMatrixTreeNode<E> newParent) {
		// this should only be for direct parents, there musn't be any relocation
		this.parent = (OrderedHazelMatrixTreeNode<E>) newParent;
	}

//	/**
//	 * Only needed for relocations
//	 */
//	private void recomputeSubtreePathMatrix() {
//		pathMatrix = computePathMatrix();
//		children.forEach(OrderedHazelMatrixTreeNode::recomputeSubtreePathMatrix);
//	}

	@Override
	public int compareTo(MatrixTreeNode<E> o) {
		return getPathMatrix().compareTo(o.getPathMatrix());
	}

	@Override
	public int hashCode() {
		return Objects.hash(pathMatrix);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		OrderedHazelMatrixTreeNode<E> other = (OrderedHazelMatrixTreeNode<E>) obj;
		return Objects.equals(pathMatrix, other.pathMatrix);
	}

	@Override
	public String toString() {

		return "OrderedHazelMatrixTreeNode [element=" + element + ", index=" + index + ", pathMatrix=" + pathMatrix
				+ ", children=" + children + "]";

	}

	

}
