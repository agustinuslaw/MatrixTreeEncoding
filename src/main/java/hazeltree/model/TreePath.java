package hazeltree.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hazeltree.computation.MatrixOp;
import hazeltree.computation.SimpleMatrixOp;
import hazeltree.matrices.EncodingMatrix;
import hazeltree.matrices.HazelMatrix;
import hazeltree.matrices.NodeMatrix;
import hazeltree.matrices.OriginMatrix;

public class TreePath  {
	private final List<Long> path;
	private final EncodingMatrix encoding;
	
	public TreePath(Long... nodes) {
		super();
		this.path = Collections.unmodifiableList(Arrays.asList(nodes));
		this.encoding = computeEncoding(this.path);
	}
	
	public TreePath(List<Long> path) {
		super();
		this.path = Collections.unmodifiableList(path);
		this.encoding = computeEncoding(this.path);
	}
	
	public List<Long> getPath() {
		return path;
	}

	public EncodingMatrix getEncoding() {
		return encoding;
	}

	private EncodingMatrix computeEncoding(List<Long> nodes)
	{
		List<HazelMatrix> product = new ArrayList<>();
		
		product.add(new OriginMatrix());
		nodes.forEach(node -> product.add(new NodeMatrix(node)));
		
		MatrixOp op = new SimpleMatrixOp();
		return new EncodingMatrix(op.multiply(product));
	}
	
}
