package nl.tudelft.goal.ut3.selector;

import nl.tudelft.goal.unreal.util.Selector;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.ut3.bot.impl.UT3BotModuleController;
import cz.cuni.amis.utils.NullCheck;

/**
 * Because the context of the bot that will used the selector is not availalble
 * during translation. This allows that context to be provided after
 * translation.
 * 
 * @author mpkorstanje
 * 
 */
public abstract class ContextSelector implements Selector<ILocated> {

	@SuppressWarnings("rawtypes")
	protected UT3BotModuleController modules;

	@SuppressWarnings("rawtypes")
	public ContextSelector setContext(UT3BotModuleController modules) {
		NullCheck.check(modules, "modules");
		this.modules = modules;
		return this;
	}
}
