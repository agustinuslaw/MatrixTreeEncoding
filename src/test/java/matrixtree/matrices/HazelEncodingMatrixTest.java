package matrixtree.matrices;

import matrixtree.computation.ExactMatrixOp;
import matrixtree.computation.MatrixOp;
import matrixtree.exception.BadArgumentException;
import matrixtree.model.HazelTreePath;
import matrixtree.model.Rational;
import matrixtree.model.TreePath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
class HazelEncodingMatrixTest {

    @Test
    void test() {
        TreePath path = new HazelTreePath(2L, 4L, 3L);
        PathMatrix m = path.getPathMatrix();
        PathMatrix p1 = path.getParentPath().getPathMatrix();
        PathMatrix p2 = path.getParentPath().getParentPath().getPathMatrix();
        System.out.println(m);
        System.out.println(p1);
        System.out.println(p2);
    }

    @Test
    void ancestor() {
        TreePath path = new HazelTreePath(2L, 3L, 3L);
        PathMatrix m = path.getPathMatrix();
        PathMatrix p = path.getParentPath().getPathMatrix();

        Rational node = m.getNodeEncoding();
        Deque<PathMatrix> ls = computePathEncoding(node);

        Assertions.assertEquals(m, ls.getLast());
        ls.pollLast();
        Assertions.assertEquals(p, ls.getLast());
    }

    @Test
    void sibling() {
        PathMatrix leftSibling = new HazelTreePath(2L, 4L, 1L).getPathMatrix();
        PathMatrix current = new HazelTreePath(2L, 4L, 2L).getPathMatrix();
        PathMatrix rightSibling = new HazelTreePath(2L, 4L, 3L).getPathMatrix();

        System.out.println("leftSibling" + leftSibling);
        System.out.println("current" + current);
        System.out.println("rightSibling" + rightSibling);

        MatrixOp op = new ExactMatrixOp();
        StandardMatrix matrix = op.multiply(current, new HazelNodeMatrix(3).invert());
        matrix = op.multiply(matrix, new HazelNodeMatrix(2));

        System.out.println("relocated" + matrix);
    }


    @Nonnull
    public LinkedList<PathMatrix> computePathEncoding(@Nonnull Rational node) {
        return computePathEncoding(node, LinkedList::new);
    }

    @Nonnull
    public PathMatrix computeRootEncoding(@Nonnull Rational node) {
        /*
         * _n numerator and _d denominator.
         * p parent node (e.g. 2.4.3) encoding.
         * sp parent sibling node (e.g. 2.4.4) encoding.
         */

        // Origin matrix {{0,1}{1,0}} [0, Inf)
        long p_n = 0;
        long p_d = 1;
        long sp_n = 1;
        long sp_d = 0;

        if (node.getNumerator() > 0) {
            long div = node.longValue();

            p_n = p_n + div;
            /* p_d = p_d + div * sp_d // empty, kept for symmetry */
            sp_n = p_n + sp_n;
            sp_d = p_d + sp_d;

            return new HazelPathMatrix(p_n, sp_n, p_d, sp_d);
        } else {
            // if numerator <= 0 this is already beyond Root, throw runtime exception
            throw new BadArgumentException("node.numerator", node.getNumerator(), "[1,Inf)");
        }
    }

    @Nonnull
    public <C extends Collection<PathMatrix>> C computePathEncoding(@Nonnull Rational node, @Nonnull Supplier<C> supplier) {
        /*
         * _n numerator and _d denominator.
         * p parent node (e.g. 2.4.3) encoding.
         * sp parent sibling node (e.g. 2.4.4) encoding.
         */

        long n = node.getNumerator();
        long d = node.getDenominator();

        // Origin matrix {{0,1}{1,0}} [0, Inf)
        long p_n = 0;
        long p_d = 1;
        long sp_n = 1;
        long sp_d = 0;

        C ls = supplier.get();
        while (n > 0 && d > 0) {
            // Do not simplify, this is for clarity
            long div = n / d;
            long mod = n % d;
            p_n = p_n + div * sp_n;
            p_d = p_d + div * sp_d;
            sp_n = p_n + sp_n;
            sp_d = p_d + sp_d;

            System.out.println(p_n + "/" + p_d);
            ls.add(new HazelPathMatrix(p_n, sp_n, p_d, sp_d));

            n = mod;
            if (n != 0) {
                d = d % mod;
                if (d == 0) {
                    d = 1;
                }
            }
        }

        return ls;
    }

}
