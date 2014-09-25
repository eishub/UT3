package nl.tudelft.goal.unreal.messages;

/**
 * Represents that there is no data. 
 */
public final class None {
	
	private final String none =  "none";

	public String id(){
		return none;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((none == null) ? 0 : none.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		None other = (None) obj;
		if (none == null) {
			if (other.none != null)
				return false;
		} else if (!none.equals(other.none))
			return false;
		return true;
	}

}
