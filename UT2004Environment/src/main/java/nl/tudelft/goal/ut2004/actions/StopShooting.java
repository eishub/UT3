package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class StopShooting extends Action {

	@SuppressWarnings("unchecked")
	public StopShooting() {
		setReplaces(Shoot.class, StopShooting.class);

	}
}
