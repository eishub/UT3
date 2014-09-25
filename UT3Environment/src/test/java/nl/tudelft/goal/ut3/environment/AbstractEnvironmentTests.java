/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.ut3.environment;

import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base.utils.Pogamut;
import cz.cuni.amis.pogamut.base3d.worldview.IVisionWorldView;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotController;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
import cz.cuniz.amis.pogamut.ut3.utils.UT3BotRunner;
import eis.eis2java.handlers.ActionHandler;
import eis.eis2java.handlers.AllPerceptPerceptHandler;
import eis.eis2java.handlers.DefaultActionHandler;
import eis.eis2java.handlers.PerceptHandler;
import eis.eis2java.translation.Translator;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.unreal.translators.LocationTranslator;
import nl.tudelft.goal.unreal.translators.NoneTranslator;
import nl.tudelft.goal.unreal.translators.PerceptTranslator;
import nl.tudelft.goal.unreal.translators.RotationTranslator;
import nl.tudelft.goal.unreal.translators.TeamTranslator;
import nl.tudelft.goal.unreal.translators.UnrealIdTranslator;
import nl.tudelft.goal.unreal.translators.VelocityTranslator;
import nl.tudelft.goal.ut2004.util.Team;
import nl.tudelft.goal.ut3.agent.UT3BotBehavior;
import nl.tudelft.goal.ut3.translators.CategoryTranslator;
import nl.tudelft.goal.ut3.translators.FireModeTranslator;
import nl.tudelft.goal.ut3.translators.FlagStateTranslator;
import nl.tudelft.goal.ut3.translators.GameTypeTranslator;
import nl.tudelft.goal.ut3.translators.ItemTypeTranslator;
import nl.tudelft.goal.ut3.translators.NavigationStateTranslator;
import nl.tudelft.goal.ut3.translators.SelectorListTranslator;
import nl.tudelft.goal.ut3.translators.SelectorTranslator;
import nl.tudelft.goal.ut3.translators.UT3ItemTypeTranslator;
import nl.tudelft.goal.unreal.translators.UnrealIdOrLocationTranslator;
import nl.tudelft.goal.ut3.translators.WeaponPrefListTranslator;
import nl.tudelft.goal.ut3.translators.WeaponPrefTranslator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test-suite to provide a full environment test.
 * 
 * @author Evers
 */
@SuppressWarnings("rawtypes")
public class AbstractEnvironmentTests {

	public static final String BOT_NAME = "TESTBOT";
	public static final String MAP_NAME = "CTF-GOALTest";
	public static final Team BOT_TEAM = Team.RED;
	
	protected static UT3BotRunner<UT2004Bot, UT2004BotParameters> runner;
	protected static List<UT2004Bot> agents;
	protected static List<PerceptHandler> perceptHandlers;
	protected List<ActionHandler> actionHandlers;

	protected static UT3BotBehavior testBot;

	protected static List<Percept> startupPercepts;

	/**
	 * Register all the required translators for the GOAL environment.
	 */
	private static void registerTranslators() {
		Translator translator = Translator.getInstance();


		/*
		 * To translate from Parameter2Java we are given an UnrealId. However we
		 * can not access the agents memory during translation. To work around
		 * this we store everything we have send to any agent. Hence the same
		 * object has to be used for both directions.
		 */
		
		/*
		 * Translators provided by the BaseUnrealEnvironment.
		 * 
		 * Please list these in lexical order.
		 */
		LocationTranslator locationTranslator = new LocationTranslator();
		translator.registerJava2ParameterTranslator(locationTranslator);
		translator.registerParameter2JavaTranslator(locationTranslator);
		
		NoneTranslator noneTranslator = new NoneTranslator();
		translator.registerJava2ParameterTranslator(noneTranslator);

		PerceptTranslator perceptTranslator = new PerceptTranslator();
		translator.registerJava2ParameterTranslator(perceptTranslator);

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
		 * Translators provided by the UT3 environment.
		 * 
		 * Please list these in lexical order.
		 */
		CategoryTranslator ut3ItemTypeTranslator = new CategoryTranslator();
		translator.registerJava2ParameterTranslator(ut3ItemTypeTranslator);

		UT3ItemTypeTranslator ut3CategoryTranslator = new UT3ItemTypeTranslator();
		translator.registerJava2ParameterTranslator(ut3CategoryTranslator);
		translator.registerParameter2JavaTranslator(ut3CategoryTranslator);

		CategoryTranslator categoryTranslator = new CategoryTranslator();
		translator.registerJava2ParameterTranslator(categoryTranslator);
		translator.registerParameter2JavaTranslator(categoryTranslator);

		ItemTypeTranslator itemTypeTranslator = new ItemTypeTranslator();
		translator.registerJava2ParameterTranslator(itemTypeTranslator);

		FireModeTranslator fireModeTranslator = new FireModeTranslator();
		translator.registerJava2ParameterTranslator(fireModeTranslator);
		translator.registerParameter2JavaTranslator(fireModeTranslator);

		FlagStateTranslator flagStateTranslator = new FlagStateTranslator();
		translator.registerJava2ParameterTranslator(flagStateTranslator);

		GameTypeTranslator gameTypeTranslator = new GameTypeTranslator();
		translator.registerJava2ParameterTranslator(gameTypeTranslator);

		NavigationStateTranslator navigationStateTranslator = new NavigationStateTranslator();
		translator.registerJava2ParameterTranslator(navigationStateTranslator);

		SelectorListTranslator selectorListTranslator = new SelectorListTranslator();
		translator.registerParameter2JavaTranslator(selectorListTranslator);

		SelectorTranslator selectorTranslator = new SelectorTranslator();
		translator.registerParameter2JavaTranslator(selectorTranslator);

		UnrealIdOrLocationTranslator unrealIdOrLocationTranslator = new UnrealIdOrLocationTranslator();
		translator.registerParameter2JavaTranslator(unrealIdOrLocationTranslator);

		WeaponPrefListTranslator weaponPrefListTranslator = new WeaponPrefListTranslator();
		translator.registerParameter2JavaTranslator(weaponPrefListTranslator);

		WeaponPrefTranslator weaponPrefTranslator = new WeaponPrefTranslator();
		translator.registerParameter2JavaTranslator(weaponPrefTranslator);
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		// Setup environment
		// 1. Register translators
		registerTranslators();

		// 2. Configure parameters
		BotParameters parameters = new BotParameters();

		parameters.setAgentId(BOT_NAME);
		parameters.setTeam(BOT_TEAM);
		parameters.setLogLevel(Level.INFO);

		// 3. Start environment                
                runner = new UT3BotRunner<UT2004Bot, UT2004BotParameters>(
                        UT3BotBehavior.class, Configuration.DEFAULT_BOT_NAME, Configuration.LOCAL_HOST, Configuration.BOT_SERVER_PORT);
		runner.setConsoleLogging(true);
		agents = runner.startAgents(parameters);

		// 4. Register percept handlers
		perceptHandlers = new ArrayList<PerceptHandler>(agents.size());

		for (UT2004Bot agent : agents) {
			PerceptHandler handler = new AllPerceptPerceptHandler(
					(AllPerceptsProvider) agent.getController());
			perceptHandlers.add(handler);
		}

		// 5. Register action handlers
		List<ActionHandler> actionHandlers = new ArrayList<ActionHandler>(
				agents.size());

		for (UT2004Bot agent : agents) {
			ActionHandler handler = new DefaultActionHandler(
					agent.getController());
			actionHandlers.add(handler);
		}

		// 6. Set testbot to the UT3 bot controller
		testBot = ((UT3BotBehavior) agents.get(0).getController());

		/*
		 * if(!testBot.getGame().getMapName().equals(MAP_NAME)) { throw new
		 * Exception("Error, tests only supported on: " + MAP_NAME); }
		 */
		// 7. Catch all startup percepts to provide send-once tests
		Thread.sleep(1000);
		startupPercepts = new ArrayList<Percept>();
		for (PerceptHandler handler : perceptHandlers) {
			startupPercepts.addAll(handler.getAllPercepts());
		}
	}

	public static List<Percept> getPercepts() throws PerceiveException {
		List<Percept> perceptList = new ArrayList<Percept>();
		for (PerceptHandler handler : perceptHandlers) {
			perceptList.addAll(handler.getAllPercepts());
		}
		return perceptList;
	}
}
