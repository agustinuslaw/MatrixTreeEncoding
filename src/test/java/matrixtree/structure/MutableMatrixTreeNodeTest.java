package matrixtree.structure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import matrixtree.matrices.HazelPathMatrix;
import matrixtree.matrices.PathMatrix;

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
		MutableMatrixTreeNode<Integer> root = new OrderedHazelMatrixTreeNode<>//
		(/* parent: root */ null, /* elem */ 100, /* idx */ 50);

		MutableMatrixTreeNode<Integer> lvl1e4 = root.insert(/* elem */4, /* matrix idx */1);
		lvl1e4.insert(2, 1);
		lvl1e4.insert(2, 2);

		MutableMatrixTreeNode<Integer> lvl1e25 = root.insert(25, 2);
		lvl1e25.insert(5, 1);
		lvl1e25.insert(5, 2);

		assertEquals(4, root.getChildAt(1).getElement());
		assertEquals(2, root.getChildAt(1).getChildAt(2).getElement());
		assertEquals(25, root.getChildAt(2).getElement());
		assertEquals(5, root.getChildAt(2).getChildAt(1).getElement());
	}

	/**
	 * Building tree using indexes from root.
	 */
	@Test
	void testBuildTreeSimple() {
		MutableMatrixTreeNode<String> root = new OrderedHazelMatrixTreeNode<>//
		(/* parent: root */ null, /* elem */ "100", /* idx 9B */ 9000000000000L);

		MutableMatrixTreeNode<String> lvl1e4 = root.add(/* elem */"4");
		lvl1e4.add("2");
		lvl1e4.add("2");

		MutableMatrixTreeNode<String> lvl1e25 = root.add("25");
		lvl1e25.add("5");
		lvl1e25.add("5");

		assertEquals("4", root.getChildAt(1).getElement());
		assertEquals("2", root.getChildAt(1).getChildAt(2).getElement());
		assertEquals("25", root.getChildAt(2).getElement());
		assertEquals("5", root.getChildAt(2).getChildAt(1).getElement());
	}

	/**
	 * This one is of special interest. Because this is how it will build when data comes from the database table itself.
	 */
	@Test
	void testBuildFromPathMatricesViaSorting() {

		/*	
		   Tree
		   Node{elem:100, idx:50, mat:HazelPathMatrix{{50,51},{1,1}}, interval:[50,51)}
			   Node{elem:4, idx:1, mat:HazelPathMatrix{{101,152},{2,3}}, interval:[50.5,50.6667)}
			      Node{elem:2, idx:1, mat:HazelPathMatrix{{253,405},{5,8}}, interval:[50.6,50.625)}
			      Node{elem:2, idx:2, mat:HazelPathMatrix{{405,557},{8,11}}, interval:[50.625,50.6364)}
			   Node{elem:25, idx:2, mat:HazelPathMatrix{{152,203},{3,4}}, interval:[50.6667,50.75)}
			      Node{elem:5, idx:1, mat:HazelPathMatrix{{355,558},{7,11}}, interval:[50.7143,50.7273)}
			      Node{elem:5, idx:2, mat:HazelPathMatrix{{558,761},{11,15}}, interval:[50.7273,50.7333)}*/

		/* 1. Query raw data from some table */

		// Querying Path from some ID
		HazelPathMatrix mat_50_root = new HazelPathMatrix(50, 51, 1, 1);
		// Querying Content from some ID
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

		/*
		 * 2. Data as nodes. Root is known at this moment. Order of nodes is unknown,
		 * purposefully jumbled. Sort the nodes using natural ordering. This is important because the tree
		 * must be created breadth first BFS.
		 */

		Set<MutableMatrixTreeNode<Integer>> sorted = new TreeSet<>();
		sorted.add(new OrderedHazelMatrixTreeNode<>(null, elem_50_1, mat_50_1));
		sorted.add(new OrderedHazelMatrixTreeNode<>(null, elem_50_2_1, mat_50_2_1));
		sorted.add(new OrderedHazelMatrixTreeNode<>(null, elem_50_2, mat_50_2));
		sorted.add(new OrderedHazelMatrixTreeNode<>(null, elem_50_root, mat_50_root));
		sorted.add(new OrderedHazelMatrixTreeNode<>(null, elem_50_1_1, mat_50_1_1));
		sorted.add(new OrderedHazelMatrixTreeNode<>(null, elem_50_1_2, mat_50_1_2));
		sorted.add(new OrderedHazelMatrixTreeNode<>(null, elem_50_2_2, mat_50_2_2));

//		sorted.forEach(System.out::print); // result below

		/* 3. Build from flat to tree structure using buildTree() */
		Iterator<MutableMatrixTreeNode<Integer>> iter = sorted.iterator();
		MutableMatrixTreeNode<Integer> rootNode = iter.next();
		while (iter.hasNext()) {
			MutableMatrixTreeNode<Integer> child = iter.next();

			rootNode.buildTree(child);
		}
	}

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

		/* 1. Query raw data from some table */

		// Querying Path from some ID
		HazelPathMatrix mat_50_root = new HazelPathMatrix(50, 51, 1, 1);
		// Querying Content from some ID
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

		/*
		 * 2. Data as map of nodes. Root is known at this moment. Order of nodes is unknown,
		 * purposefully jumbled. 
		 * 
		 * Order is completely irrelevant with this method.
		 */
		Map<PathMatrix, MutableMatrixTreeNode<Integer>> nodeMap = new HashMap<>();
		nodeMap.put(mat_50_1, new OrderedHazelMatrixTreeNode<>(null, elem_50_1, mat_50_1));
		nodeMap.put(mat_50_2_1, new OrderedHazelMatrixTreeNode<>(null, elem_50_2_1, mat_50_2_1));
		nodeMap.put(mat_50_2, new OrderedHazelMatrixTreeNode<>(null, elem_50_2, mat_50_2));
		nodeMap.put(mat_50_root, new OrderedHazelMatrixTreeNode<>(null, elem_50_root, mat_50_root));
		nodeMap.put(mat_50_1_1, new OrderedHazelMatrixTreeNode<>(null, elem_50_1_1, mat_50_1_1));
		nodeMap.put(mat_50_1_2, new OrderedHazelMatrixTreeNode<>(null, elem_50_1_2, mat_50_1_2));
		nodeMap.put(mat_50_2_2, new OrderedHazelMatrixTreeNode<>(null, elem_50_2_2, mat_50_2_2));

		/* 3. Build the tree from the map via parental relations. */
		for (MutableMatrixTreeNode<Integer> node : nodeMap.values())
		{
			PathMatrix key = node.getPathMatrix();
			PathMatrix parentKey = key.computeParentMatrix();
			
			if(parentKey != null)
			{
				MutableMatrixTreeNode<Integer> parentNode = nodeMap.get(parentKey);
				parentNode.insert(node);
			}
		}

		/* 4. The root is always known, can also always be computed via computeRootMatrix */
		MutableMatrixTreeNode<Integer> root =  nodeMap.get(mat_50_root);
	}

	@Test
	void testInsertNode() {

		HazelPathMatrix mat_50_root = new HazelPathMatrix(50, 51, 1, 1);
		Integer elem_50_root = 100;

		HazelPathMatrix mat_50_1 = new HazelPathMatrix(101, 152, 2, 3);
		Integer elem_50_1 = 4;

		MutableMatrixTreeNode<Integer> root = new OrderedHazelMatrixTreeNode<>(null, elem_50_root, mat_50_root);
		MutableMatrixTreeNode<Integer> child = new OrderedHazelMatrixTreeNode<>(null, elem_50_1, mat_50_1);

		root.buildTree(child);

		root.getChildAt(1);

	}

}
