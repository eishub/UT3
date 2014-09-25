package nl.tudelft.goal.ut3.selector;

import java.util.Collection;

import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.utils.NullCheck;

/**
 * Selects a location given before hand.
 * 
 * @author mpkorstanje
 * 
 */
public class ALocation extends ContextSelector {

	private ILocated location;

	public ALocation(ILocated location) {
		NullCheck.check(location, "location");
		this.location = location;
	}

	@Override
	public ILocated select(Collection<? extends ILocated> targets) {
		return location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ALocation [location=" + location + "]";
	}

}
