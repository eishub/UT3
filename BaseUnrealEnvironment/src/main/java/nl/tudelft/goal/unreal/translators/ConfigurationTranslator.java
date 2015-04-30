package nl.tudelft.goal.unreal.translators;

import java.net.URI;
import java.util.Map.Entry;
import java.util.logging.Level;

import nl.tudelft.goal.unreal.messages.BotParametersList;
import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.unreal.messages.ConfigurationKey;
import nl.tudelft.goal.unreal.messages.ParameterMap;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class ConfigurationTranslator implements Parameter2Java<Configuration> {

	@Override
	public Configuration translate(Parameter parameter)
			throws TranslationException {
		Translator t = Translator.getInstance();

		ParameterMap parameterMap = t.translate2Java(parameter,
				ParameterMap.class);

		Configuration configuration = new Configuration();

		for (Entry<Identifier, Parameter> entry : parameterMap.entrySet()) {
			ConfigurationKey key = t.translate2Java(entry.getKey(),
					ConfigurationKey.class);
			Parameter value = entry.getValue();

			switch (key) {
			case BOTS:
				configuration.setBots(t.translate2Java(value,
						BotParametersList.class));
				break;
			case NATIVE_BOTS:
				configuration.setNativeBots(t.translate2Java(value,
						BotParametersList.class));
				break;
			case BOT_SERVER:
				configuration.setBotServer(t.translate2Java(value, URI.class));
				break;
			case LOGLEVEL:
				configuration.setLogLevel(t.translate2Java(value, Level.class));
				break;
			case VISUALIZER_SERVER:
				configuration.setVisualizer(t.translate2Java(value, URI.class));
				break;
			case CONTROL_SERVER:
				configuration.setControlServer(t.translate2Java(value,
						URI.class));
				break;
			}
		}
		return configuration;

	}

	@Override
	public Class<Configuration> translatesTo() {
		return Configuration.class;
	}

}
