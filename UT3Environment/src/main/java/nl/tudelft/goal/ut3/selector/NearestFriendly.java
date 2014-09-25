package nl.tudelft.goal.ut3.selector;

import java.util.Collection;

import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

/**
 * Selects the closest friendly {@link Player}.
 * 
 * @author mpkorstanje
 * 
 */
public final class NearestFriendly extends ContextSelector {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NearestFriendly";
	}

	@Override
	public ILocated select(Collection<? extends ILocated> targets) {
		//Optimization, faster then selecting from collection.
		return modules.getPlayers().getNearestVisibleFriend();
	}

}
