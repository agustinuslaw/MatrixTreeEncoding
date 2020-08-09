package matrixtree.matrices;

/**
 * Hazel scheme specific Origin matrix O used for rational number calculation {{0,1},{1,0}}
 * Notation is O, this matrix is unique.
 *
 * @author Agustinus Lawandy
 *
 */
public class HazelOriginMatrix extends BaseMatrix implements StandardMatrix, SimpleInvertible {

	private static final long serialVersionUID = 6277392036974354531L;

	public HazelOriginMatrix() {
		super(0, 1, 1, 0);
	}

	/**
	 * Det {{0,1},{1,0}} = 0-1 = -1
	 * 
	 * @return -1
	 */
	@Override
	public long determinant() {
		return -1;
	}

	/**
	 * M = {{0,1},{1,0}} <br>
	 * inv(M) = M
	 * 
	 * @return itself
	 */
	@Override
	public StandardMatrix invert() {
		return this;
	}

}
