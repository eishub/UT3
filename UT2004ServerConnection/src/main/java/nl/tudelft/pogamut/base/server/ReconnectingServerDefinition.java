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
package nl.tudelft.pogamut.base.server;

import java.net.URI;

import cz.cuni.amis.utils.exception.PogamutException;
import cz.cuni.amis.utils.flag.Flag;
import cz.cuni.amis.utils.flag.FlagListener;


/**
 * States are:
 * <ul>
 * <li>Connecting</li>
 * <li>Connected</li>
 * <li>Waiting before another connection attempt</li>
 * </ul>
 * 
 * @author ik
 * @author M.P. Korstanje
 * @since 2011/01/15 refactored to wrapper.
 */
public final class ReconnectingServerDefinition<T> extends ServerDefinition<T> {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 866197741748535623L;

	private final FlagListener<T> serverListener = new FlagListener<T>() {

		@Override
		public void flagChanged(T server) { 
			if (server == null) {
				startServer();
			} else {

			}
		}
	};

	private final ServerDefinition<T> serverDefinition;

	private StartServerThread startServerThread;

	public ReconnectingServerDefinition(ServerDefinition<T> serverDefinition) {
		this.serverDefinition = serverDefinition;
		this.serverDefinition.getServerFlag().addListener(serverListener);
	}

	@Override
	public void setServerName(String name) {
		serverDefinition.setServerName(name);
	}

	@Override
	public String getServerName() {
		return serverDefinition.getServerName();
	}

	@Override
	public Flag<String> getServerNameFlag() {
		return serverDefinition.getServerNameFlag();
	}

	@Override
	public void setUri(URI uri) {
		startServer(uri);
	}

	@Override
	public URI getUri() {

		// Parent class calls getUri in constructor,
		// before we could set the serverDefinition.
		if (serverDefinition == null)
			return null;

		return serverDefinition.getUri();
	}

	@Override
	public Flag<URI> getUriFlag() {
		return serverDefinition.getUriFlag();
	}

	/**
	 * Change current server instance.
	 * 
	 * @param server
	 */
	@Override
	protected void setNewServer(T server) {
		serverDefinition.setNewServer(server);
	}

	@Override
	public Flag<T> getServerFlag() {
		return serverDefinition.getServerFlag();
	}

	/**
	 * Stops the server and any connection attempts being made.
	 * 
	 * Blocks while waiting for server and connection thread to be shut down.
	 */
	@Override
	public void stopServer() {
		// Remove listener that would cause reconnects
		serverDefinition.getServerFlag().removeListener(serverListener);

		// If reconnecting
		if (startServerThread != null && startServerThread.isAlive()) {
			// Stop any ongoing reconnects and wait for them to finish.
			startServerThread.cancel();
		}

		// Clear server thread.
		startServerThread = null;

		// If server is connected
		serverDefinition.stopServer();
	}

	/**
	 * Nonblocking implementation.
	 */
	@Override
	public void startServer() {
		startServer(serverDefinition.getUri());
	}

	private void startServer(URI uri) {
		// Stop server and connection attempts first if any.
		stopServer();

		// Add listener to ensure server connects on connection loss.
		this.serverDefinition.getServerFlag().addListener(serverListener);

		// Start new task.
		startServerThread = new StartServerThread(uri);
		startServerThread.start();
	}

	private class StartServerThread extends Thread {

		/**
		 * Time to wait before trying to reconnect. 15 seconds.
		 */
		private static final long RECONNECTION_DELAY = 15 * 1000;

		private boolean tryAgain = true;
		private boolean connected = false;
		private final URI serverAddres;

		public StartServerThread(URI serverAddres) {
			super("ReconnectingServer: " + serverAddres);
			this.serverAddres = serverAddres;
		}

		@Override
		public void run() {

			while (!connected && tryAgain) {
				try {
					serverDefinition.setUri(serverAddres);
					connected = true;
				} catch (PogamutException pogamutException) {
					try {
						Thread.sleep(RECONNECTION_DELAY);
					} catch (InterruptedException interruptedException) {
						// interruptedException.printStackTrace();
					}
				}
			}

		}

		/**
		 * Stops the task from trying again.
		 * 
		 * @return
		 */
		public void cancel() {
			tryAgain = false;
		}

		@SuppressWarnings("unused")
		public boolean isCancelled() {
			return tryAgain;
		}

		@SuppressWarnings("unused")
		public boolean isDone() {
			return connected;
		}

	}

}
