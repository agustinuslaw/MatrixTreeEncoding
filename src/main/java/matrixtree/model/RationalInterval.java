package matrixtree.model;

import java.text.NumberFormat;
import java.util.Objects;

import matrixtree.matrices.PathMatrix;

/**
 * Represent a rational interval
 * 
 * @author Agustinus Lawandy
 *
 */
public class RationalInterval {
	private Rational lower;
	private Rational upper;
	
	private static final NumberFormat NF = NumberFormat.getInstance();

	public RationalInterval(PathMatrix p) {
		this.lower = new Rational(p.getNumerator(), p.getDenominator());
		this.upper = new Rational(p.getSiblingNumerator(), p.getSiblingDenominator());
	}

	public RationalInterval(Rational lower, Rational upper) {
		this.lower = lower;
		this.upper = upper;
	}

	public Rational getLower() {
		return lower;
	}

	public Rational getUpper() {
		return upper;
	}

	public boolean contains(RationalInterval child) {
		return lower.lessOrEqualTo(child.lower) && upper.greaterOrEqualTo(child.upper);
	}

	public boolean mutuallyExclusiveWith(RationalInterval interval) {
		return lower.greaterOrEqualTo(interval.upper) || upper.lessOrEqualTo(interval.lower);
	}

	public boolean subsetOf(RationalInterval parent) {
		return parent.lower.lessThan(this.lower) && parent.upper.greaterThan(this.upper);
	}

	public boolean supersetOf(RationalInterval child) {
		return lower.lessThan(child.lower) && upper.greaterThan(child.upper);
	}

	@Override
	public String toString() {
		return "RationalInterval[" + lower + ", " + upper + ']';
	}

	/**
	 * Convenient method to represent this class as an approximate double string 
	 * @param precision to represent the string
	 * @return the string representation
	 */
	public String toDoubleStr(int precision) {
    	// [ a , b ) is the set a <= x < y
		NF.setGroupingUsed(false);
    	NF.setMaximumFractionDigits(precision);
        return "[" + NF.format(lower.doubleValue()) + "," + NF.format(upper.doubleValue()) + ")";
    }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RationalInterval that = (RationalInterval) o;
		return Objects.equals(lower, that.lower) && Objects.equals(upper, that.upper);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lower, upper);
	}
}
