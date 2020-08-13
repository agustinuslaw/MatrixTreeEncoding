package matrixtree.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import matrixtree.matrices.HazelPathMatrix;

/**
 * @author Agustinus Lawandy
 * @since 2020-08-09
 */
class MutableMatrixTreeNodeTest {

	/**
	 * Building tree using indexes from root.
	 */
	@Test
	void testBuildTreeWithIndex() {
		MutableMatrixTreeNode<Integer> root = new ListHazelMatrixTreeNode<>//
		(/* parent: root */ null, /* elem */ 100, /* idx */ 50);

		MutableMatrixTreeNode<Integer> lvl1e4 = root.insert(/* elem */4, /* matrix idx */1);
		lvl1e4.insert(2, 1);
		lvl1e4.insert(2, 2);

		MutableMatrixTreeNode<Integer> lvl1e25 = root.insert(25, 2);
		lvl1e25.insert(5, 1);
		lvl1e25.insert(5, 2);

		/* asserts */
		assertEquals(2, root.getChildCount());
		assertEquals(2, root.getChildAt(1).getChildCount());
		assertEquals(2, root.getChildAt(2).getChildCount());
		
		assertEquals(4, root.getChildAt(1).getElement());
		assertEquals(2, root.getChildAt(1).getChildAt(1).getElement());
		assertEquals(2, root.getChildAt(1).getChildAt(2).getElement());
		assertEquals(25, root.getChildAt(2).getElement());
		assertEquals(5, root.getChildAt(2).getChildAt(1).getElement());
		assertEquals(5, root.getChildAt(2).getChildAt(2).getElement());
	}

	/**
	 * Building tree using indexes from root.
	 */
	@Test
	void testBuildTreeSimple() {
		MutableMatrixTreeNode<String> root = new ListHazelMatrixTreeNode<>//
		(/* parent: root */ null, /* elem */ "100", /* idx 9B */ 9000000000000L);

		MutableMatrixTreeNode<String> lvl1e4 = root.add(/* elem */"4");
		lvl1e4.add("2");
		lvl1e4.add("2");

		MutableMatrixTreeNode<String> lvl1e25 = root.add("25");
		lvl1e25.add("5");
		lvl1e25.add("5");

		/* asserts */
		assertEquals(2, root.getChildCount());
		assertEquals(2, root.getChildAt(1).getChildCount());
		assertEquals(2, root.getChildAt(2).getChildCount());
		
		assertEquals("4", root.getChildAt(1).getElement());
		assertEquals("2", root.getChildAt(1).getChildAt(1).getElement());
		assertEquals("2", root.getChildAt(1).getChildAt(2).getElement());
		assertEquals("25", root.getChildAt(2).getElement());
		assertEquals("5", root.getChildAt(2).getChildAt(1).getElement());
		assertEquals("5", root.getChildAt(2).getChildAt(2).getElement());
	}

	@Test
	void testInsertNode() {

		HazelPathMatrix mat_50_root = new HazelPathMatrix(50, 51, 1, 1);
		Integer elem_50_root = 100;

		HazelPathMatrix mat_50_1 = new HazelPathMatrix(101, 152, 2, 3);
		Integer elem_50_1 = 4;

		MutableMatrixTreeNode<Integer> root = new ListHazelMatrixTreeNode<>(null, elem_50_root, mat_50_root);
		MutableMatrixTreeNode<Integer> child = new ListHazelMatrixTreeNode<>(null, elem_50_1, mat_50_1);

		root.insert(child);

		root.getChildAt(1);

	}

}
