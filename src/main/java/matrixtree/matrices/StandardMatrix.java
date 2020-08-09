package matrixtree.matrices;

import java.io.Serializable;

/**
 * Standard matrix M that represent all 2x2 matrix in the application.
 *
 * @author Agustinus Lawandy
 */
public interface StandardMatrix extends Serializable {

    /**
     * {{e11,e12},{e21,e22}} <br>
     * {{a,b},{c,d}}
     * @return e11 or a position.
     */
    long getE11();

    /**
     * {{e11,e12},{e21,e22}} <br>
     * {{a,b},{c,d}}
     * @return e12 or b position.
     */
    long getE12();

    /**
     * {{e11,e12},{e21,e22}} <br>
     * {{a,b},{c,d}}
     * @return e21 or c position.
     */
    long getE21();

    /**
     * {{e11,e12},{e21,e22}} <br>
     * {{a,b},{c,d}}
     * @return e22 or d position.
     */
    long getE22();

    /**
     * @return matrix as 2D array of doubles
     */
    double[][] asDouble();

    /**
     * @return matrix as 2D array of longs
     */
    long[][] asLong();

    /**
     * Determinant of matrix is e11 * e22 - e12 * e21. <br>
     * Special matrices can also have a constant determinant, such as always -1.
     * @return determinant of matrix.
     */
    long determinant();
    
    
    /**
     * Multiplies this matrix (a) by other matrix (b). <br>
     * The result matrix c = a * b.
     * 
     * @param b matrix to multiply with
     * @return a * b
     */
    StandardMatrix multiply(StandardMatrix b);

}
