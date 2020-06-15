package matrixtree.exception;

public class BadArgumentException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Input variable %s (%s) is not contained in domain %s";
	private final String name;
	private final String value;
	private final String domain;

	public BadArgumentException(Object name, Object value, String domain) {
		super(String.format(MSG, name, value, domain));
		this.name = name.toString();
		this.value = value.toString();
		this.domain = domain;
	}

	@Override
	public String toString() {
		return BadArgumentException.class.getSimpleName() + String.format(MSG, name, value, domain);
	}
}
