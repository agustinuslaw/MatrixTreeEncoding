package matrixtree.test.model;

import java.util.Objects;

import matrixtree.validation.Precondition;

/**
 * @author Agustinus Lawandy
 * @since
 */
public class NamedInteger extends Number implements Comparable<Integer> {

	private static final long serialVersionUID = -5342271489117847170L;
	private String name = "";
	private Integer number = 0;

	public NamedInteger(Number name, Integer number) {
		super();
		Precondition.checkArgument(name != null, "null name");
		Precondition.checkArgument(number != null, "null number");

		this.name = name.toString();
		this.number = number;
	}

	public NamedInteger(String name, Integer number) {
		super();
		Precondition.checkArgument(name != null && !name.isEmpty(), "null or empty name");
		Precondition.checkArgument(number != null, "null number");

		this.name = name;
		this.number = number;
	}

	@Override
	public String toString() {
		return "[name=" + name + ",num=" + number + "]";
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedInteger other = (NamedInteger) obj;
		return Objects.equals(name, other.name) && Objects.equals(number, other.number);
	}

	/**
	 * @return
	 * @see java.lang.Number#intValue()
	 */
	public int intValue() {
		return number.intValue();
	}

	/**
	 * @return
	 * @see java.lang.Number#longValue()
	 */
	public long longValue() {
		return number.longValue();
	}

	/**
	 * @return
	 * @see java.lang.Number#floatValue()
	 */
	public float floatValue() {
		return number.floatValue();
	}

	/**
	 * @return
	 * @see java.lang.Number#doubleValue()
	 */
	public double doubleValue() {
		return number.doubleValue();
	}

	/**
	 * @return
	 * @see java.lang.Number#byteValue()
	 */
	public byte byteValue() {
		return number.byteValue();
	}

	/**
	 * @return
	 * @see java.lang.Number#shortValue()
	 */
	public short shortValue() {
		return number.shortValue();
	}

	/**
	 * @param anotherInteger
	 * @return
	 * @see java.lang.Integer#compareTo(java.lang.Integer)
	 */
	public int compareTo(Integer anotherInteger) {
		return number.compareTo(anotherInteger);
	}
}
