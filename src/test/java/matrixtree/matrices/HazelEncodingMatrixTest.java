package matrixtree.matrices;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import matrixtree.model.HazelTreePath;
import matrixtree.model.Rational;
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
		EncodingMatrix p1 = path.getParentPath().getEncodingMatrix();
		EncodingMatrix p2 = path.getParentPath().getParentPath().getEncodingMatrix();
		System.out.println(m);
		System.out.println(p1);
		System.out.println(p2);
	}

	@Test
	void ancestor() {
		TreePath path = new HazelTreePath(2L, 4L, 3L);
		EncodingMatrix m = path.getEncodingMatrix();

		Rational node = m.getNodeEncoding();

		long n = node.getNumerator();
		long d = node.getDenominator();

		/**
		 * 0 1 <br>
		 * 1 0 <br>
		 */
		long n_p = 0;
		long d_p = 1;
		long n_sp = 1;
		long d_sp = 0;

		Rational p = new Rational(0,1);
		Rational ps = new Rational(1,0);
		
		List<Rational> ls = new ArrayList<>();
		while (n > 0 && d > 0) {
			System.out.println("n:" + n + " d:" + d);
			long div = n / d;
			long mod = n % d;

			n_p = n_p + div * n_sp;
			d_p = d_p + div * d_sp;
			
			n_sp = n_p + n_sp;
			d_sp = d_p + d_sp;

			ls.add(new Rational(n_p, d_p));

			// compact
			p = p.mediant(ps.multiply(div)); 

			n = mod;
			if (n != 0) {
				d = d % mod;
				if (d == 0) {
					System.out.println("Warning! Denom 0, set to 1!");
					d = 1;
				}
			}
		}
		
		ls.forEach(System.out::println);

	}

}
