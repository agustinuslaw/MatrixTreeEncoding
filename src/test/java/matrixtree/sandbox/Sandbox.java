package matrixtree.sandbox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.google.common.base.Stopwatch;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkData;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import de.bytefish.jsqlserverbulkinsert.mapping.AbstractMapping;
import de.bytefish.jsqlserverbulkinsert.records.SqlServerBulkData;
import matrixtree.structure.MatrixTreeNode;
import matrixtree.structure.MutableMatrixTreeNode;
import matrixtree.test.algorithm.PrimeTreeBuilder;
import matrixtree.test.algorithm.SieveFactorization;
import matrixtree.test.database.NamedIntegerTreeMapping;
import matrixtree.test.model.NamedInteger;

/**
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
class Sandbox {
	@Test
	void testInsert() {
		// replace user and pass
		String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Sandbox;user=example;password=example";
		String table = "HazelTreeSmall";
		try (Connection connection = DriverManager.getConnection(connectionUrl);) {
			int currentRowQuantity = getRowCount(connection, table);

			Set<MatrixTreeNode<NamedInteger>> set;
			// reset state
			deleteFrom(connection, table);

			// testing
			set = PrimeTreeBuilder.buildBinariesAsSet(2, 3);
			bulkInsert(connection, table, set.iterator());

			set = PrimeTreeBuilder.buildBinariesAsSet(3, 500000);
			bulkInsert(connection, table, set.iterator());

//			final int delta = 500000;
//			for (int i = 500000; i < 8000000; i += delta) {
//				set = PrimeTreeBuilder.buildBinariesAsSet(i, i + delta);
//				bulkInsert(connection, table, set.iterator());
//				System.gc();
//			}

			int updatedRowQuantity = getRowCount(connection, table);
			int change = updatedRowQuantity - currentRowQuantity;
			System.out.println("added " + change + " rows with total: " + updatedRowQuantity);
		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void bulkInsert(Connection connection, String table, Iterator<MatrixTreeNode<NamedInteger>> entities) {
		System.out.println("bulkInsert() started");
		Stopwatch sqlInsertStopwatch = Stopwatch.createStarted();

		AbstractMapping<MatrixTreeNode<NamedInteger>> mapping = new NamedIntegerTreeMapping(table);
		try (SQLServerBulkCopy sqlServerBulkCopy = new SQLServerBulkCopy(connection)) {
			// 1 mil table ~ 6 mil insert ~ 72 sec
			SQLServerBulkCopyOptions option = new SQLServerBulkCopyOptions();
			option.setBulkCopyTimeout(1200);

			sqlServerBulkCopy.setBulkCopyOptions(option);
			sqlServerBulkCopy.setDestinationTableName(mapping.getTableDefinition().GetFullQualifiedTableName());
			ISQLServerBulkData record = new SqlServerBulkData<MatrixTreeNode<NamedInteger>>(mapping.getColumns(),
					entities);
			sqlServerBulkCopy.writeToServer(record);
		} catch (SQLServerException e) {
			System.out.println("bulkInsert() error:" + e.getLocalizedMessage());
			throw new RuntimeException(e);
		} finally {
			System.out.println("bulkInsert() took " + sqlInsertStopwatch.elapsed(TimeUnit.SECONDS) + " sec");
		}

	}

	public void deleteFrom(Connection connection, String table) throws SQLException {
		System.out.println("delete table: " + table);
		try (Statement s = connection.createStatement();) {
			s.execute("delete from " + table);
		}
	}

	private int getRowCount(Connection connection, String table) throws SQLException {
		try (Statement s = connection.createStatement();
				ResultSet r = s.executeQuery("select count(*) as total from " + table);) {
			r.next();
			return r.getInt("total");
		}
	}

	
	void testTree() {
		int number = 8388605;

		SieveFactorization sieve = new SieveFactorization(number);
		MutableMatrixTreeNode<NamedInteger> root = PrimeTreeBuilder.buildBinary(number, sieve.factorize(number));

		root.stream().map(MatrixTreeNode::lineRepresentation).forEach(System.out::println);
	}

}
