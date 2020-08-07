package matrixtree.model;

import java.util.*;

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
	
	public HazelTreePath(Long... nodes) {
		super();
		this.path = unmodifiableList(Arrays.asList(nodes));
	}
	
	public HazelTreePath(List<Long> nodes) {
		super();
		this.path = unmodifiableList(nodes);
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
    public int depth() {
        return path.size();
    }

    @Override
	public PathMatrix computePathMatrix() {
		List<StandardMatrix> product = new ArrayList<>();

		product.add(new HazelOriginMatrix());
		path.forEach(node -> product.add(new HazelNodeMatrix(node)));

		MatrixOp op = new ExactMatrixOp();
		return new HazelPathMatrix(op.multiply(product));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		HazelTreePath that = (HazelTreePath) o;

		return Objects.equals(path, that.path);
	}

	@Override
	public int hashCode() {
		return path != null ? path.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "HazelTreePath{" + path.stream().map(Objects::toString).reduce((x, y) -> x+"."+y) + '}';
	}
}
