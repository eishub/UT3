package nl.tudelft.goal.ut2004.selector;

import java.util.Collection;

import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;

/**
 * Selects a location given before hand.
 * 
 * @author mpkorstanje
 * 
 */
public class None extends ContextSelector {


	public None() {
	}

	@Override
	public ILocated select(Collection<? extends ILocated> targets) {
		return null;
	}

	@Override
	public String toString() {
		return "None";
	}



}
