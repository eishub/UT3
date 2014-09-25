package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;


public abstract class Navigate extends Action {

	@SuppressWarnings("unchecked")
	public Navigate() {
		setReplaces(Navigate.class, Continue.class, Stop.class);
	}

}
