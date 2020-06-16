package matrixtree.computation;

import java.util.Collection;
import java.util.function.Supplier;

import matrixtree.matrices.StandardMatrix;

/**
 * @author Agustinus Lawandy
 */
public interface MatrixOp {

	StandardMatrix multiply(StandardMatrix matA, StandardMatrix matB);

	StandardMatrix multiply(Collection<StandardMatrix> matrices);

	StandardMatrix transpose(StandardMatrix m);
}
