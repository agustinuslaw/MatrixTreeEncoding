package matrixtree.test.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.microsoft.sqlserver.jdbc.ISQLServerBulkData;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import de.bytefish.jsqlserverbulkinsert.mapping.AbstractMapping;
import de.bytefish.jsqlserverbulkinsert.records.SqlServerBulkData;
import matrixtree.structure.MatrixTreeNode;
import matrixtree.test.algorithm.PrimeTreeBuilder;
import matrixtree.test.model.NamedInteger;
import matrixtree.test.model.QueryResult;
import matrixtree.validation.Precondition;

/**
 * Manages the creation and management of the database test table.
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-24
 */
public class TestTreeTableManager {

	private static String url = "jdbc:sqlserver://localhost:1433;databaseName=Sandbox;user=example;password=example";

	public static void createTestTable(String table) throws SQLException {
		System.out.println("TestTreeTableManager.createTestTable:" + table);
		String query = "	create table " + table + " (\n" + //
				"	name nvarchar(150) primary key, \n" + //
				"	parent nvarchar(150), \n" + //
				"	number int not null, \n" + //

				"	e11 bigint not null,\n" + //
				"	e21 bigint not null,\n" + //
				"	e12 bigint not null,\n" + //
				"	e22 bigint not null\n" + //
				");";
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			s.execute(query);
		}
	}

	public static void createComputedColumns(String table) throws SQLException {
		System.out.println("TestTreeTableManager.createBoundIndexesForTestTable:" + table);
		String query = "	alter table " + table + " add lower_bound as e11*1.0/e21 persisted;"//
				+ "	alter table " + table + " add upper_bound as e12*1.0/e22 persisted;";
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			Stopwatch stopwatch = Stopwatch.createStarted();
			s.execute(query);//
			System.out.println(
					"createBoundIndexesForTestTable() took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
		}
	}

	public static void createBoundIndexesForTestTable(String table) throws SQLException {
		System.out.println("TestTreeTableManager.createBoundIndexesForTestTable:" + table);
		String query = "	create nonclustered index idx_" + table.toLowerCase() + "_lower on " + table
				+ " (lower_bound asc);"//
				+ "	create nonclustered index idx_" + table.toLowerCase() + "_upper on " + table
				+ " (upper_bound asc);";
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			Stopwatch stopwatch = Stopwatch.createStarted();
			s.execute(query);//
			System.out.println(
					"createBoundIndexesForTestTable() took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
		}
	}

	public static QueryResult selectCteRecursive(String table, String name) throws SQLException {
		System.out.println("TestTreeTableManager.selectCteRecursive:" + table + " for:" + name);
		String query = String.format("	with _CTE (name, parent, number, e11, e21, e12, e22, depth)\r\n"
				+ "	as	(	\r\n" + "	select name, parent, number, e11, e21, e12, e22, 1 from %1$s \r\n"
				+ "	where name = '%2$s'  -- predicate uniquely identifying a node as root\r\n" + "		\r\n"
				+ "	union all\r\n"
				+ "	select child.name, child.parent, child.number, child.e11, child.e21, child.e12, child.e22, cte.depth+1 from _CTE cte\r\n"
				+ "	inner join %1$s child on (cte.name = child.parent)\r\n" + "	)\r\n"
				+ "	select name, number, depth \r\n" //
				+ "	from _CTE \r\n" + "	option(MAXRECURSION 50);", table, name);
		System.out.println(query);
		return queryNames(query);
	}

	public static QueryResult selectMatrices(String table, String name) throws SQLException {
		System.out.println("TestTreeTableManager.selectMatrices:" + table + " for:" + name);
		String query = String.format("	select child.*\r\n" + "	from %1$s child, %1$s node   \r\n"
				+ "	where node.e11*child.e21 <= child.e11*node.e21\r\n"
				+ "	and child.e12*node.e22 <= node.e12*child.e22 \r\n"
				+ "	and node.name = '%2$s'  -- predicate uniquely identifying a node ", table, name);
		System.out.println(query);
		return queryNames(query);
	}

	public static QueryResult selectMatricesWithIndexedColumn(String table, String name) throws SQLException {
		System.out.println(
				"TestTreeTableManager.selectMatricesWithIndexedColumn(String, String):" + table + " for:" + name);
		String query = String.format(
				"	select child.*\r\n" + "	from %1$s child, %1$s node \r\n" + "	-- approximate for indexing\r\n"
						+ "	where child.lower_bound\r\n" + "	between node.lower_bound and node.upper_bound\r\n"
						+ "	-- exact\r\n" + "	and node.e11*child.e21 <= child.e11*node.e21 \r\n"
						+ "	and child.e12*node.e22 <= node.e12*child.e22 \r\n"
						+ "	-- predicate uniquely identifying a node \r\n" + "	and node.name = '%2$s';",
				table, name);
		System.out.println(query);
		return queryNames(query);
	}

	public static QueryResult selectMatricesWithPrefilter(String table, String name) throws SQLException {
		System.out.println("TestTreeTableManager.selectMatricesWithPrefilter:" + table + " for:" + name);
		String query = String
				.format("	select child.*\r\n" + "	from %1$s child, %1$s node \r\n" + "	-- approximate \r\n"
						+ "	where child.e11/child.e21  \r\n" + "	between node.e11/node.e21 and node.e12/node.e22\r\n"
						+ "	-- exact\r\n" + "	and node.e11*child.e21 <= child.e11*node.e21 \r\n"
						+ "	and child.e12*node.e22 <= node.e12*child.e22 \r\n"
						+ "	-- predicate uniquely identifying a node \r\n" + "	and node.name = '%2$s';", table, name);
		System.out.println(query);
		return queryNames(query);
	}

	public static QueryResult queryNames(String query) throws SQLException {
		Stopwatch stopwatch = Stopwatch.createStarted();

		QueryResult qr = new QueryResult();
		try (Connection connection = DriverManager.getConnection(url);
				Statement s = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet result = s.executeQuery(query);) {
			System.out.println(result);
			for (result.first(); !result.isAfterLast(); result.next()) {
				qr.names.add(result.getObject("name").toString());
			}
		}
		qr.unit = TimeUnit.MILLISECONDS;
		qr.duration = stopwatch.elapsed(qr.unit);
		System.out.println("Query took: " + qr.duration + " milliseconds. \n");

		return qr;
	}

	public static void dropTable(String table) throws SQLException {
		System.out.println("TestTreeTableManager.dropTable:" + table);
		String query = "	drop table if exists dbo." + table;
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			s.execute(query);
		}
	}

	public static void dropBoundIndexes(String table) throws SQLException {
		System.out.println("TestTreeTableManager.dropBoundIndexes:" + table);
		String query = "	drop index if exists idx_" + table.toLowerCase() + "_lower on dbo." + table + ";\n"//
				+ "	drop index if exists idx_" + table.toLowerCase() + "_upper on dbo." + table + ";";
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			s.execute(query);
		}
	}

	/**
	 * Generates large amount of deep binary trees composed of prime factorizations.
	 * 
	 * @param table name
	 * @param n     to be created
	 */
	public static void generateAndInsertBinaryTrees(String table, int n) {
		generateAndInsertBinaryTrees(table, 2, n);
	}

	public static void generateAndInsertBinaryTrees(String table, int min, int max) {
		System.out.println("TestTreeTableManager.generateAndInsertBinaryTrees(" + table + ", " + max + ")");
		Precondition.checkArgument(max > 1, "n must be greater than 1");
		Precondition.checkArgument(min > 1, "min must be greater than 1");
		Precondition.checkArgument(max < 8000000, "n must be less than 8000000");

		// replace user and pass
		try (Connection connection = DriverManager.getConnection(url);) {
			int currentRowQuantity = getRowCount(connection, table);

			Set<MatrixTreeNode<NamedInteger>> set;

			NamedIntegerTreeMapping mapping = new NamedIntegerTreeMapping(table);

			// first batch from 3 -> delta
			final int delta = 500000;
			if (max < min + delta)
				set = PrimeTreeBuilder.buildBinariesAsSet(min, max);
			else
				set = PrimeTreeBuilder.buildBinariesAsSet(min, delta);
			bulkInsert(connection, table, set.iterator(), mapping);

			// next batches
			for (int i = delta; i < max; i += delta) {
				if (max >= i + delta)
					set = PrimeTreeBuilder.buildBinariesAsSet(i, i + delta);
				else
					set = PrimeTreeBuilder.buildBinariesAsSet(i, max);
				bulkInsert(connection, table, set.iterator(), mapping);

				// hint for gc to start cleaning
				System.gc();
			}

			int updatedRowQuantity = getRowCount(connection, table);
			int change = updatedRowQuantity - currentRowQuantity;
			System.out.println("added " + change + " rows with total: " + updatedRowQuantity);
		}
		// Handle any errors that may have occurred.
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bulk insert operation.
	 * 
	 * @param connection
	 * @param table
	 * @param entities
	 */
	public static <E> void bulkInsert(Connection connection, String table, Iterator<E> entities,
			AbstractMapping<E> mapping) {
		System.out.println("TestTreeTableManager.bulkInsert(" + table + ") started");
		Stopwatch sqlInsertStopwatch = Stopwatch.createStarted();

		try (SQLServerBulkCopy sqlServerBulkCopy = new SQLServerBulkCopy(connection)) {
			// 1 mil table ~ 6 mil insert ~ 72 sec
			SQLServerBulkCopyOptions option = new SQLServerBulkCopyOptions();
			option.setBulkCopyTimeout(240);

			sqlServerBulkCopy.setBulkCopyOptions(option);
			sqlServerBulkCopy.setDestinationTableName(mapping.getTableDefinition().GetFullQualifiedTableName());
			ISQLServerBulkData record = new SqlServerBulkData<E>(mapping.getColumns(), entities);
			sqlServerBulkCopy.writeToServer(record);
		} catch (SQLServerException e) {
			System.out.println("bulkInsert() error:" + e.getLocalizedMessage());
			throw new RuntimeException(e);
		} finally {
			System.out.println("bulkInsert() took " + sqlInsertStopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
		}

	}

	/**
	 * Delete the entire table. using default connection
	 * 
	 * @param table
	 * @throws SQLException
	 */
	public static void deleteFrom(String table) throws SQLException {
		System.out.println("TestTreeTableManager.deleteFrom(" + table + ")");
		try (Connection connection = DriverManager.getConnection(url);) {
			deleteFrom(connection, table);
		}
	}

	/**
	 * Delete the entire table. Using specified connection
	 * 
	 * @param table
	 * @throws SQLException
	 */
	public static void deleteFrom(Connection connection, String table) throws SQLException {
		System.out.println("deleteFrom: " + table);
		Stopwatch stopwatch = Stopwatch.createStarted();
		try (Statement s = connection.createStatement();) {
			s.execute("delete from " + table);
		}
		System.out.println("deleteFrom(" + table + ") took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
	}

	/**
	 * Get the row count of the table.
	 * 
	 * @param connection
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public static int getRowCount(Connection connection, String table) throws SQLException {
		try (Statement s = connection.createStatement();
				ResultSet r = s.executeQuery("select count(*) as total from " + table);) {
			r.next();
			return r.getInt("total");
		}
	}
	
	public static int getRowCount(String table) throws SQLException {
		System.out.println("TestTreeTableManager.getRowCount(" + table + ")");
		try (Connection connection = DriverManager.getConnection(url);) {
			return getRowCount(connection, table);
		}
	}
}
