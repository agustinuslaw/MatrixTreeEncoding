package hazeltree.matrices;

/**
 * Represent 2x2 matrix required for the encoding.
 * 
 * @author agustinus lawandy
 *
 */
public interface HazelMatrix {

	double[][] asDouble();

	long[][] asLong();

	long determinant();
}
