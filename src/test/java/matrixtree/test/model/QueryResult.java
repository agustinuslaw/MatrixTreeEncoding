package matrixtree.test.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Agustinus Lawandy
 * @since 
 */
public class QueryResult {
	public long duration = -1;
	public TimeUnit unit = TimeUnit.MILLISECONDS;
	public Set<String> names = new HashSet<>();
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(duration, names, unit);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryResult other = (QueryResult) obj;
		return duration == other.duration && Objects.equals(names, other.names) && unit == other.unit;
	}



	@Override
	public String toString() {
		return "QueryResult [duration=" + duration + ", unit=" + unit + ", names=" + names + "]";
	}
	
	
}
