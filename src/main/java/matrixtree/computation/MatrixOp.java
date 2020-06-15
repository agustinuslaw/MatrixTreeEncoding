package matrixtree.computation;

import java.util.Collection;

import matrixtree.matrices.TreeMatrix;

public interface MatrixOp {
	
	TreeMatrix multiply(TreeMatrix matA, TreeMatrix matB);
	
	TreeMatrix multiply(Collection<TreeMatrix> matrices);
}
