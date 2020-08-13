package matrixtree.structure;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import matrixtree.matrices.PathMatrix;

/**
 * Build tree structure from a collection of nodes.
 * 
 * @author Agustinus Lawandy
 * @param <E> type parameter of element
 */
public class MatrixTreeBuilder<E extends Serializable> {

	private final Map<PathMatrix, MutableMatrixTreeNode<E>> nodeMap;
	private final Class<E> type;

	private MatrixTreeBuilder(Map<PathMatrix, MutableMatrixTreeNode<E>> nodeMap, Class<E> type) {
		super();
		this.nodeMap = nodeMap;
		this.type = type;
	}

	/**
	 * Starts the building process.
	 * 
	 * @param <E>  type parameter of element
	 * @param type class of element
	 * @return the builder
	 */
	public static <E extends Serializable> MatrixTreeBuilder<E> of(Class<E> type) {
		// Tree map is important to preserve order of keys. Note keys (path matrix) are comparable
		return new MatrixTreeBuilder<E>(new TreeMap<>(), type);
	}

	/**
	 * Puts the node into the builder.
	 * 
	 * @param node to be incorporated into matrix structure.
	 * @return this builder
	 */
	public MatrixTreeBuilder<E> put(MutableMatrixTreeNode<E> node) {
		nodeMap.put(node.getPathMatrix(), node);
		return this;
	}

	/**
	 * Create the tree structure using parental relations. Assumes all nodes are connected.
	 * 
	 * @return the top level node, may not be root.
	 */
	public MutableMatrixTreeNode<E> buildTree() {
		// use parental relations to build the tree by directly inserting each child into it's parents
		for (MutableMatrixTreeNode<E> node : nodeMap.values()) {
			PathMatrix key = node.getPathMatrix();

			if (!key.isRoot()) {
				PathMatrix parentKey = key.computeParentMatrix();

				MutableMatrixTreeNode<E> parentNode = nodeMap.get(parentKey);
				parentNode.insert(node);
			}
		}

		// get root
		PathMatrix firstKey = nodeMap.keySet().iterator().next();
		return nodeMap.get(firstKey).visitTopNode();
	}

	/* Getters */

	/**
	 * @return the node map
	 */
	public Map<PathMatrix, MutableMatrixTreeNode<E>> getNodeMap() {
		return nodeMap;
	}

	/**
	 * @return class of element
	 */
	public Class<E> getType() {
		return type;
	}

}
