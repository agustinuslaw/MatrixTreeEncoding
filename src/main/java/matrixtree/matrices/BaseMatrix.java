package matrixtree.matrices;

/**
 * Standard structure for 2x2 matrix M.
 * <p>
 * e11(a) e12(b) <br>
 * e21(c) e22(d) <br>
 * <p>
 * {{e11,e12},{e21,e22}}
 *
 * @author Agustinus Lawandy
 */
public class BaseMatrix implements StandardMatrix {

    /**
     * a11 : Numerator
     */
    private final long e11;
    /**
     * a12 : Sibling Numerator
     */
    private final long e12;
    /**
     * a21 : Denominator
     */
    private final long e21;
    /**
     * a22 : Sibling Denominator
     */
    private final long e22;

    public long getE11() {
        return e11;
    }

    public long getE12() {
        return e12;
    }

    public long getE21() {
        return e21;
    }

    public long getE22() {
        return e22;
    }

    public BaseMatrix(long a11, long a12, long a21, long a22) {
        super();
        this.e11 = a11;
        this.e12 = a12;
        this.e21 = a21;
        this.e22 = a22;
    }

    public BaseMatrix(StandardMatrix other) {
        super();
        this.e11 = other.getE11();
        this.e12 = other.getE12();
        this.e21 = other.getE21();
        this.e22 = other.getE22();
    }

    public BaseMatrix(double a11, double a12, double a21, double a22) {
        super();
        this.e11 = (long) a11;
        this.e12 = (long) a12;
        this.e21 = (long) a21;
        this.e22 = (long) a22;
    }

    public double[][] asDouble() {
        return new double[][]{ //
                new double[]{e11, e12}, //
                new double[]{e21, e22}//
        };
    }

    public long[][] asLong() {
        return new long[][]{ //
                new long[]{e11, e12}, //
                new long[]{e21, e22}//
        };
    }

    public long determinant() {
        return e11 * e22 - e12 * e21;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (e11 ^ (e11 >>> 32));
        result = prime * result + (int) (e12 ^ (e12 >>> 32));
        result = prime * result + (int) (e21 ^ (e21 >>> 32));
        result = prime * result + (int) (e22 ^ (e22 >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseMatrix other = (BaseMatrix) obj;
        if (e11 != other.e11)
            return false;
        if (e12 != other.e12)
            return false;
        if (e21 != other.e21)
            return false;
        return e22 == other.e22;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{{" + e11 + "," + e12 + "},{" + e21 + "," + e22 + "}}";
    }

}
