package nl.tudelft.goal.unreal.messages;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


/**
 * Describes the configuration for the environment. This configuration is
 * provided through the init command.
 * 
 * @author M.P. Korstanje
 * 
 */

public class Configuration {

	public static final Level DEFAULT_LOG_LEVEL = Level.WARNING;
	public static final String DEFAULT_BOT_NAME = "UnrealGOALBot";
	public static final String LOCAL_HOST = "127.0.0.1";
	public static final int CONTROL_SERVER_PORT = 3001;
	public static final int BOT_SERVER_PORT = 3000;
	
	public static final String CONTROL_SERVER = LOCAL_HOST + ":" + CONTROL_SERVER_PORT;
	public static final String BOT_SERVER = LOCAL_HOST + ":" + BOT_SERVER_PORT;

	private static final URI VISUALIZER_SERVER = null;
	// Environment parameters
	private List<BotParameters> bots;
	private URI visualizer;
	private URI botServer;
	private URI controlServer;

	private Level logLevel;

	public List<BotParameters> getBots() {
		return bots == null ? new ArrayList<BotParameters>() : bots;
	}

	public void setBots(List<BotParameters> bots) {
		this.bots = bots;
	}

	public URI getVisualizer() {
		return visualizer;
	}

	public void setVisualizer(URI visualizer) {
		this.visualizer = visualizer;
	}

	public URI getBotServer() {
		return botServer;
	}

	public void setBotServer(URI botServer) {
		this.botServer = botServer;
	}

	public URI getControlServer() {
		return controlServer;
	}

	public void setControlServer(URI controlServer) {
		this.controlServer = controlServer;
	}

	public Configuration setLogLevel(Level level) {
		assert level != null;
		this.logLevel = level;
		return this;
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public String getDefaultBotName() {
		return DEFAULT_BOT_NAME;
	}

	/**
	 * 
	 * @return The port bot server, or -1 if the port is undefined
	 */
	public int getBotServerPort() {
		return botServer == null ? -1 : botServer.getPort();
	}

	/**
	 * 
	 * @return The host of the bot server, or null if the server is undefined
	 */
	public String getBotServerHost() {
		return botServer == null ? null : botServer.getHost();
	}

	public static Configuration getDefaults() {

		Configuration configuration = new Configuration();

		configuration.setBots(new ArrayList<BotParameters>());
		configuration.setVisualizer(VISUALIZER_SERVER);

		try {
			configuration.setBotServer(new URI(BOT_SERVER));
			configuration.setControlServer(new URI(CONTROL_SERVER));
		} catch (URISyntaxException e) {
			// Ignore. Syntax is correct
		}

		configuration.setLogLevel(DEFAULT_LOG_LEVEL);

		return configuration;
	}

	public void assignDefaults(Configuration configuration) {

		if (bots == null)
			bots = configuration.bots;
		if (visualizer == null)
			visualizer = configuration.visualizer;
		if (botServer == null)
			botServer = configuration.botServer;
		if (controlServer == null)
			controlServer = configuration.controlServer;
		if (logLevel == null)
			logLevel = configuration.logLevel;

	}

	public int getControlServerPort() {
		return controlServer == null ? -1 : controlServer.getPort();
	}

	public String getControlServerHost() {
		return controlServer == null ? null : controlServer.getHost();
	}
}
