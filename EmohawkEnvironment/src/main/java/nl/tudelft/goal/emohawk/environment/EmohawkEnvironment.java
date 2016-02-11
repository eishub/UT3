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

package nl.tudelft.goal.emohawk.environment;

import java.util.HashMap;

import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base3d.worldview.IVisionWorldView;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotController;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.pogamut.ut2004.utils.UTBotRunner;
import eis.eis2java.handlers.AllPerceptPerceptHandler;
import eis.eis2java.handlers.DefaultActionHandler;
import eis.eis2java.translation.Translator;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import nl.tudelft.goal.emohawk.agent.EmohawkBotBehavior;
import nl.tudelft.goal.emohawk.translators.EmoticonTypeTranslator;
import nl.tudelft.goal.emohawk.translators.PerceptTranslator;
import nl.tudelft.goal.emohawk.translators.PlaceTranslator;
import nl.tudelft.goal.emohawk.translators.UnrealIdOrLocationTranslator;
import nl.tudelft.goal.unreal.environment.AbstractUnrealEnvironment;
import nl.tudelft.goal.unreal.environment.MyAllPerceptsProvider;
import nl.tudelft.goal.unreal.environment.PerceptsReadyListener;
import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.unreal.translators.LocationTranslator;
import nl.tudelft.goal.unreal.translators.RotationTranslator;
import nl.tudelft.goal.unreal.translators.TeamTranslator;
import nl.tudelft.goal.unreal.translators.UnrealIdTranslator;
import nl.tudelft.goal.unreal.translators.VelocityTranslator;
import nl.tudelft.goal.unreal.util.EnvironmentUtil;

public class EmohawkEnvironment extends AbstractUnrealEnvironment {
	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 8240549393243585632L;

	@Override
	protected void registerTranslators() {
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
		 * Translators provided by the EmohawkBot environment.
		 *
		 * Please list these in lexical order.
		 */

		EmoticonTypeTranslator emoticonTypeTranslator = new EmoticonTypeTranslator();
		translator.registerJava2ParameterTranslator(emoticonTypeTranslator);
		translator.registerParameter2JavaTranslator(emoticonTypeTranslator);

		PerceptTranslator perceptTranslator = new PerceptTranslator();
		translator.registerJava2ParameterTranslator(perceptTranslator);

		PlaceTranslator placeTranslator = new PlaceTranslator();
		translator.registerJava2ParameterTranslator(placeTranslator);

		UnrealIdOrLocationTranslator unrealIdOrLocationTranslator = new UnrealIdOrLocationTranslator();
		translator.registerParameter2JavaTranslator(unrealIdOrLocationTranslator);
	}

	public static void main(String[] args) throws ManagementException {
		HashMap<String, Parameter> map = new HashMap<String, Parameter>();
		map.put("botNames", new ParameterList(new Identifier("Test")));
		new EmohawkEnvironment().init(map);
	}

	@Override
	protected Class<EmohawkBotBehavior> getControlerClass() {
		return EmohawkBotBehavior.class;
	}

	@Override
	protected UTBotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters> getBotRunner(
			Configuration configuration) {
		UT2004BotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters> runner = new UT2004BotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters>(
				getControlerClass(), configuration.getDefaultBotName(), configuration.getBotServerHost(),
				configuration.getBotServerPort());
		return runner;
	}

	@Override
	protected void registerNewBotEntity(final UT2004Bot<IVisionWorldView, IAct, UT2004BotController> agent)
			throws ManagementException {
		final String simpleID = EnvironmentUtil.simplefyID(agent.getComponentId());
		final UT2004BotController controller = agent.getController();
		if (!(controller instanceof MyAllPerceptsProvider)) {
			throw new ManagementException(
					"Expected a controller that implements " + MyAllPerceptsProvider.class.getSimpleName());
		}
		((MyAllPerceptsProvider) controller).setPerceptsReadyListener(new PerceptsReadyListener() {
			@Override
			public void notifyPerceptsReady() {
				System.out.println("Registering the new entity");
				try {
					registerEntity(simpleID, "bot", controller, new DefaultActionHandler(controller),
							new AllPerceptPerceptHandler((AllPerceptsProvider) controller));
				} catch (EntityException e) {
					agent.stop();
					System.out.println("Unable to register entity");
					e.printStackTrace();
				}
			}
		});
	}
}
