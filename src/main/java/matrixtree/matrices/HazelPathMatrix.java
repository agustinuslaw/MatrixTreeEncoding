package matrixtree.matrices;

import matrixtree.computation.ExactMatrixOp;
import matrixtree.computation.MatrixOp;
import matrixtree.exception.BadArgumentException;
import matrixtree.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Hazel scheme Path matrix P that represent a single path in the tree. Note for
 * Hazel scheme the true root is the Origin matrix O defined in
 * {@link HazelOriginMatrix}. <br>
 * P[1.1.2] = O.N[1].N[1].N[2]
 *
 * @author Agustinus Lawandy
 */
public class HazelPathMatrix extends BaseMatrix implements PathMatrix, StandardMatrix, SimpleInvertible {

	private static final long serialVersionUID = 4792910434082808474L;

	/**
	 * Initialize {@link HazelPathMatrix} with lower and upper bounds.
	 *
	 * @param i nested interval providing lower and upper bound
	 */
	public HazelPathMatrix(NestedInterval i) {
		// lower bound is node, upper bound is sibling
		super(//
				i.getLower().getNumerator(), i.getUpper().getNumerator(), //
				i.getLower().getDenominator(), i.getUpper().getDenominator());
	}

	public HazelPathMatrix(double numerator, double siblingNumerator, double denominator, double siblingDenominator) {
		// a11, a12, a21, a22
		super((long) numerator, (long) siblingNumerator, (long) denominator, (long) siblingDenominator);
	}

	/**
	 * Initialize {@link HazelPathMatrix} with individual element.
	 *
	 * @param numerator          (e11) node numerator
	 * @param siblingNumerator   (e12) node sibling numerator
	 * @param denominator        (e21) node denominator
	 * @param siblingDenominator (e22) node sibling denominator
	 */
	public HazelPathMatrix(long numerator, long siblingNumerator, long denominator, long siblingDenominator) {
		// a11, a12, a21, a22
		super(numerator, siblingNumerator, denominator, siblingDenominator);
	}

	public HazelPathMatrix(HazelPathMatrix parent, long index) {
		super(parent.multiply(new HazelNodeMatrix(index)));
	}

	public HazelPathMatrix(StandardMatrix other) {
		super(other);
	}

	public long getNumerator() {
		return getE11();
	}

	public long getDenominator() {
		return getE21();
	}

	public long getSiblingNumerator() {
		return getE12();
	}

	public long getSiblingDenominator() {
		return getE22();
	}

	@Override
	public NestedInterval asNestedInterval() {
		return new NestedInterval(this);
	}

	/**
	 * Determinant is always -1 because <br>
	 * Encoding Matrix = Origin Matrix * Node Matrices... <br>
	 * det = -1 * ( 1 * 1 * ...) = -1
	 */
	@Override
	public long determinant() {
		return -1;
	}

	@Override
	public StandardMatrix invert() {
		// remember det = -1
		// inv = -d(a22) b(a12) c(a21) -a(a11)
		return new BaseMatrix(-getE22(), getE12(), getE21(), -getE11());
	}

	@Override
	public HazelPathMatrix computeRootMatrix() {
		/*
		 * _n numerator and _d denominator. p parent node (e.g. 2.4.3) encoding. sp
		 * parent sibling node (e.g. 2.4.4) encoding.
		 */

		long numerator = getNumerator() / getDenominator();
		long denominator = 1;

		long siblingNumerator = numerator + 1;
		long siblingDenominator = 1;

		return new HazelPathMatrix(numerator, siblingNumerator, denominator, siblingDenominator);
	}

	@Override
	public int computeDepth() {
		return computeAncestors().getAncestorMatrices().size();
	}

	@Override
	public HazelAncestors computeAncestors() {
		long numerator = getNumerator();
		long denominator = getDenominator();

		// Origin matrix {{0,1}{1,0}} [0, Inf)
		long parentNum = 0;
		long parentDen = 1;
		long parentSiblingNum = 1;
		long parentSiblingDen = 0;

		List<HazelPathMatrix> ancestorMatrices = new ArrayList<>();
		List<Long> path = new ArrayList<>();
		while (numerator > 0 && denominator > 0) {
			// Do not simplify, this is for clarity
			long div = numerator / denominator;
			long mod = numerator % denominator;

			parentNum = parentNum + div * parentSiblingNum;
			parentDen = parentDen + div * parentSiblingDen;
			parentSiblingNum = parentNum + parentSiblingNum;
			parentSiblingDen = parentDen + parentSiblingDen;

			path.add(div);
			ancestorMatrices.add(new HazelPathMatrix(parentNum, parentSiblingNum, parentDen, parentSiblingDen));

			numerator = mod;
			if (numerator != 0) {
				denominator = denominator % mod;
				if (denominator == 0) {
					denominator = 1;
				}
			}
		}

		return new HazelAncestors(new HazelTreePath(path), ancestorMatrices);
	}

	@Override
	public HazelPathMatrix computeParentMatrix() {
		if (getDenominator() <= 1) {
			// When denominator == 1 : Reached top level already, division by 0 my occur
			throw new BadArgumentException("denominator", getDenominator(), "[2,Inf)");
		}

		// e12 is parent sibling numerator
		long e12 = getSiblingNumerator() - getNumerator();
		// e22 is parent sibling denominator
		long e22 = getSiblingDenominator() - getDenominator();
		// e11 is parent numerator
		long e11 = getNumerator() % e12;
		// e21 is parent denominator
		long e21 = getDenominator() % e22;

		// parent path matrix
		return new HazelPathMatrix(e11, e12, e21, e22);
	}

	@Override
	public PathMatrix computeSibling(long k) {
		MatrixOp op = new ExactMatrixOp();
		return new HazelPathMatrix(op.multiply(this, new HazelSiblingRelocationMatrix(k)));
	}

	@Override
	public PathMatrix computeChild(long k) {
		MatrixOp op = new ExactMatrixOp();
		return new HazelPathMatrix(op.multiply(this, new HazelNodeMatrix(k)));
	}

	@Override
	public long computeIndex() {
		PathMatrix parent = this.computeParentMatrix();
		// P N = C -> N = inv(P) C
		StandardMatrix node = parent.invert().multiply(this);
		HazelNodeMatrix nodeMat = new HazelNodeMatrix(node);
		return nodeMat.getIndex();
	}

	@Override
	public boolean isRoot() {
		return getDenominator() == 1L && getSiblingDenominator() == 1L;
	}

	@Override
	public int compareTo(PathMatrix o) {
		// ratio comparison
		
		Rational ref = new Rational(getNumerator(), getDenominator());
		Rational other = new Rational(o.getNumerator(), o.getDenominator());
		return ref.compareTo(other);
	}

}
