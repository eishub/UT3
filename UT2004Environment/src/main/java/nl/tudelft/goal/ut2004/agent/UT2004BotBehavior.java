/**
 * UT2004 Environment, an implementation of the environment interface standard that 
 * facilitates the connection between GOAL and UT2004. 
 * 
 * Copyright (C) 2012 UT2004 Environment authors.
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

package nl.tudelft.goal.ut2004.agent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import cz.cuni.amis.pogamut.base.agent.navigation.IPathPlanner;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.EventListener;
import cz.cuni.amis.pogamut.base.communication.worldview.object.IWorldObject;
import cz.cuni.amis.pogamut.base.utils.math.DistanceUtils;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensomotoric.Weapon;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPref;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.UT2004AStarPathPlanner;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.UT2004GetBackToNavGraph;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.UT2004Navigation;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.UT2004PathExecutor;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.UT2004RunStraight;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.loquenavigator.LoqueNavigator;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.stuckdetector.UT2004DistanceStuckDetector;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.stuckdetector.UT2004PositionStuckDetector;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.stuckdetector.UT2004TimeStuckDetector;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotModuleController;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.GetPath;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.BotKilled;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Item;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.PathList;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.PlayerKilled;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.exception.PogamutException;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter.Type;
import eis.eis2java.util.AllPerceptsModule;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import nl.tudelft.goal.unreal.actions.Action;
import nl.tudelft.goal.unreal.actions.ActionQueue;
import nl.tudelft.goal.unreal.environment.MyAllPerceptsProvider;
import nl.tudelft.goal.unreal.environment.PerceptsReadyListener;
import nl.tudelft.goal.unreal.floydwarshall.SharedFloydWarshallMap;
import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.None;
import nl.tudelft.goal.unreal.messages.Parameters;
import nl.tudelft.goal.unreal.messages.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import nl.tudelft.goal.unreal.util.Selector;
import nl.tudelft.goal.ut2004.actions.Chat;
import nl.tudelft.goal.ut2004.actions.DropWeapon;
import nl.tudelft.goal.ut2004.actions.Look;
import nl.tudelft.goal.ut2004.actions.Navigate;
import nl.tudelft.goal.ut2004.actions.Prefer;
import nl.tudelft.goal.ut2004.actions.Respawn;
import nl.tudelft.goal.ut2004.actions.Shoot;
import nl.tudelft.goal.ut2004.actions.Stop;
import nl.tudelft.goal.ut2004.messages.Combo;
import nl.tudelft.goal.ut2004.messages.FireMode;
import nl.tudelft.goal.ut2004.messages.FlagState;
import nl.tudelft.goal.ut2004.messages.Scope;
import nl.tudelft.goal.ut2004.messages.SelectorList;
import nl.tudelft.goal.ut2004.messages.WeaponPrefList;
import nl.tudelft.goal.ut2004.selector.ContextSelector;
import nl.tudelft.goal.ut2004.selector.NearestEnemy;
import nl.tudelft.goal.ut2004.util.Team;
import nl.tudelft.pogamut.unreal.agent.module.sensor.Projectiles;
import nl.tudelft.pogamut.unreal.agent.module.shooting.WeaponryShooting;
import nl.tudelft.pogamut.unreal.agent.module.shooting.util.FocusProvider;
import nl.tudelft.pogamut.unreal.agent.module.shooting.util.OrderedFocusProvider;
import nl.tudelft.pogamut.ut2004.agent.module.sensor.UT2004Projectiles;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.AssaultRifleShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.BioRifleShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.FlakCannonShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.LigthningGunShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.LinkGunShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.MinigunShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.RocketLauncherShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.ShieldGunShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.ShockRifleShooting;
import nl.tudelft.pogamut.ut2004.agent.module.shooting.weapon.SniperRifleShooting;

@SuppressWarnings("rawtypes")
public class UT2004BotBehavior extends UT2004BotModuleController<UT2004Bot> implements MyAllPerceptsProvider {

	protected List<ContextSelector> targetSelector = new ArrayList<ContextSelector>();
	protected List<ContextSelector> lookSelector = new ArrayList<ContextSelector>();
	protected Projectiles projectiles;
	protected WeaponryShooting weaponShooting;
	protected FocusProvider lookFocus = new FocusProvider();
	protected OrderedFocusProvider focus = new OrderedFocusProvider();

	protected AllPerceptsModule percepts;
	/**
	 * Settings for the bot.
	 */
	protected BotParameters parameters;

	/**
	 * Maximum size of the action queue.
	 * 
	 * The reason the action queue has a size of eight is because at most eight
	 * actions can sensibly be executed in combination. After that GOAL must be
	 * sending duplicate actions.
	 */
	private static final int ACTION_QUEUE_SIZE = 8;

	/**
	 * Queued up actions. Once the queue if full GOAL has to wait.
	 * 
	 */
	private ActionQueue actions = new ActionQueue(ACTION_QUEUE_SIZE);

	private long logicIteration = 0;
	protected long actionCount;

	PerceptsReadyListener listener = null;

	@Override
	public void setPerceptsReadyListener(PerceptsReadyListener l) {
		listener = l;
	}

	@Override
	public void initializeController(UT2004Bot bot) {
		super.initializeController(bot);

		// Setup parameters
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

	/**
	 * Initialize projectiles and weaponshooting modules.
	 */
	@Override
	protected void initializeModules(UT2004Bot bot) {
		super.initializeModules(bot);

		projectiles = new UT2004Projectiles(bot, info);
		weaponShooting = new WeaponryShooting(bot, info, weaponry, weaponPrefs, shoot);

		// Setup percept module.
		try {
			percepts = new AllPerceptsModule(this);
		} catch (EntityException e) {
			throw new PogamutException("Could not create percept module", e);
		}

		initializeWeaponShootings();
	}

	/**
	 * Adds handlers to deal with different weapons.
	 */
	protected void initializeWeaponShootings() {
		weaponShooting.addWeaponShooting(new LinkGunShooting(bot, info, shoot, weaponry));
		weaponShooting.addWeaponShooting(new ShockRifleShooting(bot, info, shoot, weaponry, projectiles));
		weaponShooting.addWeaponShooting(new MinigunShooting(bot, info, shoot, weaponry));
		weaponShooting.addWeaponShooting(new FlakCannonShooting(bot, info, shoot, weaponry));
		weaponShooting.addWeaponShooting(new ShieldGunShooting(bot, info, shoot, weaponry, projectiles, senses));
		weaponShooting.addWeaponShooting(new BioRifleShooting(bot, info, shoot, weaponry));
		weaponShooting.addWeaponShooting(new AssaultRifleShooting(bot, info, shoot, weaponry));
		weaponShooting.addWeaponShooting(new RocketLauncherShooting(bot, info, shoot, weaponry));
		weaponShooting.addWeaponShooting(new LigthningGunShooting(bot, info, shoot, weaponry));
		weaponShooting.addWeaponShooting(new SniperRifleShooting(bot, info, shoot, weaponry));
	}

	/**
	 * Path-planner ({@link IPathPlanner} using {@link NavPoint}s), you may use
	 * it to find paths inside the environment wihtout waiting for round-trip of
	 * {@link GetPath} command and {@link PathList}s response from UT2004. It is
	 * much faster than {@link UT2004BotModuleController#pathPlanner} but you
	 * need to pass {@link NavPoint} instances to planner instead of
	 * {@link ILocated} ... to find the nearest {@link NavPoint} instance,
	 * {@link DistanceUtils} is a handy, check especially
	 * {@link DistanceUtils#getNearest(java.util.Collection, ILocated)}.
	 */
	protected SharedFloydWarshallMap sfwMap;

	/**
	 * Initializes path-finding modules:
	 * {@link UT2004BotModuleControllerNew#pathPlanner},
	 * {@link UT2004BotModuleController#fwMap} and
	 * {@link UT2004BotModuleControllerNew#pathExecutor}. If you need different
	 * path planner / path executor - override this method and initialize your
	 * own modules.
	 * 
	 * @param bot
	 */
	@Override
	protected void initializePathFinding(UT2004Bot bot) {
		pathPlanner = new UT2004AStarPathPlanner(bot);
		sfwMap = new SharedFloydWarshallMap(bot);
		pathExecutor = new UT2004PathExecutor<ILocated>(bot, info, move,
				new LoqueNavigator<ILocated>(bot, info, move, bot.getLog()), bot.getLog());

		// add stuck detectors that watch over the path-following, if it
		// (heuristicly) finds out that the bot has stuck somewhere,
		// it reports an appropriate path event and the path executor will stop
		// following the path which in turn allows
		// us to issue another follow-path command in the right time
		pathExecutor.addStuckDetector(new UT2004TimeStuckDetector(bot, 3000, 100000));
		pathExecutor.addStuckDetector(new UT2004PositionStuckDetector(bot));
		pathExecutor.addStuckDetector(new UT2004DistanceStuckDetector(bot));

		getBackToNavGraph = new UT2004GetBackToNavGraph(bot, info, move);
		runStraight = new UT2004RunStraight(bot, info, move);
		navigation = new UT2004Navigation(bot, pathExecutor, sfwMap, getBackToNavGraph, runStraight);
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
		log.setLevel(this.parameters.getLogLevel());

		return init;

	}

	/**
	 * Finish controller initialisation. Connects the weaponshooting to the
	 * navigation.
	 */
	@Override
	public void finishControllerInitialization() {
		// Connects focus providers. Ordered focus provider will first check the
		// focus provided by weapon shooting. If none is provided it will go to
		// the
		// look location.
		focus.add(weaponShooting.getFocus());
		focus.add(lookFocus);
		navigation.setFocus(focus);

		// TODO: This is executed for every bot while we use a shared map.
		if (navBuilder.isUsed()) {
			log.info(
					"Navigation graph has been altered by 'navBuilder', triggering recomputation of Floyd-Warshall path matrix...");
			Level oldLevel = sfwMap.getLog().getLevel();
			sfwMap.getLog().setLevel(Level.FINER);
			sfwMap.refreshPathMatrix();
			sfwMap.getLog().setLevel(oldLevel);
		}
	}

	/**
	 * Called before the evaluation of the first logic. Use this to set up
	 * elements that need information about the world.
	 */

	@Override
	public void beforeFirstLogic() {
		targetSelector.add(new NearestEnemy().setContext(this));
		lookSelector.add(new NearestEnemy().setContext(this));

		weaponPrefs.addGeneralPref(UT2004ItemType.SHOCK_RIFLE, false);
		weaponPrefs.addGeneralPref(UT2004ItemType.ROCKET_LAUNCHER, true);
		weaponPrefs.addGeneralPref(UT2004ItemType.FLAK_CANNON, true);
		weaponPrefs.addGeneralPref(UT2004ItemType.SNIPER_RIFLE, true);
		weaponPrefs.addGeneralPref(UT2004ItemType.LIGHTNING_GUN, true);
		weaponPrefs.addGeneralPref(UT2004ItemType.MINIGUN, true);
		weaponPrefs.addGeneralPref(UT2004ItemType.LINK_GUN, true);
		weaponPrefs.addGeneralPref(UT2004ItemType.BIO_RIFLE, false);
		weaponPrefs.addGeneralPref(UT2004ItemType.ASSAULT_RIFLE, true);
		weaponPrefs.addGeneralPref(UT2004ItemType.ASSAULT_RIFLE, false);
		weaponPrefs.addGeneralPref(UT2004ItemType.SHIELD_GUN, false);
		weaponPrefs.addGeneralPref(UT2004ItemType.SHIELD_GUN, true);

	}

	/**
	 * The bot will evaluate its the logic every 100ms. In each evaluation it
	 * will do the following:
	 * 
	 * <ol>
	 * <li>Execute all outstanding action.</li>
	 * <li>Determine a target to shoot and look at. The target is determined by
	 * the first {@link Selector} in the list set by
	 * {@link UT2004BotBehavior#target(SelectorList)} that returns a valid
	 * target.</li>
	 * <li>Determine a target to look at. The target is determined by the first
	 * {@link Selector} in the list set by
	 * {@link UT2004BotBehavior#look(SelectorList)} that returns a valid target.
	 * </li>
	 * <li>Look at something</li>
	 * <ol>
	 * <li>If we are navigating, navigation will ensure that we look at either
	 * our target or at the path ahead.</li>
	 * <li>If we have a target to look at, we turn to that target.</li>
	 * <li>If we don't have a target to look at, we turn around looking for a
	 * target.</li>
	 * </ol>
	 * <li>Prepare a batch of percepts for the Environment.</li>
	 * 
	 * </ol>
	 * 
	 */
	@Override
	public void logic() {
		super.logic();

		// 0. Execute all outstanding actions.
		for (Action action : actions.drain()) {
			action.execute();
			actionCount++;
		}

		// 1. The target that we may shoot at...
		// ...is determined by the first filter to match.
		ILocated shootSelected = null;
		for (Selector<ILocated> selector : targetSelector) {
			shootSelected = selector.select(players.getVisiblePlayers().values());
			if (shootSelected != null) {
				break;
			}
		}
		weaponShooting.shoot(shootSelected);

		// 2. We determine a target to look at.
		// This will be look at if our shoot target is not visible.
		ILocated lookSelected = null;
		for (Selector<ILocated> selector : lookSelector) {
			lookSelected = selector.select(players.getVisiblePlayers().values());
			if (lookSelected != null) {
				break;
			}
		}
		lookFocus.setFocus(lookSelected);

		// 3. If we are navigating now, we are done.
		if (!navigation.isNavigating()) {
			// 4a. If we have a target but are not moving, turn to the target.
			if (focus.getLocation() != null) {
				move.turnTo(focus.getLocation());
			}
			// 4b. If we see no one, we spin around.
			else {
				move.turnHorizontal(30);
			}
		}

		logicIteration++;

		// 5. Prepare new batch of percepts
		try {
			percepts.updatePercepts();
		} catch (PerceiveException e) {
			throw new PogamutException("Could not update percepts", e);
		}

		// 6. notify that percepts are ready (if this is first round).
		if (logicIteration == 1) {
			listener.notifyPerceptsReady();
		}

	}

	/**
	 * Queues up the action to be executed on the first evaluation of the logic
	 * cycle. When the queue is full, this action will block until the queue is
	 * free.
	 * 
	 * @param action
	 * @throws InterruptedException
	 */
	public void addAction(Action action) throws InterruptedException {
		actions.put(action);
	}

	/**
	 * Returns a previously prepared batch of percepts.
	 * 
	 * @return a previously prepared batch of percepts.
	 */
	@Override
	public Map<Method, Object> getAllPercepts() {
		return percepts.getAllPercepts();
	}

	/**
	 * When called the bot will make a best effort attempt to navigate to
	 * requested destination. The destination either be a {@link Location} or
	 * the UnrealId of an {@link ILocated} such as {@link NavPoint} or
	 * {@link Player}. When provided with a player the bot will actively pursue
	 * the player provided the player remains visible.
	 * 
	 * The destination is considered reached when the bot is within 50 UT units
	 * from the destination. Or 100 UT units when trying to navigate to a
	 * player.
	 * 
	 * The navigation can fail when there is no path to the destination, when
	 * the bot gets stuck. A bot can be considered stuck by three heuristics.
	 * Either the {@link UT2004DistanceStuckDetector} when the bot has not be
	 * closing in on its target enough, {@link UT2004PositionStuckDetector} when
	 * it has not been moving at all, or the {@link UT2004TimeStuckDetector}
	 * when it has not moved enough over time.
	 * 
	 * When the bot dies or respawns the navigation will reset to waiting.
	 * 
	 * @param destination
	 *            where the bot should go.
	 */
	@AsAction(name = "navigate")
	public void navigate(final UnrealIdOrLocation destination) throws InterruptedException {
		log.fine(String.format("called navigate to %s", destination));

		addAction(new Navigate() {

			@Override
			public void execute() {

				ILocated object = destination.toILocated(world, info);

				if (object == null) {
					log.warning(String.format(
							"failed to navigate to %s. The object associated with this Id was not located in the world. Halting.",
							destination));
					navigation.stopNavigation();
					return;
				}

				log.fine(String.format("executed navigate to %s", destination));
				navigation.navigate(object);
			}
		});

	}

	// /**
	// * When called the bot will navigate to the new destination after reaching
	// * the old one. When the bot is not navigating the navigate action will be
	// * executed instead.
	// *
	// * Please note that you using continue when following a player has no
	// * effect.
	// *
	// * The navigation state will be updated to reflect the new destination.
	// *
	// * NOTE: This action is experimental and does not work properly let.
	// * Currently if there is no path to the continue location the
	// * {@link UT2004Navigation} will ignore this.
	// *
	// * @param destination
	// * where the bot should go.
	// */
	// @AsAction(name = "continue")
	// public void continueAction(final UnrealIdOrLocation destination) throws
	// InterruptedException {
	// log.fine(String.format("called continue to %s", destination));
	//
	// addAction(new Continue() {
	//
	// @Override
	// public void execute() {
	//
	// ILocated object = destination.toILocated(world, info);
	// if (object == null) {
	// log.warning(String.format("failed to continue to %s. The object
	// associated with this Id was not located in the world.",
	// object));
	// return;
	// }
	//
	// if (!navigation.isNavigating()) {
	// navigation.navigate((ILocated) object);
	// } else if (navigation.isNavigatingToPlayer()) {
	// log.warning(String.format("failed to continue to %s. Navigation is
	// navigating to a player",
	// object));
	// } else {
	// navigation.setContinueTo((ILocated) object);
	// }
	//
	// log.info(String.format("executed continue to %s", object));
	//
	// }
	// });
	// }

	/**
	 * When called the bot will stop.
	 * 
	 * Navigation will reset to waiting.
	 */
	@AsAction(name = "stop")
	public void stop() throws InterruptedException {
		log.fine("called stop");

		addAction(new Stop() {
			@Override
			public void execute() {
				log.info("executed stop");
				navigation.stopNavigation();
			}
		});
	}

	/**
	 * When called the bot will respawn in at a random spawn point of its team.
	 * 
	 * When the bot respawns he will have 100 health, 0 adrenaline, 0 armor, a
	 * shield gun and an assault rifle with 100 bullets and 4 grenades. The
	 * navigating will be reset to waiting.
	 * 
	 */
	@AsAction(name = "respawn")
	public void respawn() throws InterruptedException {
		log.fine("called respawn");
		addAction(new Respawn() {
			@Override
			public void execute() {
				log.info("executed respawn");
				bot.respawn();
			}
		});
	}

	/**
	 * <p>
	 * When called the bot will use the given combo.
	 * </p>
	 * 
	 * <p>
	 * Syntax: combo(Combo)<br>
	 * <ul>
	 * <li>Combo: Either booster, berserk, invisibility or speed</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * A combo can only be activated when the bot has 100 or more adrenaline.
	 * When active the combo will give the bot a small powerup and consume the
	 * bots adrenaline until none remains. When the adrenaline runs out the
	 * combo will stop.
	 * </p>
	 * <p>
	 * For more information about the effects of a combo see:
	 * http://liandri.beyondunreal.com/Adrenaline
	 * </p>
	 * 
	 * 
	 * 
	 * @param combo
	 *            to be activated.
	 */
	@AsAction(name = "combo")
	public void combo(final Combo combo) throws InterruptedException {
		log.fine("called combo %s", combo);

		addAction(new nl.tudelft.goal.ut2004.actions.Combo() {
			@Override
			public void execute() {

				if (info.isAdrenalineSufficient()) {
					log.info("executed combo %s", combo);
					body.getAction().startCombo(combo.toString());
				} else {
					log.warning("combo %s failed, insufficient adrenaline", combo);
				}
			}
		});
	}

	/**
	 * <p>
	 * Drops the weapon the bot is currently holding.
	 * </p>
	 * <p>
	 * Syntax: dropWeapon
	 * </p>
	 * 
	 * <p>
	 * The weapon will be dropped a few UT hundred units in the direction the
	 * bot is looking. When a weapon is dropped it can be picked up again,
	 * either by this bot or other other bots.
	 * </p>
	 * <p>
	 * Note: The translocator and the shield gun can not be dropped.
	 * <p>
	 */
	@AsAction(name = "dropWeapon")
	public void dropWeapon() throws InterruptedException {
		log.fine("called drop");

		addAction(new DropWeapon() {
			@Override
			public void execute() {
				Weapon weapon = weaponry.getCurrentWeapon();

				body.getAction().throwWeapon();

				if (weapon == null) {
					log.warning(String.format("Could not drop weapon. Not holding a weapon."));
				} else if (weapon.getType() == UT2004ItemType.SHIELD_GUN
						|| weapon.getType() == UT2004ItemType.TRANSLOCATOR) {
					log.warning(String.format("Could not drop weapon %s", weapon));
				} else {
					log.info("executed drop %s", weapon);
				}
			}
		});
	}

	/**
	 * <p>
	 * The bot computes a path from a to b.</a>
	 * 
	 * <p>
	 * Syntax: path(From,To)<br>
	 * <ul>
	 * <li>From: UnrealId of a nav point.</li>
	 * <li>To: UnrealId of a nav point.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * The action results in a path percept containing the path from a to b.
	 * </p>
	 * 
	 * <p>
	 * Syntax: path(StartId, EndId, Length,[NavPointId])<br>
	 * <ul>
	 * <li>StartId: start of the path as UnrealId.</li>
	 * <li>EndId: end of the path as UnrealId.</li>
	 * <li>Length: Length of the path in UT units.</li>
	 * <li>NavPointId: A list of UnrealIds in the path.</li>
	 * </ul>
	 * </p>
	 * 
	 * 
	 * @param from
	 *            the navpoint from which the path starts.
	 * @param to
	 *            the navpoint where the path should go to
	 * @return the path between from and to, including the distance of the path.
	 */
	@AsAction(name = "path")
	public Percept path(UnrealIdOrLocation from, UnrealIdOrLocation to) {

		ILocated toObject = to.toILocated(world, info);
		ILocated fromObject = from.toILocated(world, info);

		if (fromObject == null) {
			throw new PogamutException(String.format(
					"Failed to compute path from %s to %s. The start was not located in the world.", from, to), this);
		}
		if (toObject == null) {
			throw new PogamutException(
					String.format("Failed to compute path from %s to %s. The destination was not located in the world.",
							from, to),
					this);
		}

		// Not put into action queue. Doesn't require dynamic info from world.
		log.info(String.format("executed path from  %s to %s", from, to));

		// Extract locations. This action is off the pogamut thread so we want
		// to use the immutables for consistent results.
		// We are using volitile information here but that is okay.
		// Location objects, Navpoints, Items don't move. Players can but only
		// for a limited distance. Not enough to invalidate the path or cause
		// strange results.
		Location fromLocation = fromObject.getLocation();
		Location toLocation = toObject.getLocation();

		NavPoint fromNav = DistanceUtils.getNearest(world.getAll(NavPoint.class).values(), fromLocation);
		NavPoint toNav = DistanceUtils.getNearest(world.getAll(NavPoint.class).values(), toLocation);

		double distance = sfwMap.getDistance(fromNav, toNav);
		List<NavPoint> navPoints = sfwMap.getPath(fromNav, toNav);
		// can be null : locations both exist but no connecting path exists

		List<UnrealId> unrealIds = new ArrayList<UnrealId>();
		if (navPoints != null) {
			for (NavPoint n : navPoints) {
				unrealIds.add(n.getId());
			}
		} else {
			distance = -1;
		}
		return new Percept(fromNav.getId(), toNav.getId(), distance, unrealIds);
	}

	/**
	 * 
	 * <p>
	 * Tells the bot how to prioritize who it shoots.
	 * </p>
	 * <p>
	 * Syntax: shoot([Selector])<br>
	 * Syntax: shoot(Selector)<br>
	 * <ul>
	 * <li>[Selector]: A list of selectors. A selector either selects a target
	 * from the list of visible players, provides a fixed location or selects
	 * nothing if no suitable target was available for selection. The bot
	 * evaluates each selector in order. The target provided by the first first
	 * selector to select a target will be shot at. Should no selector provide a
	 * valid target will be shot at. If no targets are selected the bot will
	 * stop shooting.</li>
	 * <li>Selector: A selector will select a target from the visible players.
	 * Can be, nearestEnemy, nearestFriendly, nearestFriendlyWithLinkGun,
	 * enemyFlagCarrier, friendlyFlagCarrier, a PlayerID or a location(X,Y,Z).
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Note:
	 * <ol>
	 * <li>To stop shooting, use shoot([]) or stopShooting.</li>
	 * <li>By default the bot will shoot the nearest visible enemy.</li>
	 * </ol>
	 * </p>
	 */
	@AsAction(name = "shoot")
	public void shoot(final SelectorList targets) throws InterruptedException {
		log.fine(String.format("called shoot %s", targets));

		addAction(new Shoot() {

			@Override
			public void execute() {
				log.info(String.format("executed shoot %s ", targetSelector));

				targetSelector = targets.setContext(UT2004BotBehavior.this);
			}
		});

	}

	/**
	 * *
	 * <p>
	 * Tells the bot to stop shooting.
	 * </p>
	 * <p>
	 * Syntax: stopShoot
	 * </p>
	 * <p>
	 * Note: Executes shoot([])
	 * </p>
	 */

	@AsAction(name = "stopShooting")
	public void stopShooting() throws InterruptedException {
		shoot(new SelectorList());
	}

	/**
	 * <p>
	 * Tells the bot which weapon it should prefer. The bot will select the
	 * first weapon from the list it can use. A weapon can be used when the bot
	 * has the and the ammo for it.
	 * </p>
	 * 
	 * <p>
	 * Syntax: prefer([weapon(WeaponId, FireMode)])<br>
	 * Syntax: prefer(weapon(WeaponId, FireMode))<br>
	 * <ul>
	 * <li>WeaponId: The id of the weapon.</li>
	 * <li>FireMode: How the bot should use the weapon, either primary or
	 * secondary.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Note: By Default the bot prefers the weapons in this order:
	 * <ol>
	 * <li>weapon(shock_rifle, secondary)</li>
	 * <li>weapon(rocket_launcher, primary)</li>
	 * <li>weapon(flack_cannon, primary)</li>
	 * <li>weapon(sniper_rifle, primary)</li>
	 * <li>weapon(lightning_gun, primary)</li>
	 * <li>weapon(mini_gun, primary)</li>
	 * <li>weapon(link_gun, primary)</li>
	 * <li>weapon(bio_rifle, secondary)</li>
	 * <li>weapon(assault_rifle, primary)</li>
	 * <li>weapon(assault_rfile, secondary)</li>
	 * <li>weapon(shield_gun, secondary)</li>
	 * <li>weapon(shield_gun, primary)</li>
	 * </ol>
	 * 
	 * </p>
	 */
	@AsAction(name = "prefer")
	public void prefer(final WeaponPrefList weaponList) throws InterruptedException {
		log.fine(String.format("called prefer %s", weaponList));

		addAction(new Prefer() {

			@Override
			public void execute() {
				weaponPrefs.clearAllPrefs();

				for (WeaponPref pref : weaponList) {
					weaponPrefs.addGeneralPref(pref.getWeapon(), pref.isPrimary());
				}

				log.info(String.format("executed prefer %s", weaponList));
			}
		});
	}

	/**
	 * 
	 * <p>
	 * Tells the bot how to prioritize what it looks at.
	 * </p>
	 * <p>
	 * Syntax: look([Selector])<br>
	 * Syntax: look(Selector)<br>
	 * <ul>
	 * <li>[Selector]: A list of selectors. A selector either selects a target
	 * from the list of visible players, provides a fixed location or selects
	 * nothing if no suitable target was available for selection. The bot
	 * evaluates each selector in order. The target provided by the first first
	 * selector to select a target will be shot at. Should no selector provide a
	 * valid target will be looked at. If no targets are selected the bot will
	 * slowly turn around.</li>
	 * <li>Selector: A selector will select a target from the visible players.
	 * Can be, nearestEnemy, nearestFriendly, nearestFriendlyWithLinkGun,
	 * enemyFlagCarrier, friendlyFlagCarrier, a PlayerID or a location(X,Y,Z).
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Note:
	 * <ol>
	 * <li>To to start looking around, use look([]).</li>
	 * <li>By default the bot will look at the nearest visible enemy.</li>
	 * </ol>
	 * </p>
	 */
	@AsAction(name = "look")
	public void look(final SelectorList targets) throws InterruptedException {
		log.fine(String.format("called look %s", targets));

		addAction(new Look() {

			@Override
			public void execute() {
				log.info(String.format("executed look %s", targets));

				lookSelector = targets.setContext(UT2004BotBehavior.this);
			}
		});
	}

	/**
	 * 
	 * <p>
	 * Does nothing.
	 * </p>
	 * <p>
	 * Syntax: skip
	 * </p>
	 * 
	 * TODO: Check DOC
	 */
	@AsAction(name = "skip")
	public void skip() {
		// Does nothing.
	}

	/**
	 * TODO: Doc.
	 * 
	 * @param scope
	 * @param message
	 * @throws InterruptedException
	 */
	@AsAction(name = "chat")
	public void chat(final Scope scope, final String message) throws InterruptedException {
		log.fine(String.format("called chat: %s", message));

		addAction(new Chat() {
			@Override
			public void execute() {
				log.info(String.format("executed chat: %s", message));
				switch (scope) {
				case GLOBAL:
					body.getCommunication().sendGlobalTextMessage(message);
					break;
				case TEAM:
					body.getCommunication().sendTeamTextMessage(message);
					break;
				// No need for default when switching on an enum. If you're
				// worried about missing
				// cases in which the enum is extended you can use the Exclipse
				// Settings. Java ->
				// Compier -> Errors/Warnings -> Enum type constant not covered
				// on 'switch'.
				// default:
				// log.severe("unknown chat scope");
				// break;
				}

			}
		});
	}

	/**
	 * <p>
	 * Information about iteration of the bots logic.
	 * </p>
	 * <p>
	 * Type: On Change
	 * </p>
	 * 
	 * <p>
	 * Syntax: logic(Iteration)
	 * <ul>
	 * <li>Iteration: The number of iterations of the bots logic so far.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>While the bot is capable of executing multiple actions in single
	 * logic iteration, it does not always make sense. Use this iteration to
	 * check if the bot is clear again.</li>
	 * </ol>
	 * </p>
	 */
	@AsPercept(name = "logic", filter = Type.ON_CHANGE)
	@Deprecated
	public Percept logicIteration() {
		return new Percept(logicIteration);
	}

	@AsPercept(name = "actionCount", filter = Type.ON_CHANGE)
	@Deprecated
	public Percept actionCount() {
		return new Percept(actionCount);
	}

	/**
	 * <p>
	 * Information about the bot's identity and team.
	 * </p>
	 * <p>
	 * Type: On Change
	 * </p>
	 * 
	 * <p>
	 * Syntax: self(UnrealId, NickName, Team)
	 * <ul>
	 * <li>UnrealId: Unique identifier assigned by Unreal.</li>
	 * <li>NickName: Name as it appears in the game.</li>
	 * <li>Team: Either red, blue, or none.</li>
	 * </ul>
	 * </p>
	 * 
	 */
	@AsPercept(name = "self", filter = Type.ON_CHANGE)
	public Percept self() {
		return new Percept(info.getId(), info.getName(), Team.valueOf(info.getTeam()));
	}

	/**
	 * <p>
	 * Information about the bot's position, rotation and velocity.
	 * </p>
	 * <p>
	 * Type: On Change
	 * </p>
	 * <p>
	 * Syntax: orientation(location(X,Y,Z), rotation(Pitch,Yaw,Roll),
	 * velocity(Vx, Vy,Vz))
	 * <ul>
	 * <li>Location: the position in UT units.</li>
	 * <li>Rotation: the bots rotation in degrees.</li>
	 * <li>Velocity: the velocity in UT units per second.</li>
	 * </ul>
	 * </p>
	 * 
	 */
	@AsPercept(name = "orientation", filter = Type.ON_CHANGE)
	public Percept orientation() {
		return new Percept(info.getLocation(), info.getRotation(), info.getVelocity());
	}

	/**
	 * <p>
	 * Information about the bot's current physical state.
	 * </p>
	 * <p>
	 * Type: On Change
	 * </p>
	 * 
	 * <p>
	 * Syntax status(Health, Armour, Adrenaline,ActiveCombo)
	 * <ul>
	 * <li>Health: A number between 0 and 199, indicating the health.</li>
	 * <li>Armour: A number between 0 and 150, indicating the armor.</li>
	 * <li>Adrenaline: An number between 0 and 100, indicating the adrenaline.
	 * </li>
	 * <li>ActiveCombo: The combo that is currently active, or none when none is
	 * active.</li>
	 * </ul>
	 * </p>
	 * 
	 */
	@AsPercept(name = "status", filter = Type.ON_CHANGE)
	public Percept status() {
		return new Percept(info.getHealth(), info.getArmor(), info.getAdrenaline(),
				Combo.parseCombo(info.getSelf().getCombo()));
	}

	/**
	 * <p>
	 * Information about the number of kills, deaths, and suicides this bot
	 * accumulated.
	 * </p>
	 * <p>
	 * Type: On Change
	 * </p>
	 * 
	 * <p>
	 * Syntax: score(Kills, Deaths, Suicides)
	 * <ul>
	 * <li>Kills: Number of people the bot fragged during this game.</li>
	 * <li>Deaths: Number of times the bot died during this game.</li>
	 * <li>Suicides: Number of times the bot got himself killed.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * <em>Note</em>: Using the respawn action and being fragged by an opponent
	 * both count as a death. Being killed by the your own weapon counts as a
	 * suicide.
	 * </p>
	 * 
	 */
	@AsPercept(name = "score", filter = Type.ON_CHANGE)
	public Percept score() {
		return new Percept(info.getKills(), info.getDeaths(), info.getSuicides());
	}

	/**
	 * <p>
	 * Information about weapon the bot is currently holding.
	 * </p>
	 * <p>
	 * Type: On Change
	 * </p>
	 * 
	 * <p>
	 * Syntax: currentWeapon(WeaponType,FireMode)
	 * <ul>
	 * <li>WeaponType: Name of the weapon.</li>
	 * <li>FireMode: How the weapon is shooting. Either primary, secondary or
	 * none.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * TODO: List available weapons.
	 * 
	 */

	@AsPercept(name = "currentWeapon", filter = Type.ON_CHANGE)
	public Percept currentWeapon() {
		final Weapon weapon = weaponry.getCurrentWeapon();

		if (weapon == null) {
			return new Percept(new None(), FireMode.NONE);
		}

		return new Percept(weapon.getType(), FireMode.valueOf(info.isPrimaryShooting(), info.isSecondaryShooting()));
	}

	/**
	 * <p>
	 * Information about weapons the bot has in its inventory.
	 * </p>
	 * <p>
	 * Type: On change
	 * </p>
	 * <p>
	 * Syntax: weapon(WeaponType, PriAmmo, SecAmmo)
	 * <ul>
	 * <li>WeaponType: Name of the weapon.</li>
	 * <li>PriAmmo: A number between 0 and the maximum for the weapon,
	 * indicating the available ammo for the primary fire mode.</li>
	 * <li>SecAmmo: A number between 0 and the maximum for the weapon,
	 * indicating the available ammo for the secondary fire mode.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * <em>Note</em>: The Shield Gun has infinite primary ammo. Its secondary
	 * ammo recharges when not used.
	 * </p>
	 * 
	 * 
	 * TODO: List available weapons.
	 * 
	 */
	@AsPercept(name = "weapon", multiplePercepts = true, filter = Type.ON_CHANGE_NEG)
	public Collection<Percept> weapon() {
		Collection<Weapon> weapons = weaponry.getWeapons().values();
		Collection<Percept> percepts = new ArrayList<Percept>(weapons.size());

		for (Weapon w : weapons) {
			if (w.getType() == UT2004ItemType.SHIELD_GUN) {
				// Pogamut reports the secondary ammo twice. It reports this
				// because UT reports this. Not trivial to fix so we do it here.
				// 1 will stand in for infinity
				percepts.add(new Percept(w.getType(), 1, w.getSecondaryAmmo()));
			} else {
				percepts.add(new Percept(w.getType(), w.getPrimaryAmmo(), w.getSecondaryAmmo()));
			}
		}

		return percepts;
	}

	/**
	 * List of all fragged percepts.
	 */
	private List<Percept> fragged = new LinkedList<Percept>();

	/**
	 * Adds a new fragged percept to the list.
	 * 
	 * @param time
	 * @param killer
	 * @param victem
	 * @param weaponName
	 */
	private void fraggedEvent(final long time, final UnrealId killer, final UnrealId victem, final String damageType) {
		fragged.add(new Percept(time, killer, victem, WeaponDamage.weaponForDamage(damageType)));
	}

	/**
	 * Event listener for deaths of this bot.
	 * 
	 * @param msg
	 */
	@EventListener(eventClass = BotKilled.class)
	public void msgBotKilled(BotKilled msg) {
		fraggedEvent(msg.getSimTime(), msg.getKiller(), info.getId(), msg.getDamageType());
	}

	/**
	 * Event listener for deaths of other bots & players.
	 * 
	 * @param msg
	 */

	@EventListener(eventClass = PlayerKilled.class)
	public void msgPlayerKilled(PlayerKilled msg) {
		fraggedEvent(msg.getSimTime(), msg.getKiller(), msg.getId(), msg.getDamageType());
	}

	/**
	 * <p>
	 * This percept is provided when one bot is violently fragmented by another.
	 * </p>
	 * <p>
	 * Type: Always
	 * </p>
	 * 
	 * <p>
	 * Syntax: fragged(Time,KillerID,VictemID,Weapon)
	 * </p>
	 * 
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>When the killer and victim id are equal, the bot committed suicide.
	 * </li>
	 * <li>When the killer is none, the bot respawned.</li>
	 * </ol>
	 * </p>
	 * 
	 */
	@AsPercept(name = "fragged", multiplePercepts = true, filter = Type.ALWAYS, event = true)
	public List<Percept> fragged() {
		ArrayList<Percept> percepts = new ArrayList<Percept>(fragged);
		fragged.clear();
		return percepts;
	}

	/**
	 * <p>
	 * Information about the state of the navigation. The available states are:
	 * </p>
	 * 
	 * <ul>
	 * <li>navigating: The bot is traveling to its destination.</li>
	 * <li>stuck: The botcould not reach its destination.</li>
	 * <li>noPath: The bot could not find a path to its destination.</li>
	 * <li>reached: The bot has arrived at its destination.</li>
	 * <li>waiting: The bot is waiting for actions (initial state).</li>
	 * </ul>
	 * <p>
	 * Type: On Change
	 * </p>
	 * 
	 * <p>
	 * Syntax: navigation(State,Destination)
	 * <ul>
	 * <li>State: State of the navigation. Either navigating, stuck, noPath,
	 * reached or waiting.</li>
	 * </ul>
	 * </p>
	 * 
	 */
	@AsPercept(name = "navigation", filter = Type.ON_CHANGE)
	public Percept navigation() {
		ILocated currentTarget = navigation.getCurrentTarget();

		// We are going nowhere.
		if (currentTarget == null) {
			return new Percept(navigation.getState().getFlag(), new None());
		}

		// We are going some place that has an unrealid.
		if (currentTarget instanceof IWorldObject) {
			IWorldObject targetObject = (IWorldObject) navigation.getCurrentTarget();
			return new Percept(navigation.getState().getFlag(), targetObject.getId());
		}

		// We are going to a location(x,y,z)
		return new Percept(navigation.getState().getFlag(), currentTarget.getLocation());
	}

	/**
	 * <p>
	 * Information about point in the map. Together these form a directed graph
	 * that spans the entire map.
	 * </p>
	 * <p>
	 * Type: Once
	 * </p>
	 * 
	 * <p>
	 * Syntax: navPoint(UnrealID, location(X,Y,Z), [NeigsUnrealID])
	 * <ol>
	 * <li>UnrealID: The unique id of this navpoint.</li>
	 * <li>Location: The location of this navpoint in the map.</li>
	 * <li>[NeigsUnrealID]: A list of Id's for the neighbouring navpoints that
	 * are reachable from this navpoint.</li>
	 * </ol>
	 * </p>
	 * *
	 * 
	 */
	@AsPercept(name = "navPoint", multiplePercepts = true, filter = Type.ONCE)
	public Collection<Percept> navPoint() {
		Collection<NavPoint> navPoints = world.getAll(NavPoint.class).values();
		List<Percept> percepts = new ArrayList<Percept>(navPoints.size());

		for (NavPoint p : navPoints) {
			percepts.add(new Percept(p.getId(), p.getLocation(), p.getOutgoingEdges().keySet()));
		}

		return percepts;
	}

	/**
	 * <p>
	 * Information indicating at which navpoint weapons, ammo, and health can be
	 * found.
	 * </p>
	 * <p>
	 * Type: Once
	 * </p>
	 * 
	 * <p>
	 * Syntax: pickup(UnrealID, Label, ItempType)
	 * <ul>
	 * <li>UnrealID: The UnrealId of the nav point this pickup spot is placed
	 * on.</li>
	 * <li>Label: The category of the pickup.</li>
	 * <li>UT2004Group: The type of the of the item located on the pickup.</li>
	 * </ul>
	 * </p>
	 * 
	 * 
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>Depending on the game setting "weapon stay", there may not always be
	 * a weapon present on a pick up point.</li>
	 * <li>If "weapon stay" is enabled, a weapon can only be picked up if one of
	 * the same type is not present in the bots inventory yet.</li>
	 * <li>TODO: A full overview of which category Label belongs to which item
	 * type.</li>
	 * 
	 * </ol>
	 * </p>
	 */
	@AsPercept(name = "pickup", multiplePercepts = true, filter = Type.ONCE)
	public Collection<Percept> pickup() {
		Collection<Item> pickups = items.getKnownPickups().values();
		Collection<Percept> percepts = new ArrayList<Percept>(pickups.size());

		for (Item item : pickups) {
			// Ignore dropped items && items placed manually.
			// Pogamut does not consider the manually placed items to be
			// dropped. See ticket #2487.
			if (!item.isDropped() && item.getNavPoint() != null) {
				percepts.add(new Percept(item.getNavPointId(), item.getType().getCategory(), item.getType()));
			}
		}
		return percepts;
	}

	/**
	 * <p>
	 * Information indicating which pickup is visible.
	 * </p>
	 * <p>
	 * Type: On change with negation
	 * </p>
	 * 
	 * <p>
	 * Syntax: pickup(UnrealID)
	 * <ul>
	 * <li>UnrealID: The UnrealId of the nav point this pickup spot is placed
	 * on.</li>
	 * </ul>
	 * </p>
	 */
	@AsPercept(name = "pickup", multiplePercepts = true, filter = Type.ON_CHANGE_NEG)
	public Collection<Percept> visiblePickup() {
		Collection<NavPoint> navPoints = world.getAll(NavPoint.class).values();
		List<Percept> percepts = new ArrayList<Percept>(navPoints.size());

		for (NavPoint p : navPoints) {
			if (p.isVisible() && p.isItemSpawned()) {
				percepts.add(new Percept(p.getId()));
			}
		}

		return percepts;
	}

	/**
	 * <p>
	 * Information about the location of the base. The opposing team will try to
	 * steal the flag from this location. Your team must deliver any capture
	 * flags to the base.
	 * </p>
	 * 
	 * <p>
	 * Type: Once
	 * </p>
	 * 
	 * <p>
	 * Syntax: base(Team, UnrealID)
	 * <ul>
	 * <li>Team: Either red or blue.</li>
	 * <li>UnrealID: The UnrealId of the navpoint this flagbase is placed on.
	 * </li>
	 * </ul>
	 * </p>
	 */
	@AsPercept(name = "base", multiplePercepts = true, filter = Type.ONCE)
	public Collection<Percept> base() {

		Collection<FlagInfo> flags = game.getAllCTFFlagsCollection();
		Collection<Percept> percepts = new ArrayList<Percept>(flags.size());
		Collection<NavPoint> navPoints = world.getAll(NavPoint.class).values();

		for (FlagInfo flag : flags) {
			Team team = Team.valueOf(flag.getTeam());
			NavPoint nav = DistanceUtils.getNearest(navPoints, game.getFlagBase(team.id()));
			percepts.add(new Percept(team, nav.getId()));
		}

		return percepts;

	}

	/**
	 * <p>
	 * Information about the type of game being played, the map and the score
	 * required for winning the game.
	 * </p>
	 * 
	 * 
	 * <p>
	 * Type: On Change
	 * </p>
	 * 
	 * <p>
	 * Syntax: game(Gametype, Map, TeamScoreLimit, RemainingTime)
	 * <ul>
	 * <li>Gametype: The type of game being played.</li>
	 * <li>Map: The name of the map being played on.</li>
	 * <li>TeamScoreLimit: Score needed to win the match. If the score is zero
	 * or not reached by the end of the game, the team that has the highest
	 * score when the time limit is reached wins.</li>
	 * <li>RemainingTime: Time left in the game. If the score for both teams is
	 * a tie, it is possible to go into over time.</li>
	 * </ul>
	 */
	@AsPercept(name = "game", filter = Type.ON_CHANGE)
	public Percept game() {
		return new Percept(game.getGameType(), game.getMapName(), game.getTeamScoreLimit(), game.getRemainingTime());
	}

	/**
	 * <p>
	 * Percept that provides information about the current state of the game.
	 * </p>
	 * 
	 * <p>
	 * Type: On change
	 * </p>
	 * 
	 * <p>
	 * Syntax: teamScore(TeamScore, OpponentTeamScore)
	 * <ul>
	 * <li>TeamScore score of the team this bot is on.</li>
	 * <li>OpponentTeamScore score of the opponent team.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Notes
	 * <ol>
	 * <li>For CTF the score is the number of times the ag has been captured.
	 * </li>
	 * <li>Once either team reaches the goal score from the Game- info percept,
	 * the game is over.</li>
	 * </ol>
	 * </p>
	 * 
	 */
	@AsPercept(name = "teamScore", filter = Type.ON_CHANGE)
	public Percept teamScore() {
		return new Percept(game.getTeamScore(info.getTeam()), game.getTeamScore(1 - info.getTeam()));
	}

	/**
	 * <p>
	 * Description: Percept that provides information about the current state of
	 * the flag.
	 * </p>
	 * 
	 * <p>
	 * Type: On change with negation.
	 * </p>
	 * 
	 * <p>
	 * Syntax: flagState(Team,FlagState)
	 * <ul>
	 * <li>Team: Either blue or red.</li>
	 * <li>FlagState: State of the flag. Either home, held or dropped.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>See also the flag percept</li>
	 * </ol>
	 * <p>
	 * 
	 */
	@AsPercept(name = "flagState", multiplePercepts = true, filter = Type.ON_CHANGE_NEG)
	public Collection<Percept> flagState() {

		Collection<FlagInfo> flags = game.getAllCTFFlagsCollection();
		Collection<Percept> percepts = new ArrayList<Percept>(flags.size());

		for (FlagInfo flag : flags) {
			percepts.add(new Percept(Team.valueOf(flag.getTeam()), FlagState.valueOfIgnoreCase(flag.getState())));
		}

		return percepts;

	}

	/**
	 * <p>
	 * Description: Provides information items the bot sees in the world.
	 * </p>
	 * 
	 * <p>
	 * Type: On change with negation.
	 * </p>
	 * 
	 * <p>
	 * Syntax: item(UnrealID, Label, ItemType, NavPointId)
	 * </p>
	 * <p>
	 * Syntax: item(UnrealID, Label, ItemType, location(X,Y,Z)) when dropped.
	 * </p>
	 * 
	 * <ul>
	 * <li>UnrealID: The UnrealID of this item.</li>
	 * <li>Label: The category of the pick up.</li>
	 * <li>ItemType: The actual item type of the item located on the pickup.
	 * </li>
	 * <li>NavPointId: The UnrealId of the navpoint this item is placed when
	 * spawned.</li>
	 * <li>Location: location in the world when this item is dropped.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>TODO: A full over view of which category Label belongs to which item
	 * type.</li>
	 * </ol>
	 * <p>
	 * 
	 */
	@AsPercept(name = "item", multiplePercepts = true, filter = Type.ON_CHANGE_NEG)
	public Collection<Percept> item() {
		Collection<Item> visibleItems = items.getVisibleItems().values();
		Collection<Percept> percepts = new ArrayList<Percept>(visibleItems.size());

		for (Item item : visibleItems) {
			// Pogamut does not consider the manually placed items to be
			// dropped. See ticket #2487.
			if (item.isDropped() || item.getNavPointId() == null) {
				percepts.add(
						new Percept(item.getId(), item.getType().getCategory(), item.getType(), item.getLocation()));
			} else {
				percepts.add(
						new Percept(item.getId(), item.getType().getCategory(), item.getType(), item.getNavPointId()));
			}
		}

		return percepts;
	}

	/**
	 * <p>
	 * Description: Percept provided when the flag is visible.
	 * </p>
	 * 
	 * <p>
	 * Type: On change with negation.
	 * </p>
	 * 
	 * <p>
	 * Syntax: flag(Team, UnrealId, location(X,Y,Z))
	 * </p>
	 * 
	 * <ul>
	 * <li>Team: Either red or blue.</li>
	 * <li>UnrealId: The UnrealId of the player holding the flag, none when the
	 * flag is not held.</li>
	 * <li>Location: The location of the flag in the world.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>See also the flagStatus percept.</li>
	 * </ol>
	 * <p>
	 * 
	 */
	@AsPercept(name = "flag", multiplePercepts = true, filter = Type.ON_CHANGE_NEG)
	public Collection<Percept> flag() {

		Collection<FlagInfo> flags = game.getAllCTFFlagsCollection();
		Collection<Percept> percepts = new ArrayList<Percept>(flags.size());

		for (FlagInfo flag : flags) {
			if (flag.isVisible())
				percepts.add(new Percept(Team.valueOf(flag.getTeam()), flag.getHolder(), flag.getLocation()));
		}

		return percepts;
	}

	/**
	 * <p>
	 * Percept provided when another bot becomes visible to this bot.
	 * </p>
	 * 
	 * <p>
	 * Type: On change with negation.
	 * </p>
	 * 
	 * <p>
	 * Syntax: bot(UnrealId, Team, location(X,Y,Z), Weapon, FireMode)
	 * </p>
	 * 
	 * <ul>
	 * <li>UnrealId: Unique identifier for this bot assigned by Unreal.</li>
	 * <li>Team: Either red or blue.</li>
	 * <li>location(X,Y,Z): Location of the bot in the world.</li>
	 * <li>Weapon: The weapon the bot is holding. TODO: Any of the following:
	 * </li>
	 * <li>FireMode: Mode of shooting, either primary, secondary or none.</li>
	 * </ul>
	 * </p>
	 * 
	 * 
	 */
	@AsPercept(name = "bot", multiplePercepts = true, filter = Type.ON_CHANGE_NEG)
	public Collection<Percept> bot() {
		Collection<Player> visible = players.getVisiblePlayers().values();
		Collection<Percept> wrapped = new ArrayList<Percept>(visible.size());

		for (Player p : visible) {
			wrapped.add(new Percept(p.getId(), p.getName(), Team.valueOf(p.getTeam()), p.getLocation(),
					UT2004ItemType.getItemType(p.getWeapon()), FireMode.valueOf(p.getFiring())));
		}

		return wrapped;
	}

	/**
	 * <p>
	 * Description: Percept provided when bot grabbed an udamage powerup.
	 * </p>
	 * 
	 * <p>
	 * Type: On change
	 * </p>
	 * 
	 * <p>
	 * Syntax: udamage(Time)
	 * </p>
	 * 
	 * <ul>
	 * <li>Time: Time in seconds (2 decimals)</li>
	 * </ul>
	 * 
	 * @return
	 */
	@AsPercept(name = "udamage", filter = Type.ON_CHANGE)
	public Percept udamage() {
		return new Percept(info.hasUDamage() ? info.getRemainingUDamageTime() : 0);
	}

}
