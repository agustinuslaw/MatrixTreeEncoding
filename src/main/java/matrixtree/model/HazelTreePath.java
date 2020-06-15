package matrixtree.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import matrixtree.computation.ExactMatrixOp;
import matrixtree.computation.MatrixOp;
import matrixtree.matrices.EncodingMatrix;
import matrixtree.matrices.HazelEncodingMatrix;
import matrixtree.matrices.HazelNodeMatrix;
import matrixtree.matrices.HazelOriginMatrix;
import matrixtree.matrices.TreeMatrix;
/**
 * @author Agustinus Lawandy
 */
public class HazelTreePath implements TreePath  {
	private final List<Long> path;
	private final EncodingMatrix encoding;
	
	public HazelTreePath(Long... nodes) {
		super();
		this.path = Collections.unmodifiableList(Arrays.asList(nodes));
		this.encoding = computeEncoding(this.path);
	}
	
	public HazelTreePath(List<Long> nodes) {
		super();
		this.path = Collections.unmodifiableList(nodes);
		this.encoding = computeEncoding(this.path);
	}
	
	// TODO get sibling encoding
	// TODO get parent encoding
	
	@Override
	public List<Long> asList() {
		return path;
	}

	public TreePath getParentPath() {
		LinkedList<Long> nodes = new LinkedList<>(path);
		
		// Remove the last node, if empty it does nothing
		nodes.pollLast();
		return new HazelTreePath(nodes);
	}
	
	@Override
	public EncodingMatrix getEncodingMatrix() {
		return encoding;
	}

	private HazelEncodingMatrix computeEncoding(List<Long> nodes)
	{
		List<TreeMatrix> product = new ArrayList<>();
		
		product.add(new HazelOriginMatrix());
		nodes.forEach(node -> product.add(new HazelNodeMatrix(node)));
		
		MatrixOp op = new ExactMatrixOp();
		return new HazelEncodingMatrix(op.multiply(product));
	}
	
}
