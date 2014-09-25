package nl.tudelft.goal.ut3.selector;

import java.util.Collection;

import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

/**
 * Selects the closest enemy {@link Player}.
 * 
 * @author mpkorstanje
 *
 */
public class NearestEnemy extends ContextSelector {


	@Override
	public ILocated select(Collection<? extends ILocated> targets) {
		return modules.getPlayers().getNearestVisibleEnemy();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NearestEnemy";
	}

}
