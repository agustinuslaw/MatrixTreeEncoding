package matrixtree.test.algorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import matrixtree.structure.ListHazelMatrixTreeNode;
import matrixtree.structure.MapHazelMatrixTreeNode;
import matrixtree.structure.MatrixTreeNode;
import matrixtree.structure.MutableMatrixTreeNode;
import matrixtree.test.model.NamedInteger;

/**
 * Create tree out of prime factors
 * 
 * @author Agustinus Lawandy
 * @since 2020-08-22
 */
public class PrimeTreeBuilder {

	public static Set<MatrixTreeNode<NamedInteger>> buildPrimeBinaryTreesAsSet(int from, int to) {
		System.out.println("PrimeTreeBuilder.buildPrimeBinaryTreesAsSet(" + from + "->" + to + ") started");
		Stopwatch treeGenStowatch = Stopwatch.createStarted();

		SieveFactorization sieve = new SieveFactorization(to);
		Set<MatrixTreeNode<NamedInteger>> set = new HashSet<>();
		for (int i = from; i < to; i++) {
			set.addAll(buildBinaryAsSet(i, sieve.factorize(i)));
		}

		System.out.println("PrimeTreeBuilder.buildPrimeBinaryTreesAsSet(" + from + "->" + to + ")" + " took "
				+ treeGenStowatch.elapsed(TimeUnit.MILLISECONDS) + " ms");

		return set;
	}

	/**
	 * Create binary prime tree.
	 * 
	 * @param number  to be factorized
	 * @param factors of primes
	 * @return binary tree
	 */
	public static Set<MatrixTreeNode<NamedInteger>> buildBinaryAsSet(int number, List<Integer> factors) {
		NamedInteger rootElement = new NamedInteger(number, number);

		Set<MatrixTreeNode<NamedInteger>> set = new HashSet<>();

		MutableMatrixTreeNode<NamedInteger> root = new MapHazelMatrixTreeNode<>(null, /* elem */ rootElement,
				/* index */ number);
		set.add(root);

		MutableMatrixTreeNode<NamedInteger> parentNode = root;
		int intermediate = number;
		String currentPrefix = rootElement.getName() + ".";
		for (int child : factors) {
			intermediate /= child;

			if (intermediate != 1) {
				NamedInteger leafElement = new NamedInteger(currentPrefix + child, child);
				NamedInteger intermediateElement = new NamedInteger(currentPrefix + intermediate, intermediate);

				if (leafElement.equals(intermediateElement)) {
					// revise the name for leaf child so the name is always unique
					leafElement = new NamedInteger(currentPrefix + child + "a", child);
				}

				MutableMatrixTreeNode<NamedInteger> leaf = parentNode.add(leafElement);
				MutableMatrixTreeNode<NamedInteger> intermediateParent = parentNode.add(intermediateElement);

				// add both child to set
				set.add(leaf);
				set.add(intermediateParent);

				// update reference so next child is added to intermediate parent
				parentNode = intermediateParent;

				// append current prefix for next
				currentPrefix = currentPrefix + intermediate + ".";
			}
		}

		return set;
	}

	/**
	 * Create binary prime tree.
	 * 
	 * @param number  to be factorized
	 * @param factors of primes
	 * @return binary tree
	 */
	public static MutableMatrixTreeNode<NamedInteger> buildBinary(int number, List<Integer> factors) {
		NamedInteger rootElement = new NamedInteger(number, number);
		MutableMatrixTreeNode<NamedInteger> root = new MapHazelMatrixTreeNode<>(null, /* elem */ rootElement,
				/* index */ number);

		MutableMatrixTreeNode<NamedInteger> parentNode = root;
		int intermediate = number;
		String currentPrefix = rootElement.getName() + ".";
		for (int child : factors) {
			intermediate /= child;

			if (intermediate != 1) {
				NamedInteger leafElement = new NamedInteger(currentPrefix + child, child);
				NamedInteger intermediateElement = new NamedInteger(currentPrefix + intermediate, intermediate);

				if (leafElement.equals(intermediateElement)) {
					// revise the name for leaf child so the name is always unique
					leafElement = new NamedInteger(currentPrefix + child + "a", child);
				}

				parentNode.add(leafElement);
				parentNode = parentNode.add(intermediateElement);

				// append current prefix for next
				currentPrefix = currentPrefix + intermediate + ".";
			}
		}

		return root;
	}

	/**
	 * Build a tree of only one level
	 * 
	 * @param number  to be factorized
	 * @param factors of primes
	 * @return flat tree
	 */
	public static MutableMatrixTreeNode<NamedInteger> buildFlat(int number, List<Integer> factors) {
		NamedInteger rootElement = new NamedInteger(number, number);
		MutableMatrixTreeNode<NamedInteger> root = new ListHazelMatrixTreeNode<>(null, /* elem */ rootElement,
				/* index */ number);

		String currentPrefix = rootElement.getName() + ".";
		for (int child : factors) {
			NamedInteger leafElement = new NamedInteger(currentPrefix + child + "-n" + root.getChildCount(), child);
			root.add(leafElement);
		}

		return root;
	}

}
