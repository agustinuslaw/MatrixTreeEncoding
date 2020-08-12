package matrixtree.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import matrixtree.exception.BadArgumentException;
import matrixtree.matrices.HazelPathMatrix;
import matrixtree.model.HazelAncestors;
import matrixtree.model.HazelTreePath;
import matrixtree.model.NestedInterval;
import matrixtree.model.TreePath;

/**
 * List based matrix tree node rearranges automatically after remove and insert operations. Thus there are no gaps between indexes.
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-09
 */
public class OrderedHazelMatrixTreeNode<E extends Serializable> implements MutableMatrixTreeNode<E> {

	private static final long serialVersionUID = -2783094080814941618L;

	// transient to prevent this being serialized, which will cause stackoverflow
	// error
	private transient OrderedHazelMatrixTreeNode<E> parent;

	private E element;
	private long index;
	private HazelPathMatrix pathMatrix;
	private NestedInterval interval;
	private List<OrderedHazelMatrixTreeNode<E>> children;

	private final transient Supplier<List<OrderedHazelMatrixTreeNode<E>>> supplier = ArrayList::new;

	public OrderedHazelMatrixTreeNode(OrderedHazelMatrixTreeNode<E> parent, E element, long index) {
		super();

		// simple setting
		this.parent = parent;
		this.index = index;
		this.element = element;
		this.children = supplier.get();

		// compute other values
		this.pathMatrix = computePathMatrix();
		this.interval = pathMatrix.asNestedInterval();
	}

	public OrderedHazelMatrixTreeNode(OrderedHazelMatrixTreeNode<E> parent, E element, HazelPathMatrix matrix) {
		super();

		// simple setting
		this.parent = parent;
		this.element = element;
		this.children = supplier.get();
		this.pathMatrix = matrix;
		this.interval = pathMatrix.asNestedInterval();

		// compute other values
		this.index = pathMatrix.computeIndex();
	}

	@Override
	public int compareTo(MatrixTreeNode<E> o) {
		return getPathMatrix().compareTo(o.getPathMatrix());
	}

	@Override
	public HazelAncestors computeAncestors() {
		return getPathMatrix().computeAncestors();
	}

	@Override
	public HazelPathMatrix computePathMatrix() {
		if (parent != null) {
			// recompute path matrix depending on parent
			HazelPathMatrix parentM = parent.computePathMatrix();
			return new HazelPathMatrix(parentM, index);
		} else if (pathMatrix == null || isRoot()) {
			// become root according to index
			return new HazelTreePath(index).computePathMatrix();
		} else {
			// if there is already a path matrix and no use case fits.
			return pathMatrix;
		}
	}

	@Override
	public HazelTreePath computeTreePath() {
		return computeAncestors().getTreePath();
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
	public OrderedHazelMatrixTreeNode<E> getChildAt(int childIndex) {
		if (childIndex <= 0) {
			// When denominator == 1 : Reached top level already, division by 0 my occur
			throw new BadArgumentException("childIndex", childIndex, "[1,Inf)");
		}

		// this downcast is appropriate since only root elements can be long
		return children.get(positionOf(childIndex));
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	public List<MatrixTreeNode<E>> getChildren() {
		// safety copy to avoid aliasing error
		return children.stream()//
				.map(e -> ((MatrixTreeNode<E>) e))//
				.collect(Collectors.toList());
	}

	@Override
	public E getElement() {
		return element;
	}

	@Override
	public long getIndex() {
		return index;
	}

	@Override
	public OrderedHazelMatrixTreeNode<E> getParent() {
		return parent;
	}

	@Override
	public HazelPathMatrix getPathMatrix() {
		return pathMatrix;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pathMatrix);
	}

	/**
	 * Converts between list position into matrix index. This is because index starts from 1. While the list element starts from 0
	 * <p>
	 * return position + 1;
	 * 
	 * @param position of list
	 * @return matrix index
	 */
	private int indexOf(int position) {
		return position + 1;
	}

	@Override
	public MutableMatrixTreeNode<E> insert(MutableMatrixTreeNode<E> child) {
		children.add((OrderedHazelMatrixTreeNode<E>) child);
		child.setParent(this);
		return child;
	}
	
	@Override
	public MutableMatrixTreeNode<E> add(E childElement) {

		return insert(childElement, indexOf(getChildCount()));
	}

	@Override
	public MutableMatrixTreeNode<E> insert(E childElement, int childIndex) {
		OrderedHazelMatrixTreeNode<E> inserted = new OrderedHazelMatrixTreeNode<>(this, childElement, childIndex);

		return buildTree(inserted);
	}

	@Override
	public MutableMatrixTreeNode<E> buildTree(MutableMatrixTreeNode<E> childNode) {
		// check if this node can contain childNode
		if (!interval.contains(childNode.getPathMatrix().asNestedInterval()))
			throw new IllegalArgumentException("Child interval " + childNode.getPathMatrix() + " is not contained in parent " + pathMatrix);

		// get the parent of the child node if exist, if not throw an exception
		OrderedHazelMatrixTreeNode<E> parentNode = parentOf(childNode);

		// remove old child from parent
		int childIndex = (int) childNode.getIndex();
		if (!parentNode.isLeaf() && parentNode.getChildCount() > childIndex)
			parentNode.remove(childIndex);
		
		// add the new child
		parentNode.children.add(positionOf(childIndex), (OrderedHazelMatrixTreeNode<E>) childNode);

		childNode.setParent(parentNode);

		return childNode;
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public boolean isRoot() {
		return parent == null && pathMatrix.isRoot();
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
			throw new IllegalArgumentException("Not a child element! Child " + childNode.getPathMatrix() + " This " + getPathMatrix());
		} else if (depthDifference == 1) {
			// direct insert
			parentNode = this;
		} else {
			int depth = nodePath.depth();
			int childParentDepth = childPath.depth() - 1;
			int candidateParentIdx;
			parentNode = null;
			
			while (depth < childParentDepth) {
				candidateParentIdx = childPath.asList().get(depth).intValue();

				if (positionOf(candidateParentIdx /* index */) >= getChildCount() /*pos*/ )
					throw new IndexOutOfBoundsException("Ancestor nodes are not yet in place!");

				parentNode = getChildAt(candidateParentIdx);

				depth = depth + 1;
			}
		}

		if (parentNode == null)
			throw new NullPointerException("Parent node for " + childPath + " is not found!");

		return parentNode;
	}

	/**
	 * Converts between matrix index into list position. This is because index starts from 1. While the list element starts from 0.
	 * <p>
	 * return index - 1;
	 * 
	 * @param index of matrix
	 * @return list position
	 */
	private int positionOf(int index) {
		return index - 1;
	}

	@Override
	public OrderedHazelMatrixTreeNode<E> remove(int childIndex) {
		OrderedHazelMatrixTreeNode<E> removed = children.remove(positionOf(childIndex));
		removed.setParent(null);
		return removed;
	}

	@Override
	public boolean remove(MutableMatrixTreeNode<E> node) {
		boolean result = children.remove(node);
		node.setParent(null);
		return result;
	}

//	/**
//	 * Only needed for relocations
//	 */
//	private void recomputeSubtreePathMatrix() {
//		pathMatrix = computePathMatrix();
//		children.forEach(OrderedHazelMatrixTreeNode::recomputeSubtreePathMatrix);
//	}

	@Override
	public void removeFromParent() {
		if (parent != null) {
			parent.remove(this);
		}
	}

	@Override
	public void setNodeElement(E element) {
		this.element = element;
	}

	@Override
	public void setParent(MutableMatrixTreeNode<E> newParent) {
		// this should only be for direct parents, there musn't be any relocation
		this.parent = (OrderedHazelMatrixTreeNode<E>) newParent;
	}

	@Override
	public String toString() {
		return treeRepresentation(1);
	}

	private String treeRepresentation(int depth) {
		// base case:
		String root = this.lineRepresentation();
		// recursive case:
		String indent = Strings.repeat("   ", depth);
		for (OrderedHazelMatrixTreeNode<E> child : children)
			root += indent + child.treeRepresentation(depth + 1);

		return root;
	}

	private String lineRepresentation() {
		String parentRef = parent != null ? "exist" : "null";

		return "Node{" + "elem:" + getElement() + ", " + "idx:" + getIndex() + ", " + "mat:" + getPathMatrix() + ", " + "interval:" + interval.doubleStr(4) + ", parentRef:"
				+ parentRef + "}\n";
	}

}
