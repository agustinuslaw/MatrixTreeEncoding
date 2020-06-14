package hazeltree.matrices;

/**
 * Hazel algorithm only requires 2x2 matrices.
 * <p>
 * a11(a) a12(b) <br>
 * a21(c) a22(d) <br>
 * 
 * @author agustinus lawandy
 *
 */
public class BaseMatrix implements HazelMatrix {

	/** a11 : Numerator */
	public final long e11;
	/** a12 : Sibling Numerator */
	public final long e12;
	/** a21 : Denominator */
	public final long e21;
	/** a22 : Sibling Denominator */
	public final long e22;

	/**
	 * Construct Matrix : {{a11,a12},{a21,a22}}
	 * 
	 * @param a11
	 * @param a12
	 * @param a21
	 * @param a22
	 */
	public BaseMatrix(long a11, long a12, long a21, long a22) {
		super();
		this.e11 = a11;
		this.e12 = a12;
		this.e21 = a21;
		this.e22 = a22;
	}

	public BaseMatrix(double a11, double a12, double a21, double a22) {
		super();
		this.e11 = (long) a11;
		this.e12 = (long) a12;
		this.e21 = (long) a21;
		this.e22 = (long) a22;
	}

	public double[][] asDouble() {
		return new double[][] { //
				new double[] { e11, e12 }, //
				new double[] { e21, e22 }//
		};
	}

	public long[][] asLong() {
		return new long[][] { //
				new long[] { e11, e12 }, //
				new long[] { e21, e22 }//
		};
	}

	public long determinant() {
		return e11 * e22 - e12 * e21;
	}


}
