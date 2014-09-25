package nl.tudelft.goal.unreal.actions;

import nl.tudelft.goal.unreal.actions.Action;
import cz.cuni.amis.utils.exception.PogamutException;

public class BlockedAction extends Action {

	private final int id;

	@SuppressWarnings("unchecked")
	public BlockedAction(int id) {
		this.id = id;
		setBlockedBy(BlockedAction.class);
	}
	
	
	public int getId(){
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		BlockedAction other = (BlockedAction) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public void execute() throws PogamutException {
		// Does nothing.
	}

}