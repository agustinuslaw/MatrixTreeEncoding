package matrixtree.matrices;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import matrixtree.model.Ancestors;
import matrixtree.model.HazelTreePath;
import matrixtree.model.TreePath;

/**
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
class PathMatrixTest {

	// TODO add JUnit parameter for parametric testing
	// TODO test each defined constructor for equality

	@Test
	void parent() {
		PathMatrix parent = new HazelTreePath(2L, 7L, 5L).computePathMatrix();
		PathMatrix node = new HazelTreePath(2L, 7L, 5L, 3L).computePathMatrix();

		PathMatrix parent2 = new HazelTreePath(3L).computePathMatrix();
		PathMatrix node2 = new HazelTreePath(3L, 1L).computePathMatrix();

		assertEquals(parent, node.computeParentMatrix());
		assertEquals(parent2, node2.computeParentMatrix());
	}

	@Test
	void root() {
		PathMatrix root = new HazelTreePath(2L).computePathMatrix();
		PathMatrix node = new HazelTreePath(2L, 7L, 5L, 3L).computePathMatrix();

		assertEquals(root, node.computeRootMatrix());
	}

	@Test
	void path() {
		TreePath path = new HazelTreePath(2L, 7L, 5L, 3L);
		PathMatrix m = path.computePathMatrix();
		Ancestors ancestor = m.computeAncestors();

		PathMatrix mat = new HazelPathMatrix(101, 152, 2, 3);
		Ancestors ancestor2 = mat.computeAncestors();

		assertEquals(path, ancestor.getTreePath());
		assertEquals(new HazelTreePath(50L, 1L), ancestor2.getTreePath());
	}

	@Test
	void sibling() {
		PathMatrix leftSibling = new HazelTreePath(2L, 4L, 1L).computePathMatrix();
		PathMatrix current = new HazelTreePath(2L, 4L, 2L).computePathMatrix();
		PathMatrix rightSibling = new HazelTreePath(2L, 4L, 3L).computePathMatrix();
		PathMatrix tenthSibling = new HazelTreePath(2L, 4L, 12L).computePathMatrix();

		assertEquals(leftSibling, current.computeSibling(-1));
		assertEquals(current, current.computeSibling(0));
		assertEquals(rightSibling, current.computeSibling(1));
		assertEquals(tenthSibling, current.computeSibling(10));
	}

	@Test
	void child() {
		PathMatrix current = new HazelTreePath(2L, 4L, 2L).computePathMatrix();
		PathMatrix child4 = new HazelTreePath(2L, 4L, 2L, 4L).computePathMatrix();
		PathMatrix child2 = new HazelTreePath(2L, 4L, 2L, 2L).computePathMatrix();

		assertEquals(child4, current.computeChild(4));
		assertEquals(child2, current.computeChild(2));
	}

	@Test
	void depth() {
		PathMatrix current = new HazelTreePath(2L, 4L, 2L).computePathMatrix();

		assertEquals(3, current.computeDepth());
	}

	@Test
	void index() {
		PathMatrix current = new HazelTreePath(2L, 4L, 7L).computePathMatrix();

		assertEquals(7L, current.computeIndex());
	}
}
