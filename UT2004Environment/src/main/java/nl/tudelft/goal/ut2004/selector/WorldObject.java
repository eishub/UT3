package nl.tudelft.goal.ut2004.selector;

import java.util.Collection;

import cz.cuni.amis.pogamut.base.communication.worldview.object.IWorldObject;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.utils.NullCheck;

/**
 * Select the world object given unrealId.
 * 
 * @author mpkorstanje
 * 
 */
public class WorldObject extends ContextSelector {

	protected final UnrealId id;

	public WorldObject(UnrealId id) {
		NullCheck.check(id, "id");
		this.id = id;
	}

	@Override
	public ILocated select(Collection<? extends ILocated> c) {
		// We want to select something with the given id.
		IWorldObject object = modules.getWorld().get(id);
		if(object instanceof ILocated)
			return (ILocated) object;
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorldObject [id=" + id + "]";
	}
}
