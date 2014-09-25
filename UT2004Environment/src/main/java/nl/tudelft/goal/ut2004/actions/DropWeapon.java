package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;


public abstract class DropWeapon extends Action {

	@SuppressWarnings("unchecked")
	public DropWeapon() {
		setReplaces(DropWeapon.class);
	}

}
