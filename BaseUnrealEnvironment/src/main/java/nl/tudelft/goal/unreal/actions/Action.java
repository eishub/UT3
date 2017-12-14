package nl.tudelft.goal.unreal.actions;

import cz.cuni.amis.utils.exception.PogamutException;

/**
 * Basic class for actions executed by the {@link UT3BotBehavior}. Before
 * actions are executed they are are queued up in the {@link ActionQueue}. To
 * ensure the queue does not overflow the ActionQueue checks which actions can
 * replace other actions in the queue and if this action would have any effect.
 * 
 * Actions can be replaced when executing this action after them would have no
 * effect. This is indicated by using the {@link #setReplaces(Class...)} method.
 * 
 * Actions have no effect if there is an action in the queue which prevents this
 * action from having any effect. this is indicated by using the
 * {@link #setBlockedBy(Class...)} .
 * 
 * 
 */
public abstract class Action {

	@SuppressWarnings("unchecked")
	private Class<? extends Action>[] replaces = new Class[0];
	@SuppressWarnings("unchecked")
	private Class<? extends Action>[] blocks = new Class[0];

	public abstract void execute() throws PogamutException;

	/**
	 * Sets which actions will have no effect if this action is executed after
	 * them.
	 * 
	 * @param replaces
	 */
	public void setReplaces(Class<? extends Action>... replaces) {
		this.replaces = replaces;
	}

	/**
	 * @param action
	 *            the action to replace
	 * @return true if true if executing the argument action before this action
	 *         will have no effect. This action overrides all effects of the
	 *         argument action
	 */
	public boolean replaces(Action action) {

		for (Class<? extends Action> replace : replaces) {
			// Check if the class of the action is a (sub)class of the actions
			// this action replaces
			if (replace.isAssignableFrom(action.getClass())) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Sets which actions will prevent this action from having any effect if
	 * executed after them.
	 * 
	 * @param blocks
	 */
	public void setBlockedBy(Class<? extends Action>... blocks) {
		this.blocks = blocks;
	}

	/**
	 * 
	 * @param action
	 *            the action to check
	 * @return true if executing the argument action does not impede the
	 *         execution of this action. When false executing this action after
	 *         the argument action will have no effect
	 */
	public boolean hasEffect(Action action) {

		for (Class<? extends Action> block : blocks) {
			// Check if the class of the action is a (sub)class of the actions
			// this action is blocked by.
			if (block.isAssignableFrom(action.getClass())) {
				return false;
			}
		}

		return true;

	}

}
