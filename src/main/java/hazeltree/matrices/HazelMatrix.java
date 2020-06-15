package hazeltree.matrices;

/**
 * Represent 2x2 matrix required for the encoding.
 * 
 * @author agustinus lawandy
 *
 */
public interface HazelMatrix {

	public long getE11();

	public long getE12();

	public long getE21();

	public long getE22();

	double[][] asDouble();

	long[][] asLong();

	long determinant();
}
