/**
 * Emohawk Bot, an implementation of the environment interface standard that
 * facilitates the connection between GOAL and Emohawk.
 *
 * Copyright (C) 2012 Emohawk Bot authors.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package nl.tudelft.goal.emohawk.agent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import SteeringProperties.ObstacleAvoidanceProperties;
import SteeringProperties.WalkAlongProperties;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.emohawk.agent.module.sensomotoric.EmoticonType;
import cz.cuni.amis.pogamut.emohawk.agent.module.sensomotoric.Place;
import cz.cuni.amis.pogamut.emohawk.bot.impl.EmohawkBotController;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.exception.PogamutException;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter.Type;
import eis.eis2java.util.AllPerceptsModule;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import nl.tudelft.goal.emohawk.messages.Action;
import nl.tudelft.goal.emohawk.messages.Percept;
import nl.tudelft.goal.emohawk.messages.UnrealIdOrLocation;
import nl.tudelft.goal.unreal.environment.MyAllPerceptsProvider;
import nl.tudelft.goal.unreal.environment.PerceptsReadyListener;
import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.Parameters;

public class EmohawkBotBehavior extends EmohawkBotController<UT2004Bot> implements MyAllPerceptsProvider {
	private Semaphore logic = new Semaphore(0, true);
	private WalkAlongProperties walkAlongProperties;
	private BotParameters parameters;
	protected AllPerceptsModule percepts;
	/**
	 * Queued up actions.
	 */
	private BlockingQueue<Action> actions = new LinkedBlockingQueue<Action>(1);
	private PerceptsReadyListener listener = null;
	private boolean initialized;

	@Override
	public void setPerceptsReadyListener(PerceptsReadyListener l) {
		this.listener = l;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void initializeController(UT2004Bot bot) {
		super.initializeController(bot);

		// Setup parameters
		UT2004AgentParameters parameters = bot.getParams();
		if ((parameters instanceof BotParameters)) {
			this.parameters = (BotParameters) parameters;
		} else {
			this.log.warning("Provided parameters were not a subclass of UnrealGoalParameters, using defaults.");
			this.parameters = new BotParameters();
		}
		Parameters defaults = BotParameters.getDefaults();
		this.parameters.assignDefaults(defaults);

		this.initialized = false;
	}

	@Override
	protected void initializeModules(UT2004Bot bot) {
		super.initializeModules(bot);

		this.steering.addObstacleAvoidanceSteering(new ObstacleAvoidanceProperties());
		this.walkAlongProperties = new WalkAlongProperties();
		this.walkAlongProperties.setDistanceFromThePartner(200);
		this.walkAlongProperties.setGiveWayToPartner(false);
		this.steering.addWalkAlongSteering(new WalkAlongProperties());

		// Setup percept module.
		try {
			this.percepts = new AllPerceptsModule(this);
		} catch (EntityException e) {
			throw new PogamutException("Could not create percept module", e);
		}

	}

	/**
	 * Prepares the initialization message for Gamebots using the
	 * {@link BotParameters} provided to the {@link UT2004BotRunner}.
	 *
	 */
	@Override
	public Initialize getInitializeCommand() {
		assert this.parameters != null;

		// Prepare init command
		Initialize init = super.getInitializeCommand();
		init.setDesiredSkill(this.parameters.getSkill());
		init.setSkin(this.parameters.getSkin().getUnrealName());
		init.setTeam(this.parameters.getTeam());
		init.setShouldLeadTarget(this.parameters.shouldLeadTarget());
		init.setLocation(this.parameters.getInitialLocation());
		init.setRotation(this.parameters.getInitialRotation());
		// Set log level.
		this.bot.getLogger().setLevel(this.parameters.getLogLevel());

		return init;
	}

	@Override
	public void logic() throws PogamutException {
		super.logic();

		// Mark that another logic iteration has began
		this.log.fine("--- Logic iteration ---");

		// 0. Execute all outstanding actions.
		while (!this.actions.isEmpty()) {
			this.actions.remove().execute();
		}

		// 1. Prepare new batch of percepts
		try {
			this.percepts.updatePercepts();
		} catch (PerceiveException e) {
			throw new PogamutException("Could not update percepts", e);
		}

		// 2. notify that percepts are ready (if this is first round).
		if (!this.initialized) {
			this.listener.notifyPerceptsReady();
			this.initialized = true;
		}
	}

	@Override
	public void botShutdown() {
		super.botShutdown();

		/*
		 * The bot has stopped so we can release a permit.
		 *
		 * TODO: This should not be required as it implies that actions and
		 * percepts are requested after the environment has been killed. However
		 * EIS currently allows different threads to manipulate the environment
		 * at the same time. Thus it may be possible for one thread to be
		 * waiting to acquire this agent while another shuts down the
		 * environment.
		 */
		this.logic.release();
	}

	//
	// ============
	// EIS Percepts
	// ============
	//

	@AsPercept(name = "navigation")
	public String navigation() {

		if (this.steering.isNavigating()) {
			return "following";
			// FIXME: Would like to uss navigator.isExecuting here but it does
			// not consider all it's components. traveling.
		} else if (this.pathExecutor.isExecuting() || this.getBackToNavGraph.isExecuting()
				|| this.runStraight.isExecuting()) {
			return "traveling";
		} else if (this.pathExecutor.isStuck()) {
			return "stuck";
		} else if (this.pathExecutor.isPathUnavailable()) {
			return "path_unavailable";
		} else if (this.pathExecutor.isTargetReached()) {
			return "destination_reached";

		} else {
			return "waiting";
		}
	}

	@AsPercept(name = "navPoint", multiplePercepts = true, filter = Type.ONCE)
	public List<Percept> perceptNavPoints() {
		Collection<NavPoint> navPoints = this.world.getAll(NavPoint.class).values();
		List<Percept> percepts = new ArrayList<Percept>(navPoints.size());

		for (NavPoint p : navPoints) {
			percepts.add(new Percept(p.getId(), p.getLocation(), p.getOutgoingEdges().keySet()));
		}

		return percepts;
	}

	// TODO: Because EIS2Java assumes objects returned are unchanging, which is
	// not the case with pogamut we can't use any other filters but ALWAYS
	// (default) and ONCE.
	@AsPercept(name = "person", multiplePercepts = true)
	public Collection<Percept> person() {

		Collection<Percept> persons = new ArrayList<Percept>();
		for (Player p : getPlayers().getVisiblePlayers().values()) {
			persons.add(new Percept(p.getId(), p.getName(), p.getLocation(), p.getRotation(), p.getEmotLeft(),
					p.getEmotCenter(), p.getEmotRight()));
		}
		return persons;
	}

	@AsPercept(name = "self")
	public Percept self() {
		AgentInfo info = getInfo();
		EmoticonType emoteLeft = EmoticonType.get(info.getSelf().getEmotLeft());
		EmoticonType emoteCenter = EmoticonType.get(info.getSelf().getEmotCenter());
		EmoticonType emoteRight = EmoticonType.get(info.getSelf().getEmotRight());

		return new Percept(info.getId(), info.getName(), info.getLocation(), info.getRotation(), emoteLeft, emoteCenter,
				emoteRight);
	}

	@AsPercept(name = "emoticon", multiplePercepts = true, filter = Type.ONCE)
	public Collection<EmoticonType> emoticon() {

		return Arrays.asList(EmoticonType.values());
	}

	// @AsPercept(name = "animation", multiplePercepts = true, filter =
	// Type.ONCE)
	// public Set<AnimType> animation() {
	// return animations.getAvailableAnimations();
	// }

	@AsPercept(name = "place", multiplePercepts = true, filter = Type.ONCE)
	public Collection<Place> place() {
		return this.places.getPlaces();
	}

	/**
	 * Returns a previously prepared batch of percepts.
	 *
	 * @return a previously prepared batch of percepts.
	 */
	@Override
	public Map<Method, Object> getAllPercepts() {
		return this.percepts.getAllPercepts();
	}

	//
	// =============
	// EIS ACTIONS
	// =============
	//
	@AsAction(name = "stop")
	public void stop() throws InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				EmohawkBotBehavior.this.steering.stopNavigation();
				EmohawkBotBehavior.this.navigation.stopNavigation();

			}
		});
	}

	@AsAction(name = "runTo")
	public void runTo(UnrealIdOrLocation destination) throws InterruptedException {
		final ILocated location = getLocation(destination);

		addAction(new Action() {

			@Override
			public void execute() {

				EmohawkBotBehavior.this.steering.stopNavigation();
				if (!EmohawkBotBehavior.this.move.isRunning()) {
					EmohawkBotBehavior.this.move.setRun();
				}
				EmohawkBotBehavior.this.navigation.navigate(location);
			}
		});
	}

	@AsAction(name = "walkTo")
	public void walkTo(UnrealIdOrLocation destination) throws InterruptedException {
		final ILocated location = getLocation(destination);

		addAction(new Action() {

			@Override
			public void execute() {
				EmohawkBotBehavior.this.steering.stopNavigation();
				if (EmohawkBotBehavior.this.move.isRunning()) {
					EmohawkBotBehavior.this.move.setWalk();
				}
				EmohawkBotBehavior.this.navigation.navigate(location);
			}
		});
	}

	private ILocated getLocation(UnrealIdOrLocation destination) {

		ILocated location;
		if (destination.isLocation()) {
			location = destination.getLocation();
		} else {
			// Assuming ID's were passed properly this must be a player or
			// navpoint.
			location = (ILocated) this.world.get(destination.getId());
		}
		return location;
	}

	@AsAction(name = "walkAlong")
	public void walkAlong(final Player partner) throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				EmohawkBotBehavior.this.navigation.stopNavigation();
				if (!EmohawkBotBehavior.this.move.isRunning()) {
					EmohawkBotBehavior.this.move.setRun();
				}
				EmohawkBotBehavior.this.walkAlongProperties.setPartnerName(partner.getName());
				EmohawkBotBehavior.this.steering.setWalkAlongSteering(EmohawkBotBehavior.this.walkAlongProperties);
				EmohawkBotBehavior.this.steering.startNavigation();
			}
		});
	}

	@AsAction(name = "emote")
	public void emote(final EmoticonType left, final EmoticonType center, final EmoticonType right)
			throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				EmohawkBotBehavior.this.emoticons.clearEmoticons();

				if (left == EmoticonType.NONE && center == EmoticonType.NONE && right == EmoticonType.NONE) {
					return;
				}

				if (left == EmoticonType.NONE && center != EmoticonType.NONE && right == EmoticonType.NONE) {
					EmohawkBotBehavior.this.emoticons.setCenterEmoticonType(center);
					return;
				}

				if (left != EmoticonType.NONE && center == EmoticonType.NONE && right != EmoticonType.NONE) {
					EmohawkBotBehavior.this.emoticons.setDoubleEmoticon(left, right);
					return;
				}

				EmohawkBotBehavior.this.emoticons.setTripleEmoticon(left, center, right);
			}
		});
	}

	// @AsAction(name = "animate")
	// public void animate(AnimType animation, boolean loop) throws ActException
	// {
	// animations.stopAnimation();
	// animations.playAnimation(animation, loop);
	// }

	@AsAction(name = "turn")
	public void turn(final int amount) throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				EmohawkBotBehavior.this.move.turnHorizontal(amount);

			}
		});
	}

	@AsAction(name = "turnTo")
	public void turnTo(final ILocated location) throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				EmohawkBotBehavior.this.move.turnTo(location);
			}
		});
	}

	@AsAction(name = "jump")
	public void jump() throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				EmohawkBotBehavior.this.move.jump();
			}
		});
	}

	@AsAction(name = "skip")
	public void skip() throws ActException {
		// Does nothing.
	}

	/**
	 * Queues up the action to be executed on the first evaluation of the logic
	 * cycle.
	 *
	 * @param action
	 * @throws InterruptedException
	 */
	public void addAction(Action action) throws InterruptedException {
		this.actions.put(action);
	}
}
