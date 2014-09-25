package nl.tudelft.goal.ut3.actions;

import nl.tudelft.goal.unreal.actions.Action;

public abstract class Stop extends Action {
	
	@SuppressWarnings("unchecked")
	public Stop() {
		setReplaces(Navigate.class, Continue.class, Stop.class);

	}

}
