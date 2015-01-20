/**
 * BaseUnrealEnvironment, an implementation of the environment interface standard that 
 * facilitates the connection between GOAL and the UT2004 engine. 
 * 
 * Copyright (C) 2012 BaseUnrealEnvironment authors.
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
package nl.tudelft.goal.unreal.environment;

import java.util.HashMap;
import java.util.Map;

import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.unreal.messages.MapOfParameters;
import nl.tudelft.goal.unreal.translators.AgentIdTranslator;
import nl.tudelft.goal.unreal.translators.BotParametersKeyTranslator;
import nl.tudelft.goal.unreal.translators.BotParametersListTranslator;
import nl.tudelft.goal.unreal.translators.BotParametersTranslator;
import nl.tudelft.goal.unreal.translators.ConfigurationKeyTranslator;
import nl.tudelft.goal.unreal.translators.ConfigurationTranslator;
import nl.tudelft.goal.unreal.translators.LevelTranslator;
import nl.tudelft.goal.unreal.translators.LocationTranslator;
import nl.tudelft.goal.unreal.translators.ParameterMapTranslator;
import nl.tudelft.goal.unreal.translators.RotationTranslator;
import nl.tudelft.goal.unreal.translators.SkinTranslator;
import nl.tudelft.goal.unreal.translators.TeamTranslator;
import nl.tudelft.goal.unreal.translators.URITranslator;
import nl.tudelft.goal.unreal.translators.VelocityTranslator;
import nl.tudelft.goal.unreal.util.EnvironmentUtil;
import nl.tudelft.goal.unreal.util.vecmathcheck.VecmathCheck;
import cz.cuni.amis.pogamut.base.agent.IAgent;
import cz.cuni.amis.pogamut.base.agent.IAgentId;
import cz.cuni.amis.pogamut.base.agent.exceptions.AgentException;
import cz.cuni.amis.pogamut.base.agent.impl.AgentId;
import cz.cuni.amis.pogamut.base.agent.state.level0.IAgentState;
import cz.cuni.amis.pogamut.base.agent.state.level1.IAgentStateDown;
import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base.component.IComponent;
import cz.cuni.amis.pogamut.base.utils.Pogamut;
import cz.cuni.amis.pogamut.base.utils.logging.AgentLogger;
import cz.cuni.amis.pogamut.base.utils.logging.IAgentLogger;
import cz.cuni.amis.pogamut.base.utils.logging.LogCategory;
import cz.cuni.amis.pogamut.base3d.worldview.IVisionWorldView;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.bot.IUT2004BotController;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotController;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Pause;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerFactory;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerModule;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004ServerRunner;
import cz.cuni.amis.pogamut.ut2004.utils.UTBotRunner;
import cz.cuni.amis.utils.exception.PogamutException;
import cz.cuni.amis.utils.flag.FlagListener;
import eis.eis2java.environment.AbstractEnvironment;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.handlers.ActionHandler;
import eis.eis2java.handlers.DefaultActionHandler;
import eis.eis2java.handlers.DefaultPerceptHandler;
import eis.eis2java.handlers.PerceptHandler;
import eis.eis2java.translation.Translator;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;

@SuppressWarnings("rawtypes")
public abstract class AbstractUnrealEnvironment extends
		SimpleTransitioningEnvironment implements IComponent {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 6786623950045095814L;
	protected final IAgentId id;

	// Manager of logs.
	protected final IAgentLogger environmentLogger;
	// Actual logger
	protected final LogCategory log;

	// Agent state listeners
	private final Map<String, AgentDownListener> agentDownListeners;

	// Configuration based on init parameters
	protected Configuration configuration;

	// Connection to the ut Server. Can be used to pause/resume the game.
	private IUT2004Server utServer;

	/**
	 * Constructs the Unreal Environment. The environment won't be ready until
	 * until is has has been initialized.
	 * 
	 */
	public AbstractUnrealEnvironment() {
		id = new AgentId(getName());
		agentDownListeners = new HashMap<String, AgentDownListener>();
		environmentLogger = new AgentLogger(id);
		environmentLogger.addDefaultConsoleHandler();
		log = environmentLogger.getCategory(this);
		log.info("Environment has been created.");
		log.addConsoleHandler();

		// Register own translators.
		Translator translator = Translator.getInstance();
		translator.registerParameter2JavaTranslator(new AgentIdTranslator());

		translator
				.registerParameter2JavaTranslator(new BotParametersKeyTranslator());
		translator
				.registerParameter2JavaTranslator(new BotParametersListTranslator());
		translator
				.registerParameter2JavaTranslator(new BotParametersTranslator());

		translator
				.registerParameter2JavaTranslator(new ConfigurationKeyTranslator());
		translator
				.registerParameter2JavaTranslator(new ConfigurationTranslator());

		translator.registerParameter2JavaTranslator(new LevelTranslator());
		translator.registerParameter2JavaTranslator(new LocationTranslator());

		translator
				.registerParameter2JavaTranslator(new ParameterMapTranslator());

		translator.registerParameter2JavaTranslator(new RotationTranslator());

		translator.registerParameter2JavaTranslator(new SkinTranslator());
		translator.registerParameter2JavaTranslator(new TeamTranslator());

		translator.registerParameter2JavaTranslator(new URITranslator());

		translator.registerParameter2JavaTranslator(new VelocityTranslator());

		// Register translators required by the bot controller.
		registerTranslators();
	}

	protected abstract void registerTranslators();

	/**
	 * Provides an unique identifier for this component for use by loggers.
	 */

	@Override
	public IAgentId getComponentId() {
		return id;
	}

	/**
	 * Returns a string representation of the environment based on ID of the
	 * environment.
	 * 
	 * @return a representation of the environment.
	 */
	@Override
	public String toString() {
		return EnvironmentUtil.simplefyID(getComponentId());
	}

	public String getName() {
		return "Unreal Environment for EIS" + requiredVersion();
	}

	protected synchronized void initializeEnvironment(
			Map<String, Parameter> parameters) throws ManagementException {

		// Check if we are loading correct version of vecmath. See ticket #2494.
		if (!VecmathCheck.check()) {
			throw new ManagementException(VecmathCheck.getErrorMessage());
		}

		// Translate configuration
		try {
			// Wrapper pending fix to environment init.
			Parameter parameterMap = new MapOfParameters(parameters);
			configuration = Translator.getInstance().translate2Java(
					parameterMap, Configuration.class);
			configuration.assignDefaults(Configuration.getDefaults());
		} catch (TranslationException e) {
			throw new ManagementException("Invalid parameters", e);
		}

		// Set log level for environment
		log.setLevel(configuration.getLogLevel());
	}

	@Override
	protected synchronized void connectEnvironment() throws ManagementException {
		assert configuration != null;

		// 1. Start server.
		if (configuration.getControlServer() != null)
			startServer();

	}

	@Override
	public synchronized void createServerEntity() throws ManagementException {
		addServerEntity();
	}

	@Override
	protected void connectAgents() throws ManagementException {
		// 2. Start bots.
		for (BotParameters bot : configuration.getBots()) {
			startAgent(bot);
		}

	}

	/**
	 * Make serverRunner and register entities.
	 * 
	 * @throws ManagementException
	 */
	protected void startServer() throws ManagementException {
		// 1. Connect to UT server. Called from init().
		try {

			UT2004ServerRunner<? extends IUT2004Server, ? extends UT2004AgentParameters> serverRunner = createServerRunner();
			utServer = serverRunner.startAgent();
		} catch (PogamutException e) {
			throw new ManagementException(
			// Adding exception as String. While Pogamut exceptions
			// themselves are properly serializable, their contents may not be.
					"Pogmut was unable to start the server. Cause: "
							+ e.toString());
		}
	}

	/**
	 * Adds the server entity
	 * 
	 * @throws ManagementException
	 */
	protected void addServerEntity() throws ManagementException {
		String simpleID = EnvironmentUtil.simplefyID(utServer.getComponentId());

		try {
			registerEntity(simpleID, "server", utServer,
					createServerActionHandler(utServer),
					createServerPerceptHandler(utServer));
		} catch (EntityException e) {
			utServer.stop();
			throw new ManagementException("Unable to register entity", e);
		}

		// 6. Add bot dead listeners
		agentDownListeners.put(simpleID, new AgentDownListener(simpleID,
				utServer));

		// 7. Check if server is still alive. Throw out if not.
		// TODO: How?

	}

	protected ActionHandler createServerActionHandler(IUT2004Server entity)
			throws EntityException {
		return new DefaultActionHandler(entity);
	}

	protected PerceptHandler createServerPerceptHandler(IUT2004Server entity)
			throws EntityException {
		return new DefaultPerceptHandler(entity);
	}

	protected UT2004ServerRunner<? extends IUT2004Server, ? extends UT2004AgentParameters> createServerRunner() {
		UT2004ServerModule<UT2004AgentParameters> serverModule = new UT2004ServerModule<UT2004AgentParameters>();
		UT2004ServerFactory<IUT2004Server, UT2004AgentParameters> serverFactory = new UT2004ServerFactory<IUT2004Server, UT2004AgentParameters>(
				serverModule);
		UT2004ServerRunner<IUT2004Server, UT2004AgentParameters> serverRunner = new UT2004ServerRunner<IUT2004Server, UT2004AgentParameters>(
				serverFactory, "UTServer",
				configuration.getControlServerHost(),
				configuration.getControlServerPort());
		return serverRunner;
	}

	protected synchronized void startAgent(BotParameters parameters)
			throws ManagementException {

		// 1. Don't add bots if the environment has been killed.
		if (getState() == EnvironmentState.KILLED) {
			return;
		}

		// 2. Unpause the game. Can't add agents to paused game.
		utServer.getAct().act(new Pause(false, false));

		// 3. Set defaults
		parameters.assignDefaults(BotParameters.getDefaults());

		UTBotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters> runner = getBotRunner(configuration);

		runner.setLogLevel(parameters.getLogLevel());
		// TODO: File logging from inside agent perhaps?

		// 4. Launch bots using the parameters
		UT2004Bot<IVisionWorldView, IAct, UT2004BotController> agent;
		try {
			agent = runner.startAgents(parameters).get(0);
		} catch (PogamutException e) {
			throw new ManagementException(
			// Adding exception as String. While Pogamut exceptions
			// themselves are properly serializable, their contents may not be.
					"Pogmut was unable to start an agents. Cause: "
							+ e.toString());
		}

		// 5. Notify EIS of new entity.
		registerNewBotEntity(agent);

		String simpleID = EnvironmentUtil.simplefyID(agent.getComponentId());
		// UT2004BotController controller = agent.getController();
		// try {
		// registerEntity(simpleID, "bot", controller,
		// createActionHandler(controller),
		// createPerceptHandler(controller));
		// } catch (EntityException e) {
		// agent.stop();
		//
		// throw new ManagementException("Unable to register entity", e);
		// }

		// 6. Add bot dead listeners
		agentDownListeners
				.put(simpleID, new AgentDownListener(simpleID, agent));

		// 7. Check if bots are still alive. Throw out if not.
		if (agent.inState(IAgentStateDown.class)) {
			agentDownListeners.get(simpleID).removeListener();
			synchronizedDeleteEntity(simpleID);

		}

		// 8. Everything aokay
	}

	/**
	 * This method should wait for the percepts to become available (using the
	 * PerceptHandler in the controller) and then register the entity to EIS.
	 * The entity should get the type "bot", and use
	 * EnvironmentUtil.simplefyID(agent.getComponentId()) to get the entity
	 * name.
	 * 
	 * @param agent
	 * @throws ManagementException
	 */
	protected abstract void registerNewBotEntity(
			UT2004Bot<IVisionWorldView, IAct, UT2004BotController> agent)
			throws ManagementException;

	protected abstract UTBotRunner<UT2004Bot<IVisionWorldView, IAct, UT2004BotController>, UT2004BotParameters> getBotRunner(
			Configuration configuration);

	protected abstract Class<? extends IUT2004BotController> getControlerClass();

	protected abstract PerceptHandler createPerceptHandler(
			UT2004BotController controller) throws EntityException;

	protected abstract ActionHandler createActionHandler(
			UT2004BotController controller) throws EntityException;

	protected void startEnvironment() throws ManagementException {
		utServer.getAct().act(new Pause(false, false));
	}

	protected void pauseEvironment() {
		utServer.getAct().act(new Pause(true, false));
	}

	protected synchronized void killEnvironment() {
		// 1. Unpause the environment.
		if (utServer != null)
			utServer.getAct().act(new Pause(false, false));

		// Wait for bots to catch up with unpausing
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// If not able to wait, continue on and try anyway.
		}

		// 2. Shut down all bots.
		for (String id : getEntities()) {

			// TODO: This is a good example why we need a uniform entity
			// interface. Or a way to track the type of an entity.
			Object entity = getEntity(id);
			IAgent bot;
			if (entity instanceof UT2004BotController<?>) {
				@SuppressWarnings("unchecked")
				UT2004BotController<UT2004Bot> controller = ((UT2004BotController<UT2004Bot>) getEntity(id));
				bot = controller.getBot();
			} else {
				bot = (IAgent) entity;
			}

			try {
				agentDownListeners.get(id).removeListener();
				bot.stop();
				log.info(bot.getName() + " has been stopped");
			} catch (AgentException e) {
				// When a bot can not be stopped it will be killed.
				log.info(bot.getName() + " has been killed", e);
			}
		}

		// 3. Close the connection to the utServer.
		if (utServer != null)
			utServer.stop();
		utServer = null;

		// 4. Clear up config
		configuration = null;

		// 5. Stop pogamut platform. Prevents memory leak see #1727
		Pogamut.getPlatform().close();
	}

	/**
	 * Synchronized version of {@link AbstractEnvironment.deleteEntity}. Used by
	 * {@link AgentDownListener} to removed agents that have shut down.
	 * 
	 * @param entity
	 *            to remove.
	 */
	protected synchronized void synchronizedDeleteEntity(String name) {
		try {
			deleteEntity(name);
		} catch (RelationException e) {
			// TODO: This relationship exception is no longer thrown.
			log.severe("Could not delete entity " + name);
		} catch (EntityException e) {
			// TODO: This should be replaced by an assertion in the default
			// implementation of EIS.
			log.severe("Could not delete entity " + name
					+ ", it was already deleted.");
		}
	}

	/**
	 * Monitors the the agent state, if the agent goes down it is removed from
	 * the environment.
	 * 
	 * @author M.P. Korstanje
	 * 
	 */
	private class AgentDownListener implements FlagListener<IAgentState> {

		private final String key;
		private final IAgent agent;

		public AgentDownListener(String key, IAgent agent) {
			this.key = key;
			this.agent = agent;
			this.agent.getState().addStrongListener(this);
		}

		@Override
		public void flagChanged(IAgentState state) {
			if (state instanceof IAgentStateDown) {
				removeListener();
				synchronizedDeleteEntity(key);
			}
		}

		public void removeListener() {
			agent.getState().removeListener(this);
			agentDownListeners.remove(key);
		}
	}

	@Override
	protected boolean isSupportedByEnvironment(Action arg0) {
		return true;
	}

	@Override
	protected boolean isSupportedByType(Action arg0, String arg1) {
		return true;
	}

}
