package nl.tudelft.goal.ut3.messages;

import java.util.ArrayList;

import nl.tudelft.goal.ut3.selector.ContextSelector;
import cz.cuni.amis.pogamut.ut3.bot.impl.UT3BotModuleController;
import cz.cuni.amis.utils.NullCheck;

public class SelectorList extends ArrayList<ContextSelector> {

	/**
	 * Serial Version UID is data
	 */
	private static final long serialVersionUID = 201205071622L;
	
	public SelectorList(ContextSelector... selectors){
		for(ContextSelector s : selectors){
			add(s);
		}
	}
	
	@SuppressWarnings("rawtypes")	
	public SelectorList setContext(UT3BotModuleController modules){
		NullCheck.check(modules, "modules");
		
		for(ContextSelector c : this){
			c.setContext(modules);
		}
		
		return this;
	}
}
