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

import nl.tudelft.goal.emohawk.messages.Action;
import nl.tudelft.goal.emohawk.messages.Percept;
import nl.tudelft.goal.emohawk.messages.UnrealIdOrLocation;
import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.Parameters;
import SteeringProperties.ObstacleAvoidanceProperties;
import SteeringProperties.WalkAlongProperties;
import cz.cuni.amis.pogamut.base.utils.logging.IAgentLogger;
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
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;

public class EmohawkBotBehavior extends EmohawkBotController<UT2004Bot> implements AllPerceptsProvider {

	private Semaphore logic = new Semaphore(0, true);

	private WalkAlongProperties walkAlongProperties;

	private BotParameters parameters;

	protected AllPerceptsModule percepts;

	/**
	 * Queued up actions.
	 */
	private BlockingQueue<Action> actions = new LinkedBlockingQueue<Action>(1);

	@SuppressWarnings("rawtypes")
	public void initializeController(UT2004Bot bot) {
		super.initializeController(bot);

		// Setup parameters
		IAgentLogger logger = bot.getLogger();
		UT2004AgentParameters parameters = bot.getParams();
		if ((parameters instanceof BotParameters)) {
			this.parameters = (BotParameters) parameters;
		} else {
			log.warning("Provided parameters were not a subclass of UnrealGoalParameters, using defaults.");
			this.parameters = new BotParameters();
		}
		Parameters defaults = BotParameters.getDefaults();
		this.parameters.assignDefaults(defaults);
	}

	protected void initializeModules(UT2004Bot bot) {
		super.initializeModules(bot);

		steering.addObstacleAvoidanceSteering(new ObstacleAvoidanceProperties());
		walkAlongProperties = new WalkAlongProperties();
		walkAlongProperties.setDistanceFromThePartner(200);
		walkAlongProperties.setGiveWayToPartner(false);
		steering.addWalkAlongSteering(new WalkAlongProperties());

		// Setup percept module.
		try {
			percepts = new AllPerceptsModule(this);
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
		assert parameters != null;

		// Prepare init command
		Initialize init = super.getInitializeCommand();
		init.setDesiredSkill(parameters.getSkill());
		init.setSkin(parameters.getSkin().getUnrealName());
		init.setTeam(parameters.getTeam());
		init.setShouldLeadTarget(parameters.shouldLeadTarget());
		init.setLocation(parameters.getInitialLocation());
		init.setRotation(parameters.getInitialRotation());
		// Set log level.
		bot.getLogger().setLevel(this.parameters.getLogLevel());

		return init;
	}

	@Override
	public void logic() throws PogamutException {
		super.logic();

		// Mark that another logic iteration has began
		log.fine("--- Logic iteration ---");

		// 0. Execute all outstanding actions.
		while (!actions.isEmpty()) {
			actions.remove().execute();
		}

		// 1. Prepare new batch of percepts
		try {
			percepts.updatePercepts();
		} catch (PerceiveException e) {
			throw new PogamutException("Could not update percepts", e);
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
		logic.release();
	}

	//
	// ============
	// EIS Percepts
	// ============
	//

	@AsPercept(name = "navigation")
	public String navigation() {

		if (steering.isNavigating()) {
			return "following";
			// FIXME: Would like to uss navigator.isExecuting here but it does
			// not consider all it's components. traveling.
		} else if (pathExecutor.isExecuting() || getBackToNavGraph.isExecuting() || runStraight.isExecuting()) {
			return "traveling";
		} else if (pathExecutor.isStuck()) {
			return "stuck";
		} else if (pathExecutor.isPathUnavailable()) {
			return "path_unavailable";
		} else if (pathExecutor.isTargetReached()) {
			return "destination_reached";

		} else {
			return "waiting";
		}
	}

	@AsPercept(name = "navPoint", multiplePercepts = true, filter = Type.ONCE)
	public List<Percept> perceptNavPoints() {
		Collection<NavPoint> navPoints = world.getAll(NavPoint.class).values();
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
			persons.add(new Percept(p.getId(), p.getName(), p.getLocation(), p.getRotation(), p.getEmotLeft(), p.getEmotCenter(), p
					.getEmotRight()));
		}
		return persons;
	}

	@AsPercept(name = "self")
	public Percept self() {
		AgentInfo info = getInfo();
		EmoticonType emoteLeft = EmoticonType.get(info.getSelf().getEmotLeft());
		EmoticonType emoteCenter = EmoticonType.get(info.getSelf().getEmotCenter());
		EmoticonType emoteRight = EmoticonType.get(info.getSelf().getEmotRight());

		return new Percept(info.getId(), info.getName(), info.getLocation(), info.getRotation(), emoteLeft, emoteCenter, emoteRight);
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
		return places.getPlaces();
	}

	/**
	 * Returns a previously prepared batch of percepts.
	 * 
	 * @return a previously prepared batch of percepts.
	 */
	public Map<Method, Object> getAllPercepts() {
		return percepts.getAllPercepts();
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
				steering.stopNavigation();
				navigation.stopNavigation();

			}
		});
	}

	@AsAction(name = "runTo")
	public void runTo(UnrealIdOrLocation destination) throws InterruptedException {
		final ILocated location = getLocation(destination);

		addAction(new Action() {

			@Override
			public void execute() {

				steering.stopNavigation();
				if (!move.isRunning())
					move.setRun();
				navigation.navigate(location);
			}
		});
	}

	@AsAction(name = "walkTo")
	public void walkTo(UnrealIdOrLocation destination) throws InterruptedException {
		final ILocated location = getLocation(destination);

		addAction(new Action() {

			@Override
			public void execute() {
				steering.stopNavigation();
				if (move.isRunning())
					move.setWalk();
				navigation.navigate(location);
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
			location = (ILocated) world.get(destination.getId());
		}
		return location;
	}

	@AsAction(name = "walkAlong")
	public void walkAlong(final Player partner) throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				navigation.stopNavigation();
				if (!move.isRunning())
					move.setRun();
				walkAlongProperties.setPartnerName(partner.getName());
				steering.setWalkAlongSteering(walkAlongProperties);
				steering.startNavigation();
			}
		});
	}

	@AsAction(name = "emote")
	public void emote(final EmoticonType left, final EmoticonType center, final EmoticonType right) throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				emoticons.clearEmoticons();

				if (left == EmoticonType.NONE && center == EmoticonType.NONE && right == EmoticonType.NONE) {
					return;
				}

				if (left == EmoticonType.NONE && center != EmoticonType.NONE && right == EmoticonType.NONE) {
					emoticons.setCenterEmoticonType(center);
					return;
				}

				if (left != EmoticonType.NONE && center == EmoticonType.NONE && right != EmoticonType.NONE) {
					emoticons.setDoubleEmoticon(left, right);
					return;
				}

				emoticons.setTripleEmoticon(left, center, right);
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
				move.turnHorizontal(amount);

			}
		});
	}

	@AsAction(name = "turnTo")
	public void turnTo(final ILocated location) throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				move.turnTo(location);
			}
		});
	}

	@AsAction(name = "jump")
	public void jump() throws ActException, InterruptedException {
		addAction(new Action() {

			@Override
			public void execute() {
				move.jump();
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
		actions.put(action);
	}
}
