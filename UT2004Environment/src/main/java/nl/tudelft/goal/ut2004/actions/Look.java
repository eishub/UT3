package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Look extends Action {

	@SuppressWarnings("unchecked")
	public Look() {
		setReplaces(Look.class);
	}
}
