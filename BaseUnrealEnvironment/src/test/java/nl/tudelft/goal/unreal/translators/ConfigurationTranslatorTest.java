package nl.tudelft.goal.unreal.translators;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;

import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.unreal.messages.MapOfParameters;

import org.junit.*;

import eis.eis2java.exception.NoTranslatorException;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

public class ConfigurationTranslatorTest {

	@BeforeClass
	public static void setUp() throws Exception {
		Translator translator = Translator.getInstance();

		translator.registerParameter2JavaTranslator(new BotParametersKeyTranslator());
		translator.registerParameter2JavaTranslator(new BotParametersListTranslator());
		translator.registerParameter2JavaTranslator(new BotParametersTranslator());

		translator.registerParameter2JavaTranslator(new ConfigurationKeyTranslator());
		translator.registerParameter2JavaTranslator(new ConfigurationTranslator());

		translator.registerParameter2JavaTranslator(new LevelTranslator());
		translator.registerParameter2JavaTranslator(new LocationTranslator());

		translator.registerParameter2JavaTranslator(new ParameterMapTranslator());
		translator.registerParameter2JavaTranslator(new RotationTranslator());

		translator.registerParameter2JavaTranslator(new SkinTranslator());
		translator.registerParameter2JavaTranslator(new TeamTranslator());

		translator.registerParameter2JavaTranslator(new UnrealIdTranslator());
		translator.registerParameter2JavaTranslator(new URITranslator());

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	public void test() throws NoTranslatorException, TranslationException, URISyntaxException{
		HashMap<String, Parameter> map = new HashMap<String, Parameter>();
		map.put("visualizer", new Identifier("rmi://127.0.0.1:1099"));
		map.put("botServer", new Identifier("ut://127.0.0.1:3000"));
		map.put("controlServer", new Identifier("ut://127.0.0.1:3001"));
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
		Parameter parameter = new MapOfParameters(map);
		Configuration configuration = Translator.getInstance().translate2Java(parameter, Configuration.class);
		
		assertEquals(new URI("rmi://127.0.0.1:1099"), configuration.getVisualizer());
		assertEquals(new URI("ut://127.0.0.1:3000"), configuration.getBotServer());
		assertEquals(new URI("ut://127.0.0.1:3001"), configuration.getControlServer());
		assertEquals(Level.WARNING, configuration.getLogLevel());
		
		

		
		
	}
}
