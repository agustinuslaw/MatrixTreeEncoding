package matrixtree.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import matrixtree.structure.MatrixTreeNode;
import matrixtree.structure.MutableMatrixTreeNode;
import matrixtree.test.algorithm.PrimeTreeBuilder;
import matrixtree.test.algorithm.SieveFactorization;
import matrixtree.test.database.TestTreeTableManager;
import matrixtree.test.model.NamedInteger;
import matrixtree.test.model.QueryResult;

/**
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
class Sandbox {
	@Test
	void testDatabase() throws SQLException {
		String table = "HazelTree";
		String name = "524288";
		int trees = 200000;
		
		System.out.println("\n ----- Create Data ----- \n");
		TestTreeTableManager.dropTable(table);
		TestTreeTableManager.createTestTable(table);
		TestTreeTableManager.generateAndInsertBinaryTrees(table, trees);
		TestTreeTableManager.createComputedColumns(table);
		
		System.out.println("\n ----- Statistics ----- \n");
		int count = TestTreeTableManager.getRowCount(table);
		System.out.println("Trees: " + trees);
		System.out.println("Query: " + name);
		System.out.println("Table Rows: " + count);
		
		System.out.println("\n ----- Selection Comparison ----- \n");
		TestTreeTableManager.dropBoundIndexes(table);
		QueryResult mat = TestTreeTableManager.selectMatrices(table, name);
		QueryResult matPrefilter = TestTreeTableManager.selectMatricesWithPrefilter(table, name);
		
		TestTreeTableManager.createBoundIndexesForTestTable(table);
		QueryResult matIndex = TestTreeTableManager.selectMatricesWithIndexedColumn(table, name);
		QueryResult cte = TestTreeTableManager.selectCteRecursive(table, name);
		
		System.out.println("\n ----- Tree ----- \n");
		System.out.println("Queried Tree for name:" + name);
		cte.names.forEach(System.out::println);
		
		/* assert */
		// both method should be equal
		assertEquals(cte.names, mat.names);
		assertEquals(cte.names, matPrefilter.names);
		assertEquals(cte.names, matIndex.names);
		// should countain a result
		assertFalse(cte.names.isEmpty());

	}

	void testTree() {
		int number = 8388605;

		SieveFactorization sieve = new SieveFactorization(number);
		MutableMatrixTreeNode<NamedInteger> root = PrimeTreeBuilder.buildBinary(number, sieve.factorize(number));

		root.stream().map(MatrixTreeNode::lineRepresentation).forEach(System.out::println);
	}
	
}
