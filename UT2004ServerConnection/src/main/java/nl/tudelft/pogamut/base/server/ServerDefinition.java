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

import java.io.Serializable;
import java.net.URI;

import cz.cuni.amis.utils.flag.Flag;

/**
 * Model object representing arbitrary server.
 * 
 * @author ik
 */

public abstract class ServerDefinition<SERVER> implements Serializable {
 
	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = 6864162146688965336L;
	String name = "Initialize server name";
	transient Flag<String> nameFlag = null;
	URI uri = null;
	transient Flag<URI> uriFlag = null;
	private transient Flag<SERVER> serverFlag = null;

	public ServerDefinition() {
		init();
	}

	private void init() {
		nameFlag = new Flag<String>(name);
		uriFlag = new Flag<URI>(uri); 
		serverFlag = new Flag<SERVER>(null);
		if (getUri() != null)
			startServer();
	}

	public void setServerName(String name) {
		this.name = name;
		nameFlag.setFlag(name);
	}

	public String getServerName() {
		return name;
	}

	public Flag<String> getServerNameFlag() {
		return nameFlag.getImmutable();
	}

	public void setUri(URI uri) {
		this.uri = uri;
		uriFlag.setFlag(uri);
		startServer();
	}

	public URI getUri() {
		return uri;
	}

	public Flag<URI> getUriFlag() {
		return uriFlag.getImmutable();
	}

	/**
	 * Change current server instance.
	 * 
	 * @param server
	 */
	protected void setNewServer(SERVER server) {
		serverFlag.setFlag(server);
	}

	public Flag<SERVER> getServerFlag() {
		return serverFlag.getImmutable();
	}

	/**
	 * Restarts the server eg. after address change.
	 */
	public abstract void startServer();

	/**
	 * Stops connection to the server. When the server is stopped, the serverFlag should be set to null.
	 */
	public abstract void stopServer();

}