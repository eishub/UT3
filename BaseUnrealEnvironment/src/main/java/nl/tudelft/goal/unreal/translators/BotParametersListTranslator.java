package nl.tudelft.goal.unreal.translators;

import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.BotParametersList;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

public class BotParametersListTranslator implements Parameter2Java<BotParametersList> {

	
	@Override
	public  BotParametersList translate(Parameter parameter) throws TranslationException {
		if(!(parameter instanceof ParameterList)){
			throw new TranslationException("Expected a list of parameters bot got " +  parameter);
		}
		
		BotParametersList botParameters = new BotParametersList();
		for(Parameter p : (ParameterList) parameter){
			botParameters.add(Translator.getInstance().translate2Java(p, BotParameters.class));
		}
		
		return botParameters;
	}
	
	
	@Override
	public Class<BotParametersList> translatesTo() {
		return BotParametersList.class;
	}


	

}
