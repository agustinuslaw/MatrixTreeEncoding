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

	private static final long serialVersionUID = -3197137690161248482L;
	private final long index;

	public HazelNodeMatrix(StandardMatrix mat) {
		super(mat.getE11(), mat.getE12(), mat.getE21(), mat.getE22());

		if (mat.getE11() != 1L || mat.getE12() != 1L)
			throw new IllegalArgumentException("Not Hazel Node! Element e11 and e12 must be 1! " + this);
		
		if (mat.getE22() != mat.getE21() + 1)
			throw new IllegalArgumentException("Not Hazel Node! Element e22 should equal e21 + 1: " + this);
		
		if (mat.getE22() <= 0)
			throw new BadArgumentException("e21", mat.getE21(), "[1,inf)");
		
		this.index = mat.getE21();
	}
	
	public HazelNodeMatrix(long index) {
		super(1, 1, index, index + 1);
		
		// Node has to be in [1, inf)
		if (index <= 0)
			throw new BadArgumentException("node", index, "[1,inf)");
		
		this.index = index;
	}
	
	/**
	 * @return the node
	 */
	public long getIndex() {
		return index;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (index ^ (index >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HazelNodeMatrix other = (HazelNodeMatrix) obj;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HazelNodeMatrix [index=" + index + "]";
	}
	
}
