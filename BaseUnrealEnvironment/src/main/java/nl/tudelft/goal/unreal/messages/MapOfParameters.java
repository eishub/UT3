package nl.tudelft.goal.unreal.messages;

import java.util.Map;
import java.util.Map.Entry;

import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

public class MapOfParameters extends ParameterList {

	/**
	 * Date of last modification.
	 */
	private static final long serialVersionUID = 201311261751L;

	public MapOfParameters(Map<String, Parameter> parameters) {
		for(Entry<String,Parameter> entry : parameters.entrySet()){
			this.add(new ParameterList(new Identifier(entry.getKey()) , entry.getValue() ));
		}
	
	}

}
