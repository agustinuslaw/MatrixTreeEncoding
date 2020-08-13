package matrixtree.validation;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.VerifyException;

import matrixtree.exception.VerificationException;

/**
 * Enforces verifications regarding the validity of the output and processes of this project. Inspired by Guava Verify,
 * but uses own exception.
 * 
 * @author Agustinus Lawandy
 */
public class Verification {
	
	/**
	 * @param expression must be true
	 * @throws VerificationException
	 */
	public static void verify(boolean expression) {
		if (!expression) {
			throw new VerificationException();
		}
	}
	/**
	 * @param expression must be true
	 * @param message to describe the exception
	 * @throws VerificationException
	 */
	public static void verify(boolean expression, @Nullable String message) {
		if (!expression) {
			throw new VerifyException(message);
		}
	}

	private Verification() {
		throw new AssertionError(getClass().getSimpleName() + " should not be instantiated");
	}
}
