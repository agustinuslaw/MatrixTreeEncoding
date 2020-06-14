package hazeltree.matrices;

import hazeltree.exception.BadArgumentException;

/**
 * Represent a single node in the Hazel Encoding. Matrix form {{1,1},{c,c+1}}
 * where c is node.
 * 
 * @author agustinus lawandy
 *
 */
public class NodeMatrix extends BaseMatrix implements HazelMatrix, SimpleInvertible {
	private final long node;

	public NodeMatrix(long node) throws BadArgumentException {
		super(1,1,node,node+1);

		// Node has to be in [1, inf)
		if (node <= 0)
			throw new BadArgumentException("node", node, "[1,inf)");

		this.node = node;
	}

	/**
	 * For matrix {{1,1},{c,c+1}}<br>
	 * Det = 1 × (c + 1) − 1 × c = 1
	 * 
	 * @return 1
	 */
	@Override
	public long determinant() {
		return 1;
	}

	@Override
	public HazelMatrix invert() {
		// remember the determinant is 1
		// d(a22) -b(a12) -c(a21) a(a11)
		return (HazelMatrix) new BaseMatrix(e22, e12, node, node);
	}
}
