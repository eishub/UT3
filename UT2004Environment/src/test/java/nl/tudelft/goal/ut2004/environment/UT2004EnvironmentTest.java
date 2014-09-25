package nl.tudelft.goal.ut2004.environment;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cz.cuni.amis.pogamut.base.utils.Pogamut;
import cz.cuni.amis.pogamut.ut2004.utils.UCCWrapper;
import cz.cuni.amis.pogamut.ut2004.utils.UCCWrapperConf;
import eis.exceptions.ManagementException;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

public class UT2004EnvironmentTest {

	static UCCWrapper uccWrapper;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		UCCWrapperConf configuration = new UCCWrapperConf();
		configuration.setGameType("BotCTFGame");
		configuration.setMapName("CTF-Chrome");
		
		 uccWrapper = new UCCWrapper(configuration);
		 
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(uccWrapper != null)uccWrapper.stop();
		Pogamut.getPlatform().close();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ManagementException, InterruptedException {
		UT2004Environment environment = new UT2004Environment();
		HashMap<String, Parameter> map = new HashMap<String, Parameter>();
		map.put("visualizer", new Identifier("rmi://127.0.0.1:1099"));
		map.put("botServer", new Identifier("ut://127.0.0.1:" + uccWrapper.getBotPort()));
		map.put("controlServer", new Identifier("ut://127.0.0.1:" + uccWrapper.getControlPort()));
		map.put("logLevel", new Identifier("WARNING"));
		map.put("bots", new ParameterList(	
								new ParameterList(	new ParameterList(new Identifier("name"),new Identifier("RedLeader")),
													new ParameterList(new Identifier("team"),new Identifier("red")),
													new ParameterList(new Identifier("skill"),new Numeral(5)),
													new ParameterList(new Identifier("startLocation"),new Function("location", new Numeral(1),new Numeral(1),new Numeral(1))),
													new ParameterList(new Identifier("startRotation"),new Function("rotation", new Numeral(1),new Numeral(1),new Numeral(1))),
													new ParameterList(new Identifier("logLevel"), new Identifier("OFF"))),
								new ParameterList(	new ParameterList(new Identifier("name"),new Identifier("RedFive")),
													new ParameterList(new Identifier("team"),new Identifier("red")),
													new ParameterList(new Identifier("skill"),new Numeral(5)),
													new ParameterList(new Identifier("startLocation"),new Function("location", new Numeral(1),new Numeral(1),new Numeral(1))),
													new ParameterList(new Identifier("startRotation"),new Function("rotation", new Numeral(1),new Numeral(1),new Numeral(1))),
													new ParameterList(new Identifier("logLevel"), new Identifier("OFF")))));

		
		environment.init(map);
		
		
		environment.pause();
		
		
		environment.start();
		
		
		environment.kill();
		
	}
}
