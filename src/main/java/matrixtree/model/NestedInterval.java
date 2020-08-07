package matrixtree.model;

import java.util.Objects;

import matrixtree.matrices.PathMatrix;

public class NestedInterval {
    Rational lower;
    Rational upper;

    public NestedInterval(PathMatrix p) {
        this.lower = new Rational(p.getNumerator(), p.getDenominator());
        this.upper = new Rational(p.getSiblingNumerator(), p.getSiblingDenominator());
    }

    public NestedInterval(Rational lower, Rational upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Rational getLower() {
        return lower;
    }

    public Rational getUpper() {
        return upper;
    }

    public boolean contains(NestedInterval child) {
        return lower.lessOrEqualTo(child.lower) && upper.greaterOrEqualTo(child.upper);
    }

    public boolean mutuallyExclusiveWith(NestedInterval interval)
    {
        return lower.greaterOrEqualTo(interval.upper) || upper.lessOrEqualTo(interval.lower);
    }

    public boolean subsetOf(NestedInterval parent) {
        return parent.lower.lessThan(this.lower) && parent.upper.greaterThan(this.upper);
    }

    public boolean supersetOf(NestedInterval child) {
        return lower.lessThan(child.lower) && upper.greaterThan(child.upper);
    }

    public String toApproximateString() {
        return "NestedInterval[" + lower.doubleValue() + ", " + upper.doubleValue() + ']';
    }

    @Override
    public String toString() {
        return "NestedInterval[" + lower + ", " + upper + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NestedInterval that = (NestedInterval) o;
        return Objects.equals(lower, that.lower) &&
                Objects.equals(upper, that.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }
}
