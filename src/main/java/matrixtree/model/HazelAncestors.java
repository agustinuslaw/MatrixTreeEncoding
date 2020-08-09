package matrixtree.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import matrixtree.matrices.HazelPathMatrix;
import matrixtree.matrices.PathMatrix;

public class HazelAncestors implements Ancestors {

	private static final long serialVersionUID = 3330836727367707152L;
	private final HazelTreePath treePath;
	private final LinkedList<HazelPathMatrix> ancestorMatrices;

	public HazelAncestors(HazelTreePath treePath, List<HazelPathMatrix> ancestorMatrices) {
		this.treePath = treePath;
		this.ancestorMatrices = new LinkedList<>(ancestorMatrices);
	}

	@Override
	public HazelTreePath getTreePath() {
		return treePath;
	}

	@Override
	public List<PathMatrix> getAncestorMatrices() {
		return Collections.unmodifiableList(ancestorMatrices);
	}

	@Override
	public HazelPathMatrix getPathMatrix() {
		return ancestorMatrices.getLast();
	}

	@Override
	public int compareTo(Ancestors o) {
		return this.getTreePath().compareTo(o.getTreePath());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((treePath == null) ? 0 : treePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HazelAncestors other = (HazelAncestors) obj;
		if (treePath == null) {
			if (other.treePath != null)
				return false;
		} else if (!treePath.equals(other.treePath))
			return false;
		return true;
	}

}
