package matrixtree.computation;

import matrixtree.matrices.BaseMatrix;
import matrixtree.matrices.StandardMatrix;
import org.ejml.simple.SimpleMatrix;

import java.util.Collection;

/**
 * Matrix operations based on EJML Library.
 *
 * @author Agustinus Lawandy
 */
public class EjmlMatrixOp implements MatrixOp {

    @Override
    public StandardMatrix multiply(Collection<StandardMatrix> matrices) {
        if (matrices.isEmpty())
            throw new NullPointerException("Matrices should not be empty!");

        SimpleMatrix res = null;
        for (StandardMatrix mat : matrices) {
            SimpleMatrix smat = new SimpleMatrix(mat.asDouble());
            if (res == null)
                res = smat;
            else
                res = res.mult(smat);
        }

        return convert(res);
    }

    @Override
    public StandardMatrix transpose(StandardMatrix m) {
        SimpleMatrix s = new SimpleMatrix(m.asDouble());

        return convert(s.transpose());
    }

    @Override
    public StandardMatrix multiply(StandardMatrix matA, StandardMatrix matB) {
        SimpleMatrix a = new SimpleMatrix(matA.asDouble());
        SimpleMatrix b = new SimpleMatrix(matB.asDouble());

        return convert(a.mult(b));
    }

    private StandardMatrix convert(SimpleMatrix mat) {
        return new BaseMatrix(//
                mat.get(0, 0), mat.get(0, 1), //
                mat.get(1, 0), mat.get(1, 1));
    }
}
