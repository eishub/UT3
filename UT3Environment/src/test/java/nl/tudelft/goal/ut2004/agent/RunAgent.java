package nl.tudelft.goal.ut2004.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import nl.tudelft.goal.unreal.messages.BotParameters;
import nl.tudelft.goal.unreal.messages.Configuration;
import nl.tudelft.goal.unreal.translators.LocationTranslator;
import nl.tudelft.goal.unreal.translators.NoneTranslator;
import nl.tudelft.goal.unreal.translators.PerceptTranslator;
import nl.tudelft.goal.unreal.translators.RotationTranslator;
import nl.tudelft.goal.unreal.translators.TeamTranslator;
import nl.tudelft.goal.unreal.translators.UnrealIdTranslator;
import nl.tudelft.goal.unreal.translators.VelocityTranslator;
import nl.tudelft.goal.ut3.agent.UT3BotBehavior;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import nl.tudelft.goal.ut3.translators.CategoryTranslator;
import nl.tudelft.goal.ut3.translators.FireModeTranslator;
import nl.tudelft.goal.ut3.translators.FlagStateTranslator;
import nl.tudelft.goal.ut3.translators.GameTypeTranslator;
import nl.tudelft.goal.ut3.translators.ItemTypeTranslator;
import nl.tudelft.goal.ut3.translators.NavigationStateTranslator;
import nl.tudelft.goal.ut3.translators.SelectorListTranslator;
import nl.tudelft.goal.ut3.translators.SelectorTranslator;
import nl.tudelft.goal.ut3.translators.UT3ItemTypeTranslator;
import nl.tudelft.goal.unreal.translators.UnrealIdOrLocationTranslator;
import nl.tudelft.goal.ut3.translators.WeaponPrefListTranslator;
import nl.tudelft.goal.ut3.translators.WeaponPrefTranslator;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuniz.amis.pogamut.ut3.utils.UT3BotRunner;
import eis.eis2java.handlers.ActionHandler;
import eis.eis2java.handlers.AllPerceptPerceptHandler;
import eis.eis2java.handlers.DefaultActionHandler;
import eis.eis2java.handlers.PerceptHandler;
import eis.eis2java.translation.Translator;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;
import java.util.LinkedList;
import nl.tudelft.goal.ut3.messages.SelectorList;
import nl.tudelft.goal.ut3.selector.ContextSelector;
import nl.tudelft.goal.ut3.selector.NearestFriendlyWithLinkGun;

/**
 * Test class to make the agent do custom stuff.
 *
 * @author mpkorstanje
 *
 */
@SuppressWarnings("rawtypes")
public class RunAgent {

    public static void main(String[] args) throws EntityException, InterruptedException, PerceiveException {

        Translator translator = Translator.getInstance();

        /*
         * Translators provided by the BaseUnrealEnvironment.
         * 
         * Please list these in lexical order.
         */

        LocationTranslator locationTranslator = new LocationTranslator();
        translator.registerJava2ParameterTranslator(locationTranslator);
        translator.registerParameter2JavaTranslator(locationTranslator);

        /*
         * To translate from Parameter2Java we are given an UnrealId. However we
         * can not access the agents memory during translation. To work around
         * this we store everything we have send to any agent. Hence the same
         * object has to be used for both directions.
         */

        RotationTranslator rotationTranslator = new RotationTranslator();
        translator.registerJava2ParameterTranslator(rotationTranslator);
        translator.registerParameter2JavaTranslator(rotationTranslator);

        TeamTranslator teamTranslator = new TeamTranslator();
        translator.registerJava2ParameterTranslator(teamTranslator);
        translator.registerParameter2JavaTranslator(teamTranslator);

        UnrealIdTranslator unrealIdTranslator = new UnrealIdTranslator();
        translator.registerJava2ParameterTranslator(unrealIdTranslator);
        translator.registerParameter2JavaTranslator(unrealIdTranslator);

        VelocityTranslator velocityTranslator = new VelocityTranslator();
        translator.registerJava2ParameterTranslator(velocityTranslator);
        translator.registerParameter2JavaTranslator(velocityTranslator);
        /*
         * Translators provided by the UT2004 environment.
         * 
         * Please list these in lexical order.
         */

        // UT3 TRANSLATORS

        UT3ItemTypeTranslator ut3CategoryTranslator = new UT3ItemTypeTranslator();
        translator.registerJava2ParameterTranslator(ut3CategoryTranslator);
        translator.registerParameter2JavaTranslator(ut3CategoryTranslator);

        CategoryTranslator categoryTranslator = new CategoryTranslator();
        translator.registerJava2ParameterTranslator(categoryTranslator);
        translator.registerParameter2JavaTranslator(categoryTranslator);

        ItemTypeTranslator itemTypeTranslator = new ItemTypeTranslator();
        translator.registerJava2ParameterTranslator(itemTypeTranslator);

        FireModeTranslator fireModeTranslator = new FireModeTranslator();
        translator.registerJava2ParameterTranslator(fireModeTranslator);
        translator.registerParameter2JavaTranslator(fireModeTranslator);

        FlagStateTranslator flagStateTranslator = new FlagStateTranslator();
        translator.registerJava2ParameterTranslator(flagStateTranslator);

        GameTypeTranslator gameTypeTranslator = new GameTypeTranslator();
        translator.registerJava2ParameterTranslator(gameTypeTranslator);

        NavigationStateTranslator navigationStateTranslator = new NavigationStateTranslator();
        translator.registerJava2ParameterTranslator(navigationStateTranslator);

        NoneTranslator noneTranslator = new NoneTranslator();
        translator.registerJava2ParameterTranslator(noneTranslator);

        PerceptTranslator perceptTranslator = new PerceptTranslator();
        translator.registerJava2ParameterTranslator(perceptTranslator);

        SelectorListTranslator selectorListTranslator = new SelectorListTranslator();
        translator.registerParameter2JavaTranslator(selectorListTranslator);

        SelectorTranslator selectorTranslator = new SelectorTranslator();
        translator.registerParameter2JavaTranslator(selectorTranslator);

        UnrealIdOrLocationTranslator unrealIdOrLocationTranslator = new UnrealIdOrLocationTranslator();
        translator.registerParameter2JavaTranslator(unrealIdOrLocationTranslator);

        WeaponPrefListTranslator weaponPrefListTranslator = new WeaponPrefListTranslator();
        translator.registerParameter2JavaTranslator(weaponPrefListTranslator);

        WeaponPrefTranslator weaponPrefTranslator = new WeaponPrefTranslator();
        translator.registerParameter2JavaTranslator(weaponPrefTranslator);

        UT3BotRunner<UT2004Bot, UT2004BotParameters> runner = new UT3BotRunner<UT2004Bot, UT2004BotParameters>(
                UT3BotBehavior.class, Configuration.DEFAULT_BOT_NAME, "127.0.0.1", 3000);
        runner.setConsoleLogging(true);

        //AgentLogger log = new AgentLogger(new AgentId("Test"));
        BotParameters parameters = new BotParameters();
        parameters.setAgentId("SimpleRed 1");
        parameters.setSkill(7);
        parameters.setLogLevel(Level.INFO);
//		// parameters.setSkin(Skin.BotA);
//		// parameters.setTeam(Team.RED);
//		parameters.assignDefaults(BotParameters.getDefaults(log));
//		BotParameters parameters1 = new BotParameters(log);
//		parameters1.setAgentId("SimpleRed 2");
//		// parameters.setSkin(Skin.BotA);
//		// parameters.setTeam(Team.RED);
//		parameters.assignDefaults(BotParameters.getDefaults(log));
//		BotParameters parameters2 = new BotParameters(log);
//		parameters2.setAgentId("SimpleRed 3");
//		// parameters.setSkin(Skin.BotA);
//		// parameters.setTeam(Team.RED);
//		parameters2.assignDefaults(BotParameters.getDefaults(log));

        //List<UT2004Bot> agents = runner.startAgents(parameters, parameters1, parameters2);
        List<UT2004Bot> agents = runner.startAgents(parameters);

        List<PerceptHandler> handlers = new ArrayList<PerceptHandler>(agents.size());
        List<ActionHandler> actionHandlers = new ArrayList<ActionHandler>(agents.size());
        SelectorList targets = new SelectorList();
        
        for (UT2004Bot agent : agents) {
            PerceptHandler handler = new AllPerceptPerceptHandler((AllPerceptsProvider) agent.getController());
            handlers.add(handler);
        }

		for (UT2004Bot agent : agents) {
			ActionHandler handler = new DefaultActionHandler(agent.getController());
			actionHandlers.add(handler);
		}
               
                UT3BotBehavior behavior = ((UT3BotBehavior) agents.get(0).getController());
             //   targets.add(new NearestFriendlyWithLinkGun());
           //    behavior.shoot(targets);
             //    behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
             //   behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponPickupFactory_4")));
            //    behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponPickupFactory_0")));
               behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTDeployablePickupFactory_0")));
            //   behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTAmmo_BioRifle_Content_0")));
               // behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTPickupFactory_UDamage_0")));
             //   behavior.navigate(new UnrealIdOrLocation(UnrealId.get("PathNode_21")));
                boolean finalDesination = false, droppedDeployable = false;
		while (true) {
			Thread.sleep(500);
                        
                        // Display the weapon percepts
                        // TODO: Build tests to check if the type of the weapon is OK
		/*	for (PerceptHandler handler : handlers) {
				LinkedList<Percept> list = handler.getAllPercepts();
                                for(Percept p : list)
                                {
                                        if(p.getName().equals("item"))
                                                System.out.println(p);
                                }
			}*/

                  /*       for(NavPoint n : behavior.getWorldView().getAll(NavPoint.class).values())
                         {
                                 
                                 if(ItemType.getItemType(n.getVolume()) == UT3ItemType.SLOW_VOLUME)
                                        System.out.println(n);
                         }*/
                       // System.out.println("Ammo enforcer: " + behavior.getWeaponry().getAmmo(UT3ItemType.ENFORCER));
                        
                       if(!behavior.getNavigation().isNavigating()) {
                               
                               if(finalDesination && !droppedDeployable) {
                                        behavior.deploy();
                                        droppedDeployable = true;
                                }
                                else
                                {
                                       // behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTCTFBlueFlagBase_0")));
                                       // behavior.navigate(new UnrealIdOrLocation(UnrealId.get("UTPickupFactory_Berserk_0")));
                                        finalDesination = true;
                                }

                        }
                        
                      /*  for (UT3Weapon weap : behavior.getWeaponry().getWeapons().values()) {        
                           System.out.println(weap.getType() + " ammo " + behavior.getWeaponry().getAmmo(weap.getType()) + " (max: " + behavior.getWeaponry().getMaxAmmo(weap.getType()) + ")");
                        }*/

                  /*      if(behavior.getInfo().hasPowerUp())
                        {
                                System.out.println("Has POWERUP: " + behavior.getInfo().getPowerUp());
                                System.out.println("Time: " + behavior.getInfo().getPowerUpTime());
                        }*/
                       
                 /*      for (PerceptHandler handler : handlers) {
				LinkedList<Percept> list = handler.getAllPercepts();
                                for(Percept p : list)
                                {
                                        if(p.getName().equals("status"))
                                                System.out.println(p);
                                }
			}*/
                       
                        // Display the weapon percepts
                        // TODO: Build tests to check if the type of the weapon is OK
                      for (PerceptHandler handler : handlers) {
				LinkedList<Percept> list = handler.getAllPercepts();
                                for(Percept p : list)
                                {
                                        if(p.getName().equals("navigation"))
                                                System.out.println(p);
                                }
			}
                      
                      ArrayList<UnrealId> list = behavior.getVisibility().getVisibleVolumeNavPoints(UT3ItemType.SLOW_VOLUME_CONTENT);
                      System.out.println("==== START ====");
                      for(int i = 0; i < list.size(); i++)
                      {
                              System.out.println("Id: " + list.get(i));
                      }
                      System.out.println("==== END ====");
                      
                        
                  /*     Map<Integer, FlagInfo> map = behavior.getGame().getAllCTFFlags();
                        for(Map.Entry<Integer, FlagInfo> entry : map.entrySet()) {
                                System.out.println(entry.getValue());
                        }*/
                                
                 //       System.out.println(behavior.getWeaponry().getCurrentWeapon());
			
		//	behavior.shoot(new SelectorList(new NearestEnemy()));
                        

            /*     Map<Integer, FlagInfo> map = behavior.getGame().getAllCTFFlags();
             for(Map.Entry<Integer, FlagInfo> entry : map.entrySet()) {
             System.out.println(entry.getValue());
             }*/

            //       System.out.println(behavior.getWeaponry().getCurrentWeapon());

            //	behavior.shoot(new SelectorList(new NearestEnemy()));


            //behavior.navigate(new UnrealIdOrLocation(behavior.getInfo().getId()));

            //List<NavPoint> navs = new ArrayList<NavPoint>(behavior.getWorld().getAll(NavPoint.class).values());
//			
            //int first = new Random().nextInt(navs.size());
//			int second = new Random().nextInt(navs.size());
//			
//			
//			System.out.println(behavior.getInfo().getSelf());

            //behavior.respawn();

            //System.out.println(behavior.path(new UnrealIdOrLocation(navs.get(first).getId()), new UnrealIdOrLocation( navs.get(second).getId())));

            //behavior.look(new SelectorList(new PlayerOrNavpoint(navs.get(first).getId())));
//			Player p =  behavior.getPlayers().getNearestVisiblePlayer();
//			if(p != null)
//				behavior.look(new SelectorList(new PlayerOrNavpoint(p.getId())));

//			Map<UnrealId, Item> items = behavior.getItems().getAllItems(ItemType.SUPER_SHIELD_PACK);
//			Item nearest = DistanceUtils.getNearest(items.values(), behavior.getInfo().getLocation());
//			
//			if(!behavior.getInfo().hasHighArmor()){
//				behavior.navigate(new UnrealIdOrLocation(nearest.getId()));
//			} else {
//				behavior.navigate(new UnrealIdOrLocation(navs.get(1).getId()));
//			}

            /*	UnrealIdOrLocation id = new UnrealIdOrLocation(UnrealId.get("CTF-UG-Chrome.xRedFlagBase1"));
             UnrealIdOrLocation id2 = new UnrealIdOrLocation(UnrealId.get("CTF-UG-Chrome.PathNode236"));
			
             if(!behavior.getNavigation().isNavigating()){
             if(!behavior.getInfo().isAtLocation(behavior.getGame().getFlagBase(0))){
             behavior.navigate(id);
             } else {
             behavior.navigate(id2);
             }
             }*/
        }

    }
}
