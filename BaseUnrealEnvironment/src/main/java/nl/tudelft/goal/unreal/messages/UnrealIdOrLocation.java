package nl.tudelft.goal.unreal.messages;

import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.object.IWorldObject;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.utils.NullCheck;

public class UnrealIdOrLocation {

	@Override
	public String toString() {
		return isLocation() ? location.toString() : id.toString();
	}

	public boolean isLocation() {
		return location != null;
	}
	
	public boolean isUnrealId(){
		return id != null;
	}
	
	public Location getLocation(){
		return location;
	}

	private final UnrealId id;
	private final Location location;

	public UnrealIdOrLocation(Location location) {
		super();
		NullCheck.check(location, "location");
		this.location = location;
		this.id = null;
	}

	public UnrealIdOrLocation(UnrealId id) {
		super();
		NullCheck.check(id, "id");
		this.location = null;
		this.id = id;
	}

	public UnrealId getId() {
		return id;
	}

	/**
	 * Transforms an UnrealIdOrLocation to an ILocated. When this is a location
	 * it directly returned. Otherwise the UnrealId Is resolved against the bots
	 * own Id and those of objects in the world view.
	 * 
	 * When the UnrealId can not be resolved this method returns null.
	 * 
	 * @param world
	 * @param info
	 * @return
	 */
	public ILocated toILocated(IWorldView world, AgentInfo info) {

		if (isLocation()) {
			return location;
		}

		// ID could refer to this bot.
		if (id.equals(info.getId())) {
			return info;
		}

		return toILocated(world);
	}

	/**
	 * Transforms an UnrealIdOrLocation to an ILocated. When this is a location
	 * it directly returned. Otherwise the UnrealId Is resolved against objects
	 * in the world view.
	 * 
	 * When the UnrealId can not be resolved this method returns null.
	 * 
	 * @param world
	 * @param info
	 * @return
	 */

	public ILocated toILocated(IWorldView world) {
		// Try to resolve id to an ILocated
		IWorldObject object = world.get(id);

		if (object instanceof ILocated) {
			return (ILocated) object;
		}

		// Not found
		return null;
	}

}
