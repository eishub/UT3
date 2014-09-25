package nl.tudelft.goal.ut2004.actions;

import nl.tudelft.goal.unreal.actions.Action;

/**
 *
 * @author Michiel
 */
public abstract class Chat extends Action {
    
    @SuppressWarnings("unchecked")
    public Chat() {
        setReplaces(Chat.class);
    }
}
