package nl.tudelft.goal.unreal.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Smart queue for actions.
 * 
 * There is a limited bandwidth to transfer actions between GOAL and the
 * UT2004Bot. GOAL will send actions at a high rate and could saturate the
 * queue. To prevent this we check which actions are in the queue before adding
 * a new action.
 * 
 * There are three possible scenarios:
 * <ul>
 * <li>If the effects of a new action would cancel out the effects of an action
 * in the queue, the queued action is discarded.</li>
 * <li>If the effects of a new action have no effect on an action in the queue,
 * the action is put in.</li>
 * <li>If an action in the queue means the new action has no effect, the new
 * action id discarded.</li>
 * </ul>
 * 
 * 
 * @author mpkorstanje
 * 
 */
public final class ActionQueue {

	/**
	 * Queued up actions. Once the queue if full GOAL has to wait.
	 * 
	 */
	private final BlockingQueue<Action> actions;

	public ActionQueue(int capacity) {
		actions = new LinkedBlockingQueue<Action>(capacity);
	}

	/**
	 * Puts an action on the queue. If the queue is full this method will block.
	 * 
	 * The action is added intelligently to the queue. Should the results of
	 * this action happened to override or cancel out an action already in the
	 * queue the action in the queue will be removed.
	 * 
	 * @param action
	 * @throws InterruptedException
	 */
	public void put(Action action) throws InterruptedException {

		synchronized (actions) {

			// Check if adding action to queue will have effect
			for (Action queueAction : actions) {
				if (!action.hasEffect(queueAction)) {
					return;
				}
			}

			// If adding has effect, check which actions will have no effect.
			Iterator<Action> itterator = actions.iterator();
			while (itterator.hasNext()) {
				Action queueAction = itterator.next();

				if (action.replaces(queueAction)) {
					itterator.remove();
				}
			}

		}

		// Out side of sync block
		// Don't lock actions while waiting for queue to free up.
		actions.put(action);

	}

	/**
	 * Drains all actions from the queue.
	 * 
	 * @return a collection of actions to be executed.
	 */
	public Collection<Action> drain() {
		Collection<Action> drain = new ArrayList<Action>(actions.size());
		synchronized (actions) {
			actions.drainTo(drain);
		}

		return drain;
	}

}
