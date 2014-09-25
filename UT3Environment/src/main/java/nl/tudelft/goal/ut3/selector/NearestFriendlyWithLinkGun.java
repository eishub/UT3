package nl.tudelft.goal.ut3.selector;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cz.cuni.amis.pogamut.base.utils.math.DistanceUtils;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import cz.cuni.amis.utils.IFilter;
import cz.cuni.amis.utils.collections.MyCollections;

/**
 * Selects the closest friendly {@link Player}.
 * 
 * @author mpkorstanje
 * 
 */
public final class NearestFriendlyWithLinkGun extends ContextSelector {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NearestFriendlyWithLinkGun";
	}

	@Override
	public ILocated select(Collection<? extends ILocated> targets) {
		
		//Optimization, faster then selecting from collection.
		Map<UnrealId, Player> friends = modules.getPlayers().getVisibleFriends();
		List<Player> friendsWithLinkGun = MyCollections.getFiltered(friends.values(), new IFilter<Player>() {

			@Override
			public boolean isAccepted(Player p) {
				return UT3ItemType.LINK_GUN.equals(UT3ItemType.getItemType(p.getWeapon()));
			}
		});
		
		return DistanceUtils.getNearest(friendsWithLinkGun, modules.getInfo().getLocation());
	}

}
