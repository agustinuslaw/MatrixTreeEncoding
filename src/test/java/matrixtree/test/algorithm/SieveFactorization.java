package matrixtree.test.algorithm;

import java.util.ArrayList;
import java.util.List;

public class SieveFactorization {

	final int maxn;

	/** stores smallest prime factor for every number */
	int smallestPrimeFactor[];

	/**
	 * @param maxn maximum number to factorize
	 */
	public SieveFactorization(int n) {
		super();
		maxn = n + 1;
		smallestPrimeFactor = new int[maxn];

		// construct the sieve
		sieve();
	}

	/**
	 * Calculating SPF (Smallest Prime Factor) for every number till MAXN. Time
	 * Complexity : O(nloglogn)
	 */
	private void sieve() {
		smallestPrimeFactor[1] = 1;
		/* marking smallest prime factor for every number to be itself. */
		for (int i = 2; i < maxn; i++)
			smallestPrimeFactor[i] = i;

		/* separately marking spf for every even number as 2 */
		for (int i = 4; i < maxn; i += 2)
			smallestPrimeFactor[i] = 2;

		/* checking if i is prime */
		for (int i = 3; i * i < maxn; i++) {
			if (smallestPrimeFactor[i] == i) {
				/* marking SPF for all numbers divisible by i */
				for (int j = i * i; j < maxn; j += i)

					/* marking spf[j] if it is not previously marked */
					if (smallestPrimeFactor[j] == j)
						smallestPrimeFactor[j] = i;
			}
		}
	}

	/**
	 * A O(log n) function returning prime factorization by dividing by smallest
	 * prime factor at every step
	 * 
	 * @param x the number
	 * @return prime factors
	 */
	public List<Integer> factorize(int x) {
		List<Integer> ret = new ArrayList<>();
		while (x != 1) {
			ret.add(smallestPrimeFactor[x]);
			x = x / smallestPrimeFactor[x];
		}
		return ret;
	}
}