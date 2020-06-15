package hazeltree.matrices;

/**
 * Base matrix used for rational number calculation {{0,1},{1,0}}
 * 
 * @author agustinus lawandy
 *
 */
public class OriginMatrix extends BaseMatrix implements HazelMatrix, SimpleInvertible {

	public OriginMatrix() {
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
	public HazelMatrix invert() {
		return this;
	}

}
