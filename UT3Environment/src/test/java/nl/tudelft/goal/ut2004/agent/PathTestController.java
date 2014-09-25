package nl.tudelft.goal.ut2004.agent;

import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.ut2004.bot.IUT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotModuleController;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPointNeighbourLink;
import cz.cuni.amis.pogamut.ut2004.communication.translator.shared.events.MapPointListObtained;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.exception.PogamutException;

/**
 * Test controller to print specific edges in the map.
 * 
 * @author mpkorstanje
 * 
 */
@SuppressWarnings("rawtypes")
public class PathTestController extends UT2004BotModuleController<UT2004Bot> {

	public PathTestController() {
		super();
	}

	protected void initializeModules(UT2004Bot bot) {
		super.initializeModules(bot);
		
		getWorldView().addEventListener(MapPointListObtained.class, mapListener);

	
	}

	protected void initializeWeaponShootings() {
		
	}

	@Override
	public void finishControllerInitialization() {
		super.finishControllerInitialization();

	}

	@Override
	public Initialize getInitializeCommand() {
		return super.getInitializeCommand();

	}

	@Override
	public void beforeFirstLogic() {
	}

	@Override
	public void logic() throws PogamutException {

		
	}
	

	private IWorldEventListener<MapPointListObtained> mapListener = new IWorldEventListener<MapPointListObtained>() {

		@Override
		public void notify(MapPointListObtained event) {
			for(NavPoint n:  event.getNavPoints().values()){
				for(NavPointNeighbourLink edge : n.getOutgoingEdges().values()){
	                if (edge.getNeededJump() != null && edge.getNeededJump().z > 680) {
						System.out.println(n);;
						System.out.println(edge);;
					}
				}
			}
			
		}
	};

	public static void main(String[] args) {
		new UT2004BotRunner<IUT2004Bot, UT2004BotParameters>(PathTestController.class).setMain(true).startAgent();
	}

}