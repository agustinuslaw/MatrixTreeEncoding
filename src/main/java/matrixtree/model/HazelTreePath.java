package matrixtree.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import matrixtree.computation.ExactMatrixOp;
import matrixtree.computation.MatrixOp;
import matrixtree.matrices.PathMatrix;
import matrixtree.matrices.HazelPathMatrix;
import matrixtree.matrices.HazelNodeMatrix;
import matrixtree.matrices.HazelOriginMatrix;
import matrixtree.matrices.StandardMatrix;

import static java.util.Collections.*;

/**
 * @author Agustinus Lawandy
 */
public class HazelTreePath implements TreePath  {
	private final List<Long> path;
	private final PathMatrix encoding;
	
	public HazelTreePath(Long... nodes) {
		super();
		this.path = unmodifiableList(List.of(nodes));
		this.encoding = computeEncoding(this.path);
	}
	
	public HazelTreePath(List<Long> nodes) {
		super();
		this.path = unmodifiableList(nodes);
		this.encoding = computeEncoding(this.path);
	}
	
	// TODO get sibling encoding
	// TODO get parent encoding
	
	@Override
	public List<Long> asList() {
		return path;
	}

	@Override
	public TreePath getParentPath() {
		LinkedList<Long> nodes = new LinkedList<>(path);
		
		if (nodes.size() > 1)
			nodes.pollLast();
		
		return new HazelTreePath(nodes);
	}
	
	@Override
	public PathMatrix getPathMatrix() {
		return encoding;
	}

	private HazelPathMatrix computeEncoding(List<Long> nodes)
	{
		List<StandardMatrix> product = new ArrayList<>();
		
		product.add(new HazelOriginMatrix());
		nodes.forEach(node -> product.add(new HazelNodeMatrix(node)));
		
		MatrixOp op = new ExactMatrixOp();
		return new HazelPathMatrix(op.multiply(product));
	}
	
}
