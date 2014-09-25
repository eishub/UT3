package nl.tudelft.goal.ut3.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Prefer extends Action {

	@SuppressWarnings("unchecked")
	public Prefer() {
		setReplaces(Prefer.class);
	}
}
