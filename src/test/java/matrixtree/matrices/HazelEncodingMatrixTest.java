package matrixtree.matrices;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import matrixtree.model.HazelTreePath;
import matrixtree.model.TreePath;

/**
 * @author Agustinus Lawandy
 * @since
 */
class HazelEncodingMatrixTest {

	@Test
	void test() {
		TreePath path = new HazelTreePath(2L, 4L, 3L);
		EncodingMatrix m = path.getEncodingMatrix();
		
		System.out.println(m);
	}

}
