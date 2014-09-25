package nl.tudelft.goal.ut2004.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.unreal.translators.LocationTranslator;
import nl.tudelft.goal.unreal.translators.NoneTranslator;
import nl.tudelft.goal.unreal.translators.PerceptTranslator;
import nl.tudelft.goal.unreal.translators.RotationTranslator;
import nl.tudelft.goal.unreal.translators.TeamTranslator;
import nl.tudelft.goal.unreal.translators.UnrealIdOrLocationTranslator;
import nl.tudelft.goal.unreal.translators.UnrealIdTranslator;
import nl.tudelft.goal.unreal.translators.VelocityTranslator;
import nl.tudelft.goal.ut2004.translators.CategoryTranslator;
import nl.tudelft.goal.ut2004.translators.ComboTranslator;
import nl.tudelft.goal.ut2004.translators.FireModeTranslator;
import nl.tudelft.goal.ut2004.translators.FlagStateTranslator;
import nl.tudelft.goal.ut2004.translators.GameTypeTranslator;
import nl.tudelft.goal.ut2004.translators.UT2004GroupTranslator;
import nl.tudelft.goal.ut2004.translators.ItemTypeTranslator;
import nl.tudelft.goal.ut2004.translators.NavigationStateTranslator;
import nl.tudelft.goal.ut2004.translators.SelectorListTranslator;
import nl.tudelft.goal.ut2004.translators.SelectorTranslator;
import nl.tudelft.goal.ut2004.translators.WeaponPrefListTranslator;
import nl.tudelft.goal.ut2004.translators.WeaponPrefTranslator;
import cz.cuni.amis.pogamut.base.agent.impl.AgentId;
import cz.cuni.amis.pogamut.base.utils.logging.AgentLogger;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import eis.eis2java.handlers.ActionHandler;
import eis.eis2java.handlers.AllPerceptPerceptHandler;
import eis.eis2java.handlers.DefaultActionHandler;
import eis.eis2java.handlers.PerceptHandler;
import eis.eis2java.translation.Translator;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

import java.util.LinkedList;

/**
 * Test class to make the agent do custom stuff.
 * 
 * @author mpkorstanje
 * 
 */
public class RunAgent {

	public static void main(String[] args) throws EntityException,
			InterruptedException, PerceiveException {

		Translator translator = Translator.getInstance();

		/*
		 * Translators provided by the BaseUnrealEnvironment.
		 * 
		 * Please list these in lexical order.
		 */

		LocationTranslator locationTranslator = new LocationTranslator();
		translator.registerJava2ParameterTranslator(locationTranslator);
		translator.registerParameter2JavaTranslator(locationTranslator);

		/*
		 * To translate from Parameter2Java we are given an UnrealId. However we
		 * can not access the agents memory during translation. To work around
		 * this we store everything we have send to any agent. Hence the same
		 * object has to be used for both directions.
		 */

		RotationTranslator rotationTranslator = new RotationTranslator();
		translator.registerJava2ParameterTranslator(rotationTranslator);
		translator.registerParameter2JavaTranslator(rotationTranslator);

		TeamTranslator teamTranslator = new TeamTranslator();
		translator.registerJava2ParameterTranslator(teamTranslator);
		translator.registerParameter2JavaTranslator(teamTranslator);

		UnrealIdTranslator unrealIdTranslator = new UnrealIdTranslator();
		translator.registerJava2ParameterTranslator(unrealIdTranslator);
		translator.registerParameter2JavaTranslator(unrealIdTranslator);

		VelocityTranslator velocityTranslator = new VelocityTranslator();
		translator.registerJava2ParameterTranslator(velocityTranslator);
		translator.registerParameter2JavaTranslator(velocityTranslator);
		/*
		 * Translators provided by the UT2004 environment.
		 * 
		 * Please list these in lexical order.
		 */

		CategoryTranslator itemTypeTranslator = new CategoryTranslator();
		translator.registerJava2ParameterTranslator(itemTypeTranslator);

		ComboTranslator comboTranslator = new ComboTranslator();
		translator.registerParameter2JavaTranslator(comboTranslator);

		FireModeTranslator fireModeTranslator = new FireModeTranslator();
		translator.registerJava2ParameterTranslator(fireModeTranslator);
		translator.registerParameter2JavaTranslator(fireModeTranslator);

		FlagStateTranslator flagStateTranslator = new FlagStateTranslator();
		translator.registerJava2ParameterTranslator(flagStateTranslator);

		GameTypeTranslator gameTypeTranslator = new GameTypeTranslator();
		translator.registerJava2ParameterTranslator(gameTypeTranslator);

		UT2004GroupTranslator groupTranslator = new UT2004GroupTranslator();
		translator.registerJava2ParameterTranslator(groupTranslator);
		translator.registerParameter2JavaTranslator(groupTranslator);

		ItemTypeTranslator categoryTranslator = new ItemTypeTranslator();
		translator.registerJava2ParameterTranslator(categoryTranslator);

		NavigationStateTranslator navigationStateTranslator = new NavigationStateTranslator();
		translator.registerJava2ParameterTranslator(navigationStateTranslator);

		NoneTranslator noneTranslator = new NoneTranslator();
		translator.registerJava2ParameterTranslator(noneTranslator);

		PerceptTranslator perceptTranslator = new PerceptTranslator();
		translator.registerJava2ParameterTranslator(perceptTranslator);

		SelectorListTranslator selectorListTranslator = new SelectorListTranslator();
		translator.registerParameter2JavaTranslator(selectorListTranslator);

		SelectorTranslator selectorTranslator = new SelectorTranslator();
		translator.registerParameter2JavaTranslator(selectorTranslator);

		UnrealIdOrLocationTranslator unrealIdOrLocationTranslator = new UnrealIdOrLocationTranslator();
		translator
				.registerParameter2JavaTranslator(unrealIdOrLocationTranslator);

		WeaponPrefListTranslator weaponPrefListTranslator = new WeaponPrefListTranslator();
		translator.registerParameter2JavaTranslator(weaponPrefListTranslator);

		WeaponPrefTranslator weaponPrefTranslator = new WeaponPrefTranslator();
		translator.registerParameter2JavaTranslator(weaponPrefTranslator);

		Configuration config = Configuration.getDefaults();

		@SuppressWarnings("rawtypes")
		UT2004BotRunner<UT2004Bot, UT2004BotParameters> runner = new UT2004BotRunner<UT2004Bot, UT2004BotParameters>(
				UT2004BotBehavior.class, config.getDefaultBotName(),
				"127.0.0.1", 3000);
		runner.setConsoleLogging(true);

		AgentLogger log = new AgentLogger(new AgentId("Test"));
		BotParameters parameters = new BotParameters();
		parameters.setAgentId("SimpleRed 1");
		parameters.setLogLevel(Level.INFO);
		// // parameters.setSkin(Skin.BotA);
		// // parameters.setTeam(Team.RED);
		// parameters.assignDefaults(BotParameters.getDefaults(log));
		// BotParameters parameters1 = new BotParameters(log);
		// parameters1.setAgentId("SimpleRed 2");
		// // parameters.setSkin(Skin.BotA);
		// // parameters.setTeam(Team.RED);
		// parameters.assignDefaults(BotParameters.getDefaults(log));
		// BotParameters parameters2 = new BotParameters(log);
		// parameters2.setAgentId("SimpleRed 3");
		// // parameters.setSkin(Skin.BotA);
		// // parameters.setTeam(Team.RED);
		// parameters2.assignDefaults(BotParameters.getDefaults(log));

		// List<UT2004Bot> agents = runner.startAgents(parameters, parameters1,
		// parameters2);
		List<UT2004Bot> agents = runner.startAgents(parameters);

		List<PerceptHandler> handlers = new ArrayList<PerceptHandler>(
				agents.size());
		List<ActionHandler> actionHandlers = new ArrayList<ActionHandler>(
				agents.size());

		for (UT2004Bot agent : agents) {
			PerceptHandler handler = new AllPerceptPerceptHandler(
					(AllPerceptsProvider) agent.getController());
			handlers.add(handler);
		}

		for (UT2004Bot agent : agents) {
			ActionHandler handler = new DefaultActionHandler(
					agent.getController());
			actionHandlers.add(handler);
		}

		while (true) {

			Thread.sleep(500);

			for (PerceptHandler handler : handlers) {
				LinkedList<Percept> list = handler.getAllPercepts();
				for (Percept p : list) {
					if (p.getName().equals("weapon"))
						System.out.println(p);
				}
			}
			UT2004BotBehavior behavior = ((UT2004BotBehavior) agents.get(0)
					.getController());
			// Player p = behavior.getPlayers().getNearestVisiblePlayer();
			// if (p != null)
			// behavior.navigate(new UnrealIdOrLocation(p.getId()));

			// behavior.shoot(new SelectorList(new NearestFriendly()));

			// behavior.navigate(new
			// UnrealIdOrLocation(behavior.getInfo().getId()));

			// List<NavPoint> navs = new
			// ArrayList<NavPoint>(behavior.getWorld().getAll(NavPoint.class).values());
			//
			// int first = new Random().nextInt(navs.size());
			// int second = new Random().nextInt(navs.size());
			//
			//
			// System.out.println(behavior.getInfo().getSelf());

			// behavior.respawn();

			// System.out.println(behavior.path(new
			// UnrealIdOrLocation(navs.get(first).getId()), new
			// UnrealIdOrLocation( navs.get(second).getId())));

			// behavior.look(new SelectorList(new
			// PlayerOrNavpoint(navs.get(first).getId())));
			// Player p = behavior.getPlayers().getNearestVisiblePlayer();
			// if(p != null)
			// behavior.look(new SelectorList(new PlayerOrNavpoint(p.getId())));

			// Map<UnrealId, Item> items =
			// behavior.getItems().getAllItems(ItemType.SUPER_SHIELD_PACK);
			// Item nearest = DistanceUtils.getNearest(items.values(),
			// behavior.getInfo().getLocation());
			//
			// if(!behavior.getInfo().hasHighArmor()){
			// behavior.navigate(new UnrealIdOrLocation(nearest.getId()));
			// } else {
			// behavior.navigate(new UnrealIdOrLocation(navs.get(1).getId()));
			// }

			// UnrealIdOrLocation id = new
			// UnrealIdOrLocation(UnrealId.get("CTF-UG-Chrome.xRedFlagBase1"));
			// UnrealIdOrLocation id2 = new
			// UnrealIdOrLocation(UnrealId.get("CTF-UG-Chrome.PathNode236"));
			//
			// if(!behavior.getNavigation().isNavigating()){
			// if(!behavior.getInfo().isAtLocation(behavior.getGame().getFlagBase(0))){
			// behavior.navigate(id);
			// } else {
			// behavior.navigate(id2);
			// }
			// }

			//behavior.stopShooting();
		}

	}

}
