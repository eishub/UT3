package nl.tudelft.goal.unreal.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import nl.tudelft.goal.unreal.actions.Action;
import nl.tudelft.goal.unreal.actions.ActionQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.amis.utils.exception.PogamutException;

public class ActionQueueTest {

	ActionQueue queue;

	@Before
	public void setUp() throws Exception {
		queue = new ActionQueue(5);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void putReplaceActions() throws InterruptedException {

		queue.put(new ReplaceAction(0));
		queue.put(new ReplaceAction(1));
		queue.put(new ReplaceAction(2));
		queue.put(new ReplaceAction(3));
		queue.put(new ReplaceAction(4));

		Collection<Action> actions = queue.drain();
		assertTrue(actions.size() == 1);
		assertTrue(actions.contains(new ReplaceAction(4)));

	}

	@Test
	public void putBlockedActions() throws InterruptedException {

		queue.put(new BlockedAction(0));
		queue.put(new BlockedAction(1));
		queue.put(new BlockedAction(2));
		queue.put(new BlockedAction(3));
		queue.put(new BlockedAction(4));

		Collection<Action> actions = queue.drain();
		assertTrue(actions.size() == 1);
		assertTrue(actions.contains(new BlockedAction(0)));

	}

	@Test
	public void putReplaceAndBlockedActions() throws InterruptedException {
		queue.put(new BlockedAction(0));
		queue.put(new ReplaceAction(0));

		queue.put(new BlockedAction(1));
		queue.put(new ReplaceAction(1));

		queue.put(new BlockedAction(2));
		queue.put(new ReplaceAction(2));

		queue.put(new BlockedAction(3));
		queue.put(new ReplaceAction(3));

		queue.put(new BlockedAction(4));
		queue.put(new ReplaceAction(4));

		Collection<Action> actions = queue.drain();
		assertTrue(actions.size() == 2);
		assertTrue(actions.contains(new BlockedAction(0)));
		assertTrue(actions.contains(new ReplaceAction(4)));

	}

	@Test
	public void putAnonClassReplaceAction() throws InterruptedException {

		ReplaceAction action0 = new ReplaceAction(0) {

			@Override
			public void execute() throws PogamutException {
				super.execute();
				// Still does nothing.
			}

		};
		queue.put(action0);

		queue.put(new ReplaceAction(1));
		queue.put(new ReplaceAction(2));
		queue.put(new ReplaceAction(3));

		ReplaceAction action4 = new ReplaceAction(4) {

			@Override
			public void execute() throws PogamutException {
				super.execute();
				// Still does nothing.
			}

		};

		queue.put(action4);

		Collection<Action> actions = queue.drain();
		assertTrue(actions.size() == 1);
		assertFalse(actions.contains(action0));
		assertTrue(actions.contains(action4));


	}
	

	@Test
	public void putAnonClassBlockedAction() throws InterruptedException {

		BlockedAction action0 = new BlockedAction(0) {

			@Override
			public void execute() throws PogamutException {
				super.execute();
				// Still does nothing.
			}

		};
		queue.put(action0);

		queue.put(new BlockedAction(1));
		queue.put(new BlockedAction(2));
		queue.put(new BlockedAction(3));

		BlockedAction action4 = new BlockedAction(4) {

			@Override
			public void execute() throws PogamutException {
				super.execute();
				// Still does nothing.
			}

		};

		queue.put(action4);

		Collection<Action> actions = queue.drain();
		assertTrue(actions.size() == 1);
		assertTrue(actions.contains(action0));
		assertFalse(actions.contains(action4));


	}
}
