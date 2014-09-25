package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Respawn extends Action {

	@SuppressWarnings("unchecked")
	public Respawn() {
		setReplaces(Navigate.class, Continue.class, Stop.class, Respawn.class);
	}
}
