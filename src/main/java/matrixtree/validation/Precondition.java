package matrixtree.validation;

import matrixtree.exception.BadDomainException;

/**
 * This precondition takes inspiration from Guava Preconditions. Should be called before client calls to check the
 * validity of the arguments.
 * 
 * @author Agustinus Lawandy
 */
public class Precondition {

	/**
	 * Checks the domain of a variable.
	 * 
	 * @param expression that must remain true
	 * @param name       of variable e.g. temperature
	 * @param value      of variable e.g. 273 'K'
	 * @param domain     that must be satisfied e.g. [0, inf) 'K'
	 */
	public static void checkDomain(boolean expression, String name, Object value, String domain) {
		if (!expression) {
			throw new BadDomainException(name, value, domain);
		}
	}

	/**
	 * Checks that the argument is satisfied.
	 * 
	 * @param expression
	 * @param message
	 */
	public static void checkArgument(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	private Precondition() {
		throw new AssertionError(getClass().getSimpleName() + " should not be instantiated");
	}
}
