package matrixtree.computation;

import java.util.Collection;

import org.ejml.simple.SimpleMatrix;

import matrixtree.matrices.BaseMatrix;
import matrixtree.matrices.StandardMatrix;

/**
 * @author Agustinus Lawandy
 */
public class EjmlMatrixOp implements MatrixOp {

	@Override
	public StandardMatrix multiply(Collection<StandardMatrix>  matrices) {
		SimpleMatrix res = null;
		for (StandardMatrix mat : matrices) {
			SimpleMatrix smat = new SimpleMatrix(mat.asDouble());
			if (res == null)
				res = smat;
			else
				res = res.mult(smat);
		}

		if (res == null)
			throw new NullPointerException("Matrices should not be empty!");

		return convert(res);
	}

	@Override
	public StandardMatrix multiply(StandardMatrix matA, StandardMatrix matB) {
		SimpleMatrix a = new SimpleMatrix(matA.asDouble());
		SimpleMatrix b = new SimpleMatrix(matB.asDouble());
		
		return convert(a.mult(b));
	}

	private StandardMatrix convert(SimpleMatrix mat) {
		return new BaseMatrix(mat.get(0, 0), mat.get(0, 1), //
				mat.get(1, 0), mat.get(1, 1));
	}
}
