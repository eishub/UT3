/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.goal.ut2004.visualizer.map;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import nl.tudelft.goal.ut2004.visualizer.timeline.map.ISubGLRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.PlayerRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.FlagRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.WaypointRenderer;
import cz.cuni.amis.pogamut.base.communication.worldview.object.WorldObjectId;
import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealMap;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import cz.cuni.amis.pogamut.unreal.server.IUnrealServer;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;
import cz.cuni.amis.utils.collections.CollectionEventListener;
import javax.swing.SwingUtilities;

/**
 * Simple map that overviews what is happening in the level right now. It
 * listens for changes in server.getAgents() and server.getNativeBots() for
 * adding and removing bots from map.
 *
 * This is intended for one map only, it is not intended to handle change from
 * one map to another, simply discard this, call destroy() and create a new one.
 *
 * This is panel that shows one particular map. After you are finished with
 * using the class, please call destroy() to remove listeners, stops redrawing
 * and do some other clean up.
 *
 * @author Honza
 */
public class PureMapGLPanel extends SelectableMapGLPanel implements
        CollectionEventListener<IPlayer> {

    /**
     * Server on which we listen for changes in agents and bots
     */
    protected IUnrealServer server;
    // Timer used to redraw this panel.
    private Timer timer;
    // How often should map be redrawn, be careful, because of traffic overhead,
    // we get the data from somewhere in the net.
    private final int REDRAW_DELAY = 250;

    protected PureMapGLPanel(IUnrealMap map, IUnrealServer server) {
        super(map, Logger.getLogger("PureMapGLPanel"));

        this.server = server;

        // add all found agents in the map
        for (Object agent : server.getPlayers()) {
            addAgentRenderer((IPlayer) agent);
        }

        // add all objects in the map
        Map<WorldObjectId, FlagInfo> flags = server.getWorldView().getAll(
                FlagInfo.class);
        for (FlagInfo flag : flags.values()) {
            addFlagRenderer(flag);
        }

        Collection<IUnrealWaypoint> navs = map.vertexSet();
        for (IUnrealWaypoint waypoint : navs) {
            addWaypointRenderer(waypoint);
        }

        // add listeners so I can update agents
        server.getPlayers().addCollectionListener(this);

    }
    static int called = 0;

    /**
     * Start display loop
     */
    public synchronized void startDisplayLoop() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (timer == null) {
                    timer = new java.util.Timer("Overview map redrawer");
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            display();
                        }
                    }, REDRAW_DELAY, REDRAW_DELAY);
                }
            }
        });
    }

    /**
     * Stop display loop
     */
    public synchronized void stopDisplayLoop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Create a renderable representation of agent and add it to renderers.
     *
     * @param agent Agent that will be added to drawn agents
     */
    private void addAgentRenderer(IPlayer agent) {
        agentRenderes.addRenderer(new PlayerRenderer(agent, lastGLName++));
    }

    private void removeAgentRenderer(IPlayer agent) {
        agentRenderes.removeRenderersOf(agent);
    }

    private void addFlagRenderer(FlagInfo flag) {
        ISubGLRenderer<FlagInfo> subRenderer = new FlagRenderer(flag, lastGLName++);
        objectRenderes.addRenderer(subRenderer);
    }

    private void addWaypointRenderer(IUnrealWaypoint waypoint) {
        ISubGLRenderer<IUnrealWaypoint> subRenderer = new WaypointRenderer(waypoint, lastGLName++);
        objectRenderes.addRenderer(subRenderer);
    }

    /**
     * Do nothing.
     *
     * @param toBeAdded
     * @param whereToAdd
     */
    @Override
    public void preAddEvent(Collection<IPlayer> toBeAdded,
            Collection<IPlayer> whereToAdd) {
    }

    /**
     * Add renderers representing the agents to the map.
     *
     * @param alreadyAdded
     * @param whereWereAdded
     */
    @Override
    public synchronized void postAddEvent(Collection<IPlayer> alreadyAdded,
            Collection<IPlayer> whereWereAdded) {
        for (IPlayer agent : alreadyAdded) {
            addAgentRenderer(agent);
        }
    }

    /**
     * Remove renderers that represented the removed agents from the map
     *
     * @param toBeRemoved
     * @param whereToRemove
     */
    @Override
    public synchronized void preRemoveEvent(Collection<IPlayer> toBeRemoved,
            Collection<IPlayer> whereToRemove) {
        for (IPlayer removedAgent : toBeRemoved) {
            removeAgentRenderer(removedAgent);
        }
    }

    /**
     * Do nothing
     *
     * @param alreadyAdded
     * @param whereWereRemoved
     */
    @Override
    public void postRemoveEvent(Collection<IPlayer> alreadyAdded,
            Collection<IPlayer> whereWereRemoved) {
    }

    @Override
    public synchronized void destroy() {
        if (timer != null) {
            timer.cancel();
        }

        server.getAgents().removeCollectionListener(this);
        server.getNativeAgents().removeCollectionListener(this);

        server = null;

        super.destroy();
    }
}
