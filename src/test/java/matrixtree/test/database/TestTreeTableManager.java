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

	public static void createMatrixComputedColumns(String table) throws SQLException {
		System.out.println("TestTreeTableManager.createBoundIndexesForTestTable:" + table);
		String query = "	alter table " + table + " add lower as e11*1.0/e21 persisted;"//
				+ "	alter table " + table + " add upper as e12*1.0/e22 persisted;";
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			Stopwatch stopwatch = Stopwatch.createStarted();
			s.execute(query);//
			System.out.println(
					"createBoundIndexesForTestTable() took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
		}
	}

	public static void createMatrixComputedIndexesForTestTable(String table) throws SQLException {
		System.out.println("TestTreeTableManager.createMatrixComputedIndexesForTestTable:" + table);
		String query = String.format(
				"	create nonclustered index idx_%2$s_lower on %1$s ( lower asc);\r\n"
						+ "	create nonclustered index idx_%2$s_upper on %1$s ( upper asc);",
				table, table.toLowerCase());
		execute(query);
	}

	public static void createMatrixElementIndexesForTestTable(String table) throws SQLException {
		System.out.println("TestTreeTableManager.createMatrixComputedIndexesForTestTable:" + table);
		String query = String.format("	create nonclustered index idx_%2$s_e11 on %1$s ( e11 asc);\r\n" + //
				"	create nonclustered index idx_%2$s_e12 on %1$s ( e12 asc);\r\n" + //
				"	create nonclustered index idx_%2$s_e21 on %1$s ( e21 asc);\r\n" + //
				"	create nonclustered index idx_%2$s_e22 on %1$s ( e22 asc);", table, table.toLowerCase());
		execute(query);
	}

	public static void createNameIndexesForTestTable(String table) throws SQLException {
		System.out.println("TestTreeTableManager.createNameIndexesForTestTable:" + table);
		String query = String.format(
				"	create nonclustered index idx_%2$s_parent on %1$s ( parent asc);",
				table, table.toLowerCase());
		execute(query);
	}

	public static void dropAllIndexes(String table) throws SQLException {
		System.out.println("TestTreeTableManager.dropBoundIndexes:" + table);
		String query = String.format(//
				"	drop index if exists idx_%2$s_lower on dbo.%1$s;\r\n" + //
						"	drop index if exists idx_%2$s_upper on dbo.%1$s;\r\n" + //
						"	drop index if exists idx_%2$s_left on dbo.%1$s;\r\n" + //
						"	drop index if exists idx_%2$s_right on dbo.%1$s;\r\n" + //
						"	drop index if exists idx_%2$s_e11 on dbo.%1$s;\r\n" + //
						"	drop index if exists idx_%2$s_e12 on dbo.%1$s;\r\n" + //
						"	drop index if exists idx_%2$s_e21 on dbo.%1$s;\r\n" + //
						"	drop index if exists idx_%2$s_e22 on dbo.%1$s;",
				table, table.toLowerCase());
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			s.execute(query);
		}
	}

	public static void execute(String query) throws SQLException {
		System.out.println(query);
		try (Connection connection = DriverManager.getConnection(url); Statement s = connection.createStatement();) {
			Stopwatch stopwatch = Stopwatch.createStarted();
			s.execute(query);//
			System.out.println("Query took: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
		}
	}

	/*
	 * create nonclustered index idx_hazeltree_name on HazelTree ( name asc); create
	 * nonclustered index idx_hazeltree_parent on HazelTree ( parent asc);
	 */
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
		QueryResult result = queryNames(query);
		result.method = "selectCteRecursive";
		return result;
	}

	public static QueryResult selectMatrices(String table, String name) throws SQLException {
		System.out.println("TestTreeTableManager.selectMatrices:" + table + " for:" + name);
		String query = String.format("	select child.*\r\n" + "	from %1$s child, %1$s node   \r\n"
				+ "	where node.e11*child.e21 <= child.e11*node.e21\r\n"
				+ "	and child.e12*node.e22 <= node.e12*child.e22 \r\n"
				+ "	and node.name = '%2$s'  -- predicate uniquely identifying a node ", table, name);
		System.out.println(query);
		QueryResult result = queryNames(query);
		result.method = "selectMatrices";
		return result;
	}

	public static QueryResult selectMatricesWithIndexedColumnApproximate(String table, String name)
			throws SQLException {
		System.out.println("TestTreeTableManager.selectMatricesWithIndexedColumnApproximate(String, String):" + table
				+ " for:" + name);
		String query = String.format("	select child.*\r\n" //
				+ "	from %1$s child, %1$s node \r\n" //
				+ "	-- approximate for indexing\r\n"//
				+ "	where child.lower\r\n" //
				+ "	between node.lower and node.upper\r\n"//
				+ "	-- predicate uniquely identifying a node \r\n" //
				+ "	and node.name = '%2$s';", table, name);
		System.out.println(query);
		QueryResult result = queryNames(query);
		result.method = "selectMatricesWithIndexedColumnApproximate";
		return result;
	}

	public static QueryResult selectMatricesWithIndexedColumn(String table, String name) throws SQLException {
		System.out.println(
				"TestTreeTableManager.selectMatricesWithIndexedColumn(String, String):" + table + " for:" + name);
		String query = String.format("	select child.*\r\n" //
				+ "	from %1$s child, %1$s node \r\n" //
				+ "	-- approximate for indexing\r\n"//
				+ "	where child.lower\r\n" //
				+ "	between node.lower and node.upper\r\n"//
				+ "	-- exact\r\n" //
				+ "	and node.e11*child.e21 <= child.e11*node.e21 \r\n"//
				+ "	and child.e12*node.e22 <= node.e12*child.e22 \r\n"//
				+ "	-- predicate uniquely identifying a node \r\n" //
				+ "	and node.name = '%2$s';", table, name);
		System.out.println(query);
		QueryResult result = queryNames(query);
		result.method = "selectMatricesWithIndexedColumn";
		return result;
	}

	public static QueryResult queryNames(String query) {
		Stopwatch stopwatch = Stopwatch.createStarted();

		QueryResult qr = new QueryResult();
		try (Connection connection = DriverManager.getConnection(url);
				Statement s = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet result = s.executeQuery(query);) {
			System.out.println(result);
			for (result.first(); !result.isAfterLast(); result.next()) {
				qr.names.add(result.getObject("name").toString());
			}
		} catch (SQLException e) {
			qr.errorMessage = e.getLocalizedMessage();
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
		Precondition.checkArgument(max <= 8000000, "n must be less than 8000000");

		// replace user and pass
		try (Connection connection = DriverManager.getConnection(url);) {
			int currentRowQuantity = getRowCount(connection, table);

			Set<MatrixTreeNode<NamedInteger>> set;

			NamedIntegerTreeMapping mapping = new NamedIntegerTreeMapping(table);

			// first batch from 3 -> delta
			final int delta = 250000;
			int firstStep = Math.min(min + delta, max);
			set = PrimeTreeBuilder.buildPrimeBinaryTreesAsSet(min, firstStep);
			bulkInsert(connection, table, set.iterator(), mapping);

			// next batches
			for (int i = firstStep; i < max; i += delta) {
				if (max >= i + delta)
					set = PrimeTreeBuilder.buildPrimeBinaryTreesAsSet(i, i + delta);
				else
					set = PrimeTreeBuilder.buildPrimeBinaryTreesAsSet(i, max);
				bulkInsert(connection, table, set.iterator(), mapping);

				// hint for gc to start cleaning
				System.gc();
			}

			int updatedRowQuantity = getRowCount(connection, table);
			int change = updatedRowQuantity - currentRowQuantity;
			System.out.println("TestTreeTableManager.generateAndInsertBinaryTrees() added " + change
					+ " rows with total: " + updatedRowQuantity);
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
			System.out.println("TestTreeTableManager.bulkInsert() error:" + e.getLocalizedMessage());
			throw new RuntimeException(e);
		} finally {
			System.out.println("TestTreeTableManager.bulkInsert() took "
					+ sqlInsertStopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
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
			System.out.println("TestTreeTableManager.getRowCount: " + r.getInt("total"));
			return r.getInt("total");
		}
	}

	public static int getRowCount(String table) throws SQLException {
		System.out.println("TestTreeTableManager.getRowCount(" + table + ")");
		try (Connection connection = DriverManager.getConnection(url);) {
			return getRowCount(connection, table);
		}
	}

	// select max(upper) as trees from HazelTreeView;
	public static int getTreeCount(String tableView) throws SQLException {
		System.out.println("TestTreeTableManager.getTrees(" + tableView + ")");
		try (Connection connection = DriverManager.getConnection(url);
				Statement s = connection.createStatement();
				ResultSet r = s.executeQuery("select max(upper) as totalTrees from " + tableView);) {
			r.next();
			System.out.println("TestTreeTableManager.getTrees: " + r.getInt("totalTrees"));
			return r.getInt("totalTrees");
		}
	}
}
