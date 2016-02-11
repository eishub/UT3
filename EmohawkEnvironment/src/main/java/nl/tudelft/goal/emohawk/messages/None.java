package nl.tudelft.goal.emohawk.messages;

/**
 * Represents that there is no data.
 */
public final class None {

	private final String none = "none";

	public String id() {
		return this.none;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.none == null) ? 0 : this.none.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		None other = (None) obj;
		if (this.none == null) {
			if (other.none != null) {
				return false;
			}
		} else if (!this.none.equals(other.none)) {
			return false;
		}
		return true;
	}

}
