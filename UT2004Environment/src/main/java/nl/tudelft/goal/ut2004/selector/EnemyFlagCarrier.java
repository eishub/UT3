package nl.tudelft.goal.ut2004.selector;

import java.util.Collection;

import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;

/**
 * Selects the enemy flag carrier.
 * 
 * @author mpkorstanje
 *
 */
public class EnemyFlagCarrier extends ContextSelector {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EnemyFlagCarrier";
	}

	@Override
	public ILocated select(Collection<? extends ILocated> c) {

		FlagInfo flag = modules.getGame().getCTFFlag(modules.getInfo().getTeam());
		UnrealId holderId = flag.getHolder();
		if (holderId == null) {
			return null;
		}

		return modules.getPlayers().getPlayer(holderId);
	}
		

}
