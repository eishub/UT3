package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Combo extends Action {

	
	@SuppressWarnings("unchecked")
	public Combo() {
		setBlockedBy(Combo.class);
	}
}
