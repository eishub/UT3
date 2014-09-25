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
package nl.tudelft.pogamut.unreal.server;

import nl.tudelft.pogamut.base.server.ServerDefinition;
import cz.cuni.amis.pogamut.base.agent.state.level0.IAgentState;
import cz.cuni.amis.pogamut.base.agent.state.level2.IAgentStateFailed;
import cz.cuni.amis.pogamut.base.agent.state.level2.IAgentStateStopped;
import cz.cuni.amis.pogamut.base.communication.command.ICommandListener;
import cz.cuni.amis.pogamut.unreal.server.IUnrealServer;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.ChangeMap;
import cz.cuni.amis.utils.exception.PogamutException;
import cz.cuni.amis.utils.flag.FlagListener;

/**
 * Definition of UnrealTournament2004 server.
 * 
 * @author ik
 */
@SuppressWarnings("rawtypes")
public abstract class UnrealServerDefinition<T extends IUnrealServer> extends
		ServerDefinition<T> {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -2167034240278963009L;
	transient ICommandListener<ChangeMap> mapChangeListener = null;
	protected transient T server = null;

	@Override
	public void startServer() throws PogamutException {
		// kill old server
		if (server != null
				&& !(server.getState().getFlag().isState(
						IAgentStateStopped.class, IAgentStateFailed.class))) {
			server.stop();
		}
		server = createServer();
		// Subscribe to new server.

		server.getState().addListener(new FlagListener<IAgentState>() {

			@Override
			public void flagChanged(IAgentState state) {
				if (state.isState(IAgentStateStopped.class,
						IAgentStateFailed.class)) {
					setNewServer(null);
				}
			}
		});
		setNewServer(server); // notify listeners
	}

	/**
	 * Creates a new server. Server is assumed to be up and running before it is returned or null if creating a server failed.
	 * 
	 * @return
	 */
	protected abstract T createServer();

	protected void serverStopped(T server) {

	}

	@Override
	public void stopServer() {
		if (server != null) {
			server.stop();
			server = null; // dereference.
			setNewServer(null); // notify listeners
		}
	}

	// /**
	// * Starts Unreal viewer of the server.
	// */
	// public void spectate() throws PogamutException {
	// URI uri = getUriFlag().getFlag();
	// if(uri == null) throw new PogamutException("Could not start viewer because the server URI isn't set.", this);
	// try {
	// startSpectImpl(uri);
	// } catch (IOException ex) {
	// throw new PogamutException("Viewer start failed.", ex);
	// }
	// }

	// protected abstract void startSpectImpl(URI uri) throws IOException;
}
