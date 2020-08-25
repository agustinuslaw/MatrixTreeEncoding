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
		String tableView = "HazelTreeView";
		int trees = 3000000;
		String name = "131072";

		System.out.println("\n ----- Create Data ----- \n");
		int count = TestTreeTableManager.getRowCount(table);
		int prevTrees = TestTreeTableManager.getTreeCount(tableView);

		if (prevTrees < trees) {
//			TestTreeTableManager.dropTable(table);
//			TestTreeTableManager.createTestTable(table);
			TestTreeTableManager.generateAndInsertBinaryTrees(table, prevTrees, trees);
//			TestTreeTableManager.createMatrixComputedColumns(table);
//			TestTreeTableManager.createMatrixComputedIndexesForTestTable(table);
//			TestTreeTableManager.createMatrixElementIndexesForTestTable(table);
		}

		System.out.println("\n ----- Selection Comparison ----- \n");
		QueryResult cte = TestTreeTableManager.selectCteRecursive(table, name);
		QueryResult mat = TestTreeTableManager.selectMatrices(table, name);

		QueryResult matComputedIndex = TestTreeTableManager.selectMatricesWithIndexedColumn(tableView, name);
		QueryResult matComputedIndexApprox = TestTreeTableManager.selectMatricesWithIndexedColumnApproximate(tableView,
				name);

		System.out.println("\n ----- Tree ----- \n");
		System.out.println("Queried Tree for name:" + name);
		mat.names.forEach(System.out::println);

		System.out.println("\n ----- Selection Statistics ----- \n");
		printSelectionStat(cte);
		printSelectionStat(mat);
		printSelectionStat(matComputedIndex);
		printSelectionStat(matComputedIndexApprox);

		System.out.println("Table Rows: " + count + " rows");
		try {
			System.out.println("Maximum Depth: " + (int) Math.floor(Math.log(Double.valueOf(name)) / Math.log(2.0)));
		} catch (Exception ex) {
			System.out.println("Can't compute max depth, check 'name' is a valid number");
		}

		/* assert */
		// all method should be equal
		assertEquals(mat.names, matComputedIndex.names);
		assertEquals(mat.names, cte.names);
		assertEquals(mat.names, matComputedIndexApprox.names);
		// should countain a result
		assertFalse(mat.names.isEmpty());

	}

	private void printSelectionStat(QueryResult qr) {
		if (qr.errorMessage.isEmpty())
			System.out.println(qr.method + ": " + qr.duration + " " + qr.unit);
		else
			System.out.println(qr.method + ": " + qr.errorMessage);
	}

	void testTree() {
		int number = 8388605;

		SieveFactorization sieve = new SieveFactorization(number);
		MutableMatrixTreeNode<NamedInteger> root = PrimeTreeBuilder.buildBinary(number, sieve.factorize(number));

		root.stream().map(MatrixTreeNode::lineRepresentation).forEach(System.out::println);
	}

}
