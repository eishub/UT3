package nl.tudelft.goal.ut3.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Shoot extends Action {

	@SuppressWarnings("unchecked")
	public Shoot() {
		setReplaces(Shoot.class, StopShooting.class);
	}
}
