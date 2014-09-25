package nl.tudelft.goal.emohawk.translators;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

public class UnrealIdOrLocation {


	@Override
	public String toString() {
		return isLocation() ? location.toString() : id.toString();
	}

	private final UnrealId id;
	private final Location location;

	public UnrealIdOrLocation(Location location) {
		super();
		this.location = location;
		this.id = null;
	}

	public UnrealIdOrLocation(UnrealId id) {
		super();
		this.location = null;
		this.id = id;
	}

	public UnrealId getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isUnrealId() {
		return id != null;
	}

	public boolean isLocation() {
		return location != null;
	}

}
