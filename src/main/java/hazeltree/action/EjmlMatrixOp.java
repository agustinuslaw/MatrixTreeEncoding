package hazeltree.action;

import org.ejml.simple.SimpleMatrix;

import hazeltree.computation.MatrixOp;
import hazeltree.matrices.BaseMatrix;
import hazeltree.matrices.HazelMatrix;

public class EjmlMatrixOp implements MatrixOp {

	@Override
	public HazelMatrix multiply(HazelMatrix... matrices) {
		SimpleMatrix res = null;
		for (HazelMatrix mat : matrices) {
			SimpleMatrix smat = new SimpleMatrix(mat.asDouble());
			if (res == null)
				res = smat;
			else
				res = res.mult(smat);
		}

		if (matrices.length == 0 || res == null)
			throw new NullPointerException("Matrices should not be empty!");

		return convert(res);
	}

	@Override
	public HazelMatrix multiply(HazelMatrix matA, HazelMatrix matB) {
		SimpleMatrix a = new SimpleMatrix(matA.asDouble());
		SimpleMatrix b = new SimpleMatrix(matB.asDouble());
		
		return convert(a.mult(b));
	}

	private HazelMatrix convert(SimpleMatrix mat) {
		return new BaseMatrix(mat.get(0, 0), mat.get(0, 1), //
				mat.get(1, 0), mat.get(1, 1));
	}
}
