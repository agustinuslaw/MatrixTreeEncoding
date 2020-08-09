package matrixtree.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import matrixtree.computation.ExactMatrixOp;
import matrixtree.computation.MatrixOp;
import matrixtree.matrices.HazelNodeMatrix;
import matrixtree.matrices.HazelOriginMatrix;
import matrixtree.matrices.HazelPathMatrix;
import matrixtree.matrices.StandardMatrix;

/**
 * @author Agustinus Lawandy
 */
public class HazelTreePath implements TreePath {
	private static final long serialVersionUID = 1288241696205292392L;
	private final List<Long> path;

	public HazelTreePath(Long... nodes) {
		super();
		this.path = unmodifiableList(Arrays.asList(nodes));
	}

	public HazelTreePath(List<Long> nodes) {
		super();
		this.path = unmodifiableList(nodes);
	}

	@Override
	public List<Long> asList() {
		return path;
	}

	@Override
	public HazelTreePath getParentPath() {
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
	public HazelPathMatrix computePathMatrix() {
		List<StandardMatrix> product = new ArrayList<>();

		product.add(new HazelOriginMatrix());
		path.forEach(node -> product.add(new HazelNodeMatrix(node)));

		MatrixOp op = new ExactMatrixOp();
		return new HazelPathMatrix(op.multiply(product));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		HazelTreePath that = (HazelTreePath) o;

		return Objects.equals(path, that.path);
	}

	@Override
	public int hashCode() {
		return path != null ? path.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "HazelTreePath{" + path.stream().map(Objects::toString).reduce((x, y) -> x + "." + y) + '}';
	}

	@Override
	public int compareTo(TreePath o) {
		// depth ordering then lexicographic
		
		if (this.depth() != o.depth()) {
			return this.depth() - o.depth();
		} else {
			for (int j = 0; j < this.depth(); j++) {
				if (!this.asList().get(j).equals(o.asList().get(j)))
					return (int) (this.asList().get(j) - o.asList().get(j));
			}

		}

		return 0;
	}

}
