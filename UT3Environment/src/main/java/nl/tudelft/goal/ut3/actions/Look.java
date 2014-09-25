package nl.tudelft.goal.ut3.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Look extends Action {

	@SuppressWarnings("unchecked")
	public Look() {
		setReplaces(Look.class);
	}
}
