package matrixtree.computation;

import java.util.Collection;
import java.util.function.Supplier;

import matrixtree.matrices.BaseMatrix;
import matrixtree.matrices.StandardMatrix;

/**
 * Matrix operation based only on exact arithmetics.
 *
 * @author Agustinus Lawandy
 */
public class ExactMatrixOp implements MatrixOp {

	@Override
	public StandardMatrix multiply(StandardMatrix matA, StandardMatrix matB) {
		long[][] a = matA.asLong();
		long[][] b = matB.asLong();

		// Exact calculation using only integer arithmetics
		long e11 = a[0][0] * b[0][0] + a[0][1] * b[1][0];
		long e12 = a[0][0] * b[0][1] + a[0][1] * b[1][1];
		long e21 = a[1][0] * b[0][0] + a[1][1] * b[1][0];
		long e22 = a[1][0] * b[0][1] + a[1][1] * b[1][1];

		return new BaseMatrix(e11, e12, e21, e22);
	}

	@Override
	public StandardMatrix multiply(Collection<StandardMatrix> matrices) {
		if (matrices.isEmpty())
			throw new NullPointerException("Matrices should not be empty!");

		StandardMatrix res = null;
		for (StandardMatrix mat : matrices)
			if (res == null)
				res = mat;
			else
				res = multiply(res, mat);

		return res;
	}

	@Override
	public StandardMatrix transpose(StandardMatrix m) {
		return new BaseMatrix(m.getE11(), m.getE21(), m.getE12(), m.getE22());
	}


}
