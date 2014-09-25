package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Prefer extends Action {

	@SuppressWarnings("unchecked")
	public Prefer() {
		setReplaces(Prefer.class);
	}
}
