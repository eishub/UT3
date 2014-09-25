package nl.tudelft.goal.unreal.translators;

import java.util.Map.Entry;
import java.util.logging.Level;

import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.BotParametersKey;
import nl.tudelft.goal.unreal.messages.ParameterMap;
import nl.tudelft.goal.ut2004.util.Skin;
import nl.tudelft.goal.ut2004.util.Team;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class BotParametersTranslator implements Parameter2Java<BotParameters> {

	@Override
	public BotParameters translate(Parameter parameter) throws TranslationException {
		Translator t = Translator.getInstance();

		ParameterMap parameterMap = t.translate2Java(parameter, ParameterMap.class);

		BotParameters parameters = new BotParameters();

		for (Entry<Identifier, Parameter> entry : parameterMap.entrySet()) {
			BotParametersKey key = t.translate2Java(entry.getKey(), BotParametersKey.class);
			Parameter value = entry.getValue();

			switch (key) {
			case LEADTARGET:
				parameters.setShouldLeadTarget(t.translate2Java(value, Boolean.class));
				break;
			case LOGLEVEL:
				parameters.setLogLevel(t.translate2Java(value, Level.class));
				break;
			case SKILL:
				parameters.setSkill(t.translate2Java(value, Integer.class));
				break;
			case SKIN:
				parameters.setSkin(t.translate2Java(value, Skin.class));
				break;
			case STARTLOCATION: 
				parameters.setInitialLocation(t.translate2Java(value, Location.class));
				break;
			case STARTROTATION:
				parameters.setInitialRotation(t.translate2Java(value, Rotation.class));
				break;
			case TEAM:
				parameters.setTeam(t.translate2Java(value, Team.class));
				break;
			case NAME: 
				parameters.setAgentId(t.translate2Java(value, String.class));
				break;
			}
		}

		return parameters;

	}

	@Override
	public Class<BotParameters> translatesTo() {
		return BotParameters.class;
	}

}
