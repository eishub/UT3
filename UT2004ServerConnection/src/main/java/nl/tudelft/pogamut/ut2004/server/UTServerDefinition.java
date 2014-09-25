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
package nl.tudelft.pogamut.ut2004.server;

import nl.tudelft.pogamut.unreal.server.UnrealServerDefinition;
import cz.cuni.amis.pogamut.ut2004.factory.direct.remoteagent.UT2004ServerFactory;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004ServerRunner;

/**
 * Definition of UnrealTournament2004 server.
 * 
 * @author ik
 */
public class UTServerDefinition extends UnrealServerDefinition<IUT2004Server> {


	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -5673456978655463816L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected IUT2004Server createServer() {
		UT2004ServerFactory factory = new UT2004ServerFactory();
		UT2004ServerRunner serverRunner = new UT2004ServerRunner(factory, "UTServer", getUri().getHost(), getUri().getPort());
		return serverRunner.startAgent();
	}

}
