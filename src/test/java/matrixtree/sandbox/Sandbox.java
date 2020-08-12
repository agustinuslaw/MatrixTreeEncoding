package matrixtree.sandbox;

import matrixtree.matrices.PathMatrix;
import matrixtree.model.HazelTreePath;
import matrixtree.model.NestedInterval;

/**
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
class Sandbox {


//    @Test
    void child() {
        PathMatrix current = new HazelTreePath(2L, 1L).computePathMatrix();
        PathMatrix child4 = new HazelTreePath(2L, 1L, 1L).computePathMatrix();

        summarize(current);
        summarize(child4);
    }

	/**
	 * @param current
	 */
	private void summarize(PathMatrix current) {
		
		NestedInterval currentNi = current.asNestedInterval();
		
	}
    
    

}
