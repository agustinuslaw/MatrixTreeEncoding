package matrixtree.matrices;

import matrixtree.exception.BadDomainException;

/**
 * Represent moving the node horizontally (across siblings) within the same depth.<br>
 * Note that Relocation Matrix R <br>
 * P = O.N[c_1].N[c_2]...N[c_n] <br>
 * P' = O.N[c_1].N[c_2]...N[c_n] N[c_n]^-1 N[c_m]<br>
 * Relocation matrix R = N[c_n]^-1 N[c_m] <br>
 * Matrix form is {{1-(m-n),-(m-n)},{m-n,1+m-n}} where n is node current position and m is the desired position.
 * Differently stated, {{1-k,-k},{k,1+k}} where k is the distance from current position.
 *
 * @author Agustinus Lawandy
 */
public class HazelSiblingRelocationMatrix extends BaseMatrix implements StandardMatrix, SimpleInvertible {

    private static final long serialVersionUID = -317374135292198078L;

	/**
     * Horizontally relocate the EncodingMatrix from n-th child to m-th child.
     * {{1-(m-n),-(m-n)},{m-n,1+m-n}}
     *
     * @param n the current position of node.
     * @param m the desired position of node.
     */
    public HazelSiblingRelocationMatrix(long n, long m) throws BadDomainException {
        super(1 - (m - n), -(m - n), m - n, 1 + (m - n));
    }

    /**
     * Horizontally relocate the EncodingMatrix with k-steps in the same tree depth.
     * {{1-k,-k},{k,1+k}}
     *
     * @param k steps of relocation distance, may be negative.
     */
    public HazelSiblingRelocationMatrix(long k) throws BadDomainException {
        super(1 - k, -k, k, 1 + k);
    }

    /**
     * For matrix {{1-k,-k},{k,1+k}}<br>
     * Det = 1^2 - k^2 + k^2 = 1
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
