package hazeltree.computation;

import hazeltree.matrices.HazelMatrix;

public interface MatrixOp {
	
	HazelMatrix multiply(HazelMatrix matA, HazelMatrix matB);
	
	HazelMatrix multiply(HazelMatrix... matrices);
	
}
