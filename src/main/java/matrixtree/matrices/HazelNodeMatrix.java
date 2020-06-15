package matrixtree.matrices;

import matrixtree.exception.BadArgumentException;

/**
 * Represent a single node in the Hazel encoding scheme. <br>
 * Matrix form is {{1,1},{c,c+1}} where c is child position.
 * Notation of this matrix is N[4] representing Node of the 4th child. e.g. {{1,1},{4,4+1}}
 *
 * @author Agustinus Lawandy
 *
 */
public class HazelNodeMatrix extends BaseMatrix implements StandardMatrix, SimpleInvertible {

	public HazelNodeMatrix(long node) throws BadArgumentException {
		super(1, 1, node, node + 1);

		// Node has to be in [1, inf)
		if (node <= 0)
			throw new BadArgumentException("node", node, "[1,inf)");
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
	public StandardMatrix invert() {
		// remember the determinant is 1
		// d(a22) -b(a12) -c(a21) a(a11)
		return new BaseMatrix(getE22(), -getE12(), -getE21(), getE11());
	}
}
