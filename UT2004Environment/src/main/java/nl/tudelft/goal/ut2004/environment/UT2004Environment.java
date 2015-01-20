/**
 * UT2004Environment, an implementation of the environment interface standard that 
 * facilitates the connection between GOAL and UT2004. 
 * 
 * Copyright (C) 2012 UT2004Environment authors.
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

package nl.tudelft.goal.ut2004.environment;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.goal.unreal.environment.AbstractUnrealEnvironment;
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
import nl.tudelft.goal.unreal.util.EnvironmentUtil;
import nl.tudelft.goal.ut2004.agent.MyAllPerceptsProvider;
import nl.tudelft.goal.ut2004.agent.PerceptsReadyListener;
import nl.tudelft.goal.ut2004.agent.UT2004BotBehavior;
import nl.tudelft.goal.ut2004.server.EnvironmentControllerServer;
import nl.tudelft.goal.ut2004.server.EnvironmentControllerServerModule;
import nl.tudelft.goal.ut2004.translators.CategoryTranslator;
import nl.tudelft.goal.ut2004.translators.ComboTranslator;
import nl.tudelft.goal.ut2004.translators.FireModeTranslator;
import nl.tudelft.goal.ut2004.translators.FlagStateTranslator;
import nl.tudelft.goal.ut2004.translators.GameTypeTranslator;
import nl.tudelft.goal.ut2004.translators.GroupTranslator;
import nl.tudelft.goal.ut2004.translators.ItemTypeTranslator;
import nl.tudelft.goal.ut2004.translators.NavigationStateTranslator;
import nl.tudelft.goal.ut2004.translators.ScopeTranslator;
import nl.tudelft.goal.ut2004.translators.SelectorListTranslator;
import nl.tudelft.goal.ut2004.translators.SelectorTranslator;
import nl.tudelft.goal.ut2004.translators.UT2004GroupTranslator;
import nl.tudelft.goal.ut2004.translators.UT2004ItemTypeTranslator;
import nl.tudelft.goal.ut2004.translators.WeaponPrefListTranslator;
import nl.tudelft.goal.ut2004.translators.WeaponPrefTranslator;
import nl.tudelft.goal.ut2004.visualizer.connection.AddBotCommand;
import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentServiceListener;
import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentServiceMediator;
import nl.tudelft.goal.ut2004.visualizer.connection.client.RemoteVisualizer;
import nl.tudelft.goal.ut2004.visualizer.connection.client.VisualizerServiceDefinition;
import nl.tudelft.pogamut.base.server.ReconnectingServerDefinition;
import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base3d.worldview.IVisionWorldView;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotController;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerFactory;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerModule;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004ServerRunner;
import cz.cuni.amis.pogamut.ut2004.utils.UTBotRunner;
import cz.cuni.amis.utils.flag.FlagListener;
import eis.eis2java.handlers.AllPerceptPerceptHandler;
import eis.eis2java.handlers.DefaultActionHandler;
import eis.eis2java.translation.Translator;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

public class UT2004Environment extends AbstractUnrealEnvironment {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 8240549393243585632L;

	// Connection to the visualizer. Can be used to add bots to the environment.
	private ReconnectingServerDefinition<RemoteVisualizer> visualizerConnection;

	@Override
	protected void registerTranslators() {

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

		UnrealIdOrLocationTranslator unrealIdOrLocationTranslator = new UnrealIdOrLocationTranslator();
		translator
				.registerParameter2JavaTranslator(unrealIdOrLocationTranslator);
		translator
				.registerJava2ParameterTranslator(unrealIdOrLocationTranslator);

		VelocityTranslator velocityTranslator = new VelocityTranslator();
		translator.registerJava2ParameterTranslator(velocityTranslator);
		translator.registerParameter2JavaTranslator(velocityTranslator);
		/*
		 * Translators provided by the UT2004 environment.
		 * 
		 * Please list these in lexical order.
		 */

		CategoryTranslator categoryTranslator = new CategoryTranslator();
		translator.registerJava2ParameterTranslator(categoryTranslator);
		translator.registerParameter2JavaTranslator(categoryTranslator);

		ComboTranslator comboTranslator = new ComboTranslator();
		translator.registerParameter2JavaTranslator(comboTranslator);

		FireModeTranslator fireModeTranslator = new FireModeTranslator();
		translator.registerJava2ParameterTranslator(fireModeTranslator);
		translator.registerParameter2JavaTranslator(fireModeTranslator);

		FlagStateTranslator flagStateTranslator = new FlagStateTranslator();
		translator.registerJava2ParameterTranslator(flagStateTranslator);

		GameTypeTranslator gameTypeTranslator = new GameTypeTranslator();
		translator.registerJava2ParameterTranslator(gameTypeTranslator);

		GroupTranslator groupTranslator = new GroupTranslator();
		translator.registerJava2ParameterTranslator(groupTranslator);
		translator.registerParameter2JavaTranslator(groupTranslator);

		ItemTypeTranslator itemTypeTranslator = new ItemTypeTranslator();
		translator.registerJava2ParameterTranslator(itemTypeTranslator);
		translator.registerParameter2JavaTranslator(itemTypeTranslator);

		NavigationStateTranslator navigationStateTranslator = new NavigationStateTranslator();
		translator.registerJava2ParameterTranslator(navigationStateTranslator);

		SelectorListTranslator selectorListTranslator = new SelectorListTranslator();
		translator.registerParameter2JavaTranslator(selectorListTranslator);

		SelectorTranslator selectorTranslator = new SelectorTranslator();
		translator.registerParameter2JavaTranslator(selectorTranslator);

		UT2004GroupTranslator ut2004GroupTranslator = new UT2004GroupTranslator();
		translator.registerJava2ParameterTranslator(ut2004GroupTranslator);
		translator.registerParameter2JavaTranslator(ut2004GroupTranslator);

		UT2004ItemTypeTranslator ut2004ItemTypeTranslator = new UT2004ItemTypeTranslator();
		translator.registerJava2ParameterTranslator(ut2004ItemTypeTranslator);
		translator.registerParameter2JavaTranslator(ut2004ItemTypeTranslator);

		WeaponPrefListTranslator weaponPrefListTranslator = new WeaponPrefListTranslator();
		translator.registerParameter2JavaTranslator(weaponPrefListTranslator);

		WeaponPrefTranslator weaponPrefTranslator = new WeaponPrefTranslator();
		translator.registerParameter2JavaTranslator(weaponPrefTranslator);

		ScopeTranslator scopeTranslator = new ScopeTranslator();
		translator.registerParameter2JavaTranslator(scopeTranslator);
	}

	public static void main(String[] args) throws ManagementException {
		HashMap<String, Parameter> map = new HashMap<String, Parameter>();
		map.put("botNames", new ParameterList(new Identifier("Test")));

		new UT2004Environment().init(map);

	}

	@Override
	protected Class<UT2004BotBehavior> getControlerClass() {
		return UT2004BotBehavior.class;
	}

	protected UTBotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters> getBotRunner(
			Configuration configuration) {
		UT2004BotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters> runner = new UT2004BotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters>(
				getControlerClass(), configuration.getDefaultBotName(),
				configuration.getBotServerHost(),
				configuration.getBotServerPort());
		return runner;
	}

	@Override
	protected void registerNewBotEntity(
			final UT2004Bot<IVisionWorldView, IAct, UT2004BotController> agent)
			throws ManagementException {
		final String simpleID = EnvironmentUtil.simplefyID(agent
				.getComponentId());
		final UT2004BotController controller = agent.getController();
		if (!(controller instanceof MyAllPerceptsProvider)) {
			throw new ManagementException(
					"Expected a controller that implements "
							+ MyAllPerceptsProvider.class.getSimpleName());
		}

		((MyAllPerceptsProvider) controller)
				.setPerceptsReadyListener(new PerceptsReadyListener() {
					@Override
					public void notifyPerceptsReady() {
						System.out.println("Registering the new entity");
						try {
							registerEntity(simpleID, "bot", controller,
									new DefaultActionHandler(controller),
									new AllPerceptPerceptHandler(
											(AllPerceptsProvider) controller));
						} catch (EntityException e) {
							agent.stop();
							System.out.println("Unable to register entity");
							e.printStackTrace();
						}

					}
				});

	}

	@Override
	protected UT2004ServerRunner<? extends IUT2004Server, ? extends UT2004AgentParameters> createServerRunner() {
		UT2004ServerModule<UT2004AgentParameters> serverModule = new EnvironmentControllerServerModule<UT2004AgentParameters>();
		UT2004ServerFactory<EnvironmentControllerServer, UT2004AgentParameters> serverFactory = new UT2004ServerFactory<EnvironmentControllerServer, UT2004AgentParameters>(
				serverModule);
		UT2004ServerRunner<EnvironmentControllerServer, UT2004AgentParameters> serverRunner = new UT2004ServerRunner<EnvironmentControllerServer, UT2004AgentParameters>(
				serverFactory, "UTServer",
				configuration.getControlServerHost(),
				configuration.getControlServerPort());
		return serverRunner;
	}

	@Override
	protected synchronized void initializeEnvironment(
			Map<String, Parameter> parameters) throws ManagementException {
		super.initializeEnvironment(parameters);

		// Set up (future) connection to visualizer. Connecting is done later.
		try {
			visualizerConnection = new ReconnectingServerDefinition<RemoteVisualizer>(
					new VisualizerServiceDefinition());
			visualizerConnection.getServerFlag().addListener(
					new VisualizerServiceListener());
		} catch (RemoteException e) {
			log.severe("Could not start connection to Visualizer: " + e);
		}

	}

	/**
	 * Helper class that handles the connection to the visualizer. Registers a
	 * mediator with the visualizer and listens to the actions it requests.
	 * 
	 * @author M.P. Korstanje
	 * 
	 */
	private class VisualizerServiceListener implements
			EnvironmentServiceListener, FlagListener<RemoteVisualizer> {

		private final EnvironmentServiceMediator mediator;

		public VisualizerServiceListener() throws RemoteException {
			mediator = new EnvironmentServiceMediator(getComponentId());
			mediator.setListener(this);
		}

		@Override
		public void flagChanged(RemoteVisualizer visualizer) {
			if (visualizer != null) {
				visualizer.setEnvironment(mediator);
			}
		}

		/**
		 * 
		 * Listens to actions executed by the remote visualizer.
		 * 
		 * @throws ManagementException
		 * 
		 */
		@Override
		public void addBot(AddBotCommand command) throws ManagementException {

			BotParameters parameters = new BotParameters();

			// Fill out the bot parameter
			if (command.getBotName() != null) {
				parameters.setAgentId(command.getBotName());
			}
			if (command.getLogLevel() != null) {
				parameters.setLogLevel(command.getLogLevel());
			}
			if (command.getShouldLeadTarget() != null) {
				parameters.setShouldLeadTarget(command.getShouldLeadTarget());
			}
			if (command.getSkill() != null) {
				parameters.setSkill(command.getSkill());
			}
			if (command.getSkin() != null) {
				parameters.setSkin(command.getSkin());
			}
			if (command.getTeam() != null) {
				parameters.setTeam(command.getTeam());
			}

			if (command.getRotation() != null) {
				parameters.setInitialRotation(command.getRotation());
			}

			if (command.getLocation() != null) {
				parameters.setInitialLocation(command.getLocation());
			}

			// Take connection and other settings from init.
			// FIXME: Should get the defaults or set the connection parameters
			// too.
			// parameters.assignDefaults(botParameters);

			startAgent(parameters);

		}

	}

	@Override
	protected synchronized void connectEnvironment() throws ManagementException {
		super.connectEnvironment();

		// Connect to visualizer
		URI visualizerUri = configuration.getVisualizer();
		if (visualizerUri != null) {
			log.info("Connecting to visualizer server at " + visualizerUri
					+ " .");

			visualizerConnection.setUri(visualizerUri);
		} else {
			log.info("No address for the visualizer server was provided. The environment will not try to connect to the visualizer.");
		}
	}

	@Override
	protected synchronized void killEnvironment() {
		super.killEnvironment();
		// Close the connection to the visualizer.
		visualizerConnection.stopServer();

	}

	@Override
	public void createServerEntity() throws ManagementException {
		super.createServerEntity();

	}

}
