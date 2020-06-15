package matrixtree.computation;

import java.util.Collection;

import matrixtree.matrices.BaseMatrix;
import matrixtree.matrices.TreeMatrix;

public class ExactMatrixOp implements MatrixOp {

	@Override
	public TreeMatrix multiply(TreeMatrix matA, TreeMatrix matB) {
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
	public TreeMatrix multiply(Collection<TreeMatrix> matrices) {
		TreeMatrix res = null;
		for (TreeMatrix mat : matrices) 
			if (res == null)
				res = mat;
			else
				res = multiply(res, mat);

		if (res == null)
			throw new NullPointerException("Matrices should not be empty!");

		return res;
	}

}
