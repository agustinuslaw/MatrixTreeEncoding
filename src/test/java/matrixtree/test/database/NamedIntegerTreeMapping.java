package matrixtree.test.database;

import java.math.BigInteger;
import java.util.function.Function;

import de.bytefish.jsqlserverbulkinsert.mapping.AbstractMapping;
import matrixtree.structure.MatrixTreeNode;
import matrixtree.test.model.NamedInteger;

/**
 * Mapping to enable rapid insertion for SQL Server Bulk Copy feature.
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-24
 */
public class NamedIntegerTreeMapping extends AbstractMapping<MatrixTreeNode<NamedInteger>> {
	/*
	 * name nvarchar(64) primary key,-- name containing the path
	 * 
	 * parent nvarchar(64), -- for traditional recursive search comparison
	 * 
	 * number int not null, -- node element
	 * 
	 * e11 bigint not null,
	 * 
	 * e21 bigint not null,
	 * 
	 * e12 bigint not null,
	 * 
	 * e22 bigint not null,
	 */
	public NamedIntegerTreeMapping(String table) {
		super(/* schema */ "dbo", table);

		mapNvarchar("name", n -> n.getElement().getName());
		mapNvarchar("parent", n -> n.getParent() == null ? null : n.getParent().getElement().getName());
		mapInt("number", n -> n.getElement().intValue());

		mapBigInt2("e11", n -> n.getPathMatrix().getE11());
		mapBigInt2("e21", n -> n.getPathMatrix().getE21());
		mapBigInt2("e12", n -> n.getPathMatrix().getE12());
		mapBigInt2("e22", n -> n.getPathMatrix().getE22());

	}

	protected void mapBigInt2(String columnName, Function<MatrixTreeNode<NamedInteger>, Long> propGet) {
		mapBigInt(columnName, n -> BigInteger.valueOf(propGet.apply(n)));
	}

	/** Needed due to mapInteger was ambiguous */
	protected void mapInt(String columnName, Function<MatrixTreeNode<NamedInteger>, Integer> propertyGetter) {
		mapInteger(columnName, propertyGetter);
	}

}