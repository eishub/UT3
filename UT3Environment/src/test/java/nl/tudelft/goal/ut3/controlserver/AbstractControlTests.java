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
package nl.tudelft.goal.ut3.controlserver;

import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.bot.IUT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerFactory;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerModule;
import cz.cuni.amis.pogamut.ut2004.server.impl.UT2004Server;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004ServerRunner;
import cz.cuni.amis.utils.collections.ObservableCollection;
import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests;
import nl.tudelft.goal.ut3.server.EnvironmentControllerServer;
import nl.tudelft.goal.ut3.server.EnvironmentControllerServerModule;
import org.junit.BeforeClass;

/**
 *
 * @author Evers
 */
public class AbstractControlTests extends AbstractEnvironmentTests {
        public static EnvironmentControllerServer utServer;
        
        private static UT2004ServerModule<UT2004AgentParameters> serverModule;
        private static UT2004ServerFactory<EnvironmentControllerServer, UT2004AgentParameters> serverFactory;
        private static UT2004ServerRunner<EnvironmentControllerServer, UT2004AgentParameters> serverRunner;
        
        @BeforeClass
        public static void initControlServer()
        {
                // start the control server
                serverModule = new EnvironmentControllerServerModule<UT2004AgentParameters>();
		serverFactory = new UT2004ServerFactory<EnvironmentControllerServer, UT2004AgentParameters>(
				serverModule);
		serverRunner = new UT2004ServerRunner<EnvironmentControllerServer, UT2004AgentParameters>(
				serverFactory, "UTServer",
				Configuration.LOCAL_HOST,
				Configuration.CONTROL_SERVER_PORT);
                utServer = serverRunner.startAgent();                    
        }
}
