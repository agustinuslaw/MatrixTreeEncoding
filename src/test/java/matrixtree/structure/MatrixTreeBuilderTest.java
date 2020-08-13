package matrixtree.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import matrixtree.matrices.HazelPathMatrix;

class MatrixTreeBuilderTest {

	/**
	 * This is simpler.
	 */
	@Test
	void testBuildFromPathMatricesViaMapping() {

		/*	
		   Tree
		   Node{elem:100, idx:50, mat:HazelPathMatrix{{50,51},{1,1}}, interval:[50,51)}
			   Node{elem:4, idx:1, mat:HazelPathMatrix{{101,152},{2,3}}, interval:[50.5,50.6667)}
			      Node{elem:2, idx:1, mat:HazelPathMatrix{{253,405},{5,8}}, interval:[50.6,50.625)}
			      Node{elem:2, idx:2, mat:HazelPathMatrix{{405,557},{8,11}}, interval:[50.625,50.6364)}
			   Node{elem:25, idx:2, mat:HazelPathMatrix{{152,203},{3,4}}, interval:[50.6667,50.75)}
			      Node{elem:5, idx:1, mat:HazelPathMatrix{{355,558},{7,11}}, interval:[50.7143,50.7273)}
			      Node{elem:5, idx:2, mat:HazelPathMatrix{{558,761},{11,15}}, interval:[50.7273,50.7333)}*/

		/* arrange, data from some data source maybe database */

		// querying path and content from some table
		HazelPathMatrix mat_50_root = new HazelPathMatrix(50, 51, 1, 1);
		Integer elem_50_root = 100;

		HazelPathMatrix mat_50_1 = new HazelPathMatrix(101, 152, 2, 3);
		Integer elem_50_1 = 4;

		HazelPathMatrix mat_50_1_1 = new HazelPathMatrix(253, 405, 5, 8);
		Integer elem_50_1_1 = 2;

		HazelPathMatrix mat_50_1_2 = new HazelPathMatrix(405, 557, 8, 11);
		Integer elem_50_1_2 = 2;

		HazelPathMatrix mat_50_2 = new HazelPathMatrix(152, 203, 3, 4);
		Integer elem_50_2 = 25;

		HazelPathMatrix mat_50_2_1 = new HazelPathMatrix(355, 558, 7, 11);
		Integer elem_50_2_1 = 5;

		HazelPathMatrix mat_50_2_2 = new HazelPathMatrix(558, 761, 11, 15);
		Integer elem_50_2_2 = 5;

		/* act */
		MutableMatrixTreeNode<Integer> root = MatrixTreeBuilder//
				.of(Integer.class)//
				.put(new ListHazelMatrixTreeNode<>(null, elem_50_1, mat_50_1))//
				.put(new ListHazelMatrixTreeNode<>(null, elem_50_2_1, mat_50_2_1))//
				.put(new ListHazelMatrixTreeNode<>(null, elem_50_2, mat_50_2))//
				.put(new ListHazelMatrixTreeNode<>(null, elem_50_root, mat_50_root))//
				.put(new ListHazelMatrixTreeNode<>(null, elem_50_1_1, mat_50_1_1))//
				.put(new ListHazelMatrixTreeNode<>(null, elem_50_1_2, mat_50_1_2))//
				.put(new ListHazelMatrixTreeNode<>(null, elem_50_2_2, mat_50_2_2))//
				.buildTree();

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

}
