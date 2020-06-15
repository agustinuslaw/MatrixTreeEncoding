package matrixtree.matrices;

import matrixtree.exception.BadArgumentException;

/**
 * TODO this relocation matrix is wrong: need to derive my own via below.
 * P = O.N[c_1].N[c_2]...N[c_n] <br>
 * P' = O.N[c_1].N[c_2]...N[c_n] N[c_n]^-1 N[c_m]<br>
 * Relocation matrix R = N[c_n]^-1 N[c_m]
 * <p>s
 * Represent moving the node horizontally within the same depth. <br>
 * Note that Encoding Matrix E
 * Matrix form is {{1,0},{m-n,1}} where n is node current position and m is the desired position.
 * Differently stated, {{1,0},{k,1}} where k is the distance from current position.
 *
 * @author Agustinus Lawandy
 */
public class HazelRelocationMatrix extends BaseMatrix implements StandardMatrix, SimpleInvertible {

    /**
     * Horizontally relocate the EncodingMatrix from n-th child to m-th child.
     * {{1,0},{m-n,1}}
     * @param n the current position of node.
     * @param m the desired position of node.
     */
    public HazelRelocationMatrix(long n, long m) throws BadArgumentException {
        super(1, 0, m - n, 1);
    }

    /**
     * Horizontally relocate the EncodingMatrix with k-steps in the same tree depth.
     * {{1,0},{k,1}}
     * @param k steps of relocation distance, may be negative.
     */
    public HazelRelocationMatrix(long k) throws BadArgumentException {
        super(1, 0, k, 1);
    }

    /**
     * For matrix {{1,0},{k,1}}<br>
     * Det = 1 x 1 - k x 0 = 1
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
