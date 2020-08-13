package matrixtree.exception;

/**
 * Exception to represent a variable not belonging in the correct domain of input.
 * 
 * @author Agustinus Lawandy
 */
public class BadDomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Input variable %s (%s) is not contained in domain %s";
	private final String name;
	private final String value;
	private final String domain;

	/**
	 * Instantiate domain exception.
	 * @param name   of the variable
	 * @param value  of the variable
	 * @param domain that the variable should be in, e.g. [1,Inf)
	 */
	public BadDomainException(Object name, Object value, String domain) {
		super(String.format(MSG, name, value, domain));
		this.name = name.toString();
		this.value = value.toString();
		this.domain = domain;
	}

	@Override
	public String toString() {
		return BadDomainException.class.getSimpleName() + String.format(MSG, name, value, domain);
	}
}
