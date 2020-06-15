package hazeltree.computation;

import java.util.Collection;

import hazeltree.matrices.HazelMatrix;

public interface MatrixOp {
	
	HazelMatrix multiply(HazelMatrix matA, HazelMatrix matB);
	
	HazelMatrix multiply(Collection<HazelMatrix> matrices);
}
