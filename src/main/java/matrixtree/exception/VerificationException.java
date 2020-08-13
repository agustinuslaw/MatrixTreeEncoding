package matrixtree.exception;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represent failure to comply with a verification requirement.
 * 
 * @author Agustinus Lawandy
 */
public class VerificationException extends RuntimeException {
	private static final long serialVersionUID = 8933348884576908889L;

	public VerificationException() {
		super();
	}

	/**
	 * @param message description
	 * @param cause of exception
	 */
	public VerificationException(@Nullable String message, @Nullable Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message description
	 */
	public VerificationException(@Nullable String message) {
		super(message);
	}

	/**
	 * @param cause of exception
	 */
	public VerificationException(@Nullable Throwable cause) {
		super(cause);
	}

}
