package matrixtree.structure;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import matrixtree.matrices.HazelPathMatrix;
import matrixtree.model.HazelAncestors;
import matrixtree.model.HazelTreePath;
import matrixtree.model.RationalInterval;
import matrixtree.validation.Precondition;

/**
 * Map based matrix tree node. Gaps may occur between indexes. No reordering will be done.
 * 
 * @author Agustinus Lawandy
 *
 * @param <E> type parameter of element
 */
public class MapHazelMatrixTreeNode<E extends Serializable> implements MutableMatrixTreeNode<E> {

	private static final long serialVersionUID = -2783094080814941618L;

	// transient to prevent this being serialized, which will cause stackoverflow
	private transient MapHazelMatrixTreeNode<E> parent;

	private E element;
	private long index;
	private HazelPathMatrix pathMatrix;
	private RationalInterval interval;
	private Map<Integer, MapHazelMatrixTreeNode<E>> children;
	private Class<E> type;

	private final transient Supplier<Map<Integer, MapHazelMatrixTreeNode<E>>> supplier = TreeMap::new;

	@SuppressWarnings("unchecked")
	public MapHazelMatrixTreeNode(MapHazelMatrixTreeNode<E> parent, E element, long index) {
		super();

		// simple setting
		this.parent = parent;
		this.index = index;
		this.element = element;
		this.children = supplier.get();

		// compute other values
		this.pathMatrix = computePathMatrix();
		this.interval = pathMatrix.asNestedInterval();
		this.type = (Class<E>) element.getClass();
	}

	@SuppressWarnings("unchecked")
	public MapHazelMatrixTreeNode(MapHazelMatrixTreeNode<E> parent, E element, HazelPathMatrix matrix) {
		super();

		// simple setting
		this.parent = parent;
		this.element = element;
		this.children = supplier.get();
		this.pathMatrix = matrix;
		this.interval = pathMatrix.asNestedInterval();

		// compute other values
		this.index = pathMatrix.computeIndex();
		this.type = (Class<E>) element.getClass();
	}

	public RationalInterval getInterval() {
		return interval;
	}

	@Override
	public Class<E> getType() {
		return type;
	}

	public Supplier<Map<Integer, MapHazelMatrixTreeNode<E>>> getSupplier() {
		return supplier;
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
		@SuppressWarnings("rawtypes")
		MapHazelMatrixTreeNode other = (MapHazelMatrixTreeNode) obj;
		return Objects.equals(children, other.children) && Objects.equals(element, other.element)
				&& index == other.index && Objects.equals(interval, other.interval)
				&& Objects.equals(pathMatrix, other.pathMatrix) && Objects.equals(type, other.type);
	}

	@Override
	public MapHazelMatrixTreeNode<E> getChildAt(int childIndex) {
		Precondition.checkDomain(childIndex > 0, "childIndex", childIndex, "[1,Inf)");

		// this downcast is appropriate since only root elements can be long
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@SuppressWarnings("unchecked")
	public Set<MatrixTreeNode<E>> getChildren() {
		// safety copy to avoid aliasing error
		return children.values().stream().map(n -> (MatrixTreeNode<E>) children).collect(Collectors.toSet());
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
	public MapHazelMatrixTreeNode<E> getParent() {
		return parent;
	}

	@Override
	public HazelPathMatrix getPathMatrix() {
		return pathMatrix;
	}

	@Override
	public int hashCode() {
		return Objects.hash(children, element, index, interval, pathMatrix, type);
	}

	@Override
	public MutableMatrixTreeNode<E> insert(MutableMatrixTreeNode<E> child) {
		// downcast is appropriate because child indexes are always ints
		children.put((int) child.getIndex(), (MapHazelMatrixTreeNode<E>) child);
		child.setParent(this);
		return child;
	}

	@Override
	public MutableMatrixTreeNode<E> add(E childElement) {

		return insert(childElement, getNextIndex());
	}

	private int getNextIndex() {
		// downcast is appropriate because child indexes are always int
		return (int) children.keySet().stream().max(Long::compare).orElse(0) + 1;
	}

	@Override
	public MutableMatrixTreeNode<E> insert(E childElement, int childIndex) {
		return insert(new MapHazelMatrixTreeNode<E>(this, childElement, childIndex));
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public boolean isRoot() {
		return parent == null && pathMatrix.isRoot();
	}

	@Override
	public MatrixTreeNode<E> visitTopNode() {
		// base case
		if (parent == null)
			return this;
		// recursive case
		else
			return parent.visitTopNode();
	}

	@Override
	public MapHazelMatrixTreeNode<E> remove(int childIndex) {
		if (!children.containsKey(childIndex))
			return null;

		MapHazelMatrixTreeNode<E> removed = children.remove(childIndex);
		removed.setParent(null);
		return removed;
	}

	@Override
	public boolean remove(MutableMatrixTreeNode<E> node) {
		// downcast is appropriate since children indexes are always int
		MapHazelMatrixTreeNode<E> result = children.remove((int) node.getIndex());
		node.setParent(null);
		return result != null ? true : false;
	}

	@Override
	public void removeFromParent() {
		if (parent != null) {
			parent.remove(this);
		}
	}

	@Override
	public void setNodeElement(E element) {
		this.element = Objects.requireNonNull(element);
	}

	@Override
	public void setParent(MutableMatrixTreeNode<E> newParent) {

		// this should only be for direct parents, there musn't be any relocation
		this.parent = (MapHazelMatrixTreeNode<E>) newParent;
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
		for (MapHazelMatrixTreeNode<E> child : children.values())
			root += indent + child.treeRepresentation(depth + 1);

		return root;
	}

	private String lineRepresentation() {
		String parentRef = parent != null ? "exist" : "null";

		return "Node{" + "elem:" + getElement() + ", " + "idx:" + getIndex() + ", " + "mat:" + getPathMatrix() + ", "
				+ "interval:" + interval.toDoubleStr(4) + ", parentRef:" + parentRef + "}\n";
	}

}
