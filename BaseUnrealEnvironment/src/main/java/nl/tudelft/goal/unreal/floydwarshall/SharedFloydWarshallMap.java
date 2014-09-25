package nl.tudelft.goal.unreal.floydwarshall;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.amis.pogamut.base.agent.IGhostAgent;
import cz.cuni.amis.pogamut.base.agent.module.SensorModule;
import cz.cuni.amis.pogamut.base.agent.navigation.IPathFuture;
import cz.cuni.amis.pogamut.base.agent.navigation.IPathPlanner;
import cz.cuni.amis.pogamut.base.agent.navigation.impl.PrecomputedPathFuture;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPointNeighbourLink;
import cz.cuni.amis.pogamut.ut2004.communication.translator.shared.events.MapPointListObtained;
import cz.cuni.amis.utils.collections.MyCollections;

/**
 * Wrapper for a FloyWarschallMap shared by multiple agents.
 *
 * @author M.P. Korstanje
 */
public class SharedFloydWarshallMap extends SensorModule<IGhostAgent> implements IPathPlanner<NavPoint> {

    /**
     * Prohibited edges.
     */
    protected int badEdgeFlag = 0;
    /**
     * Our map
     */
    protected FloydWarshallMap sharedMap = null;
    private IWorldEventListener<MapPointListObtained> mapListener = new IWorldEventListener<MapPointListObtained>() {
        @Override
        public void notify(MapPointListObtained event) {
            if (log.isLoggable(Level.INFO)) {
                log.info("Map point list obtained.");
            }
            sharedMap = FloydWarshallMapCache.getInstance().createMap(event, badEdgeFlag, log);
        }
    };

    public SharedFloydWarshallMap(IGhostAgent bot) {
        this(bot, null);
    }

    public SharedFloydWarshallMap(IGhostAgent bot, Logger log) {
        this(bot, FloydWarshallMap.BAD_EDGE_FLAG, log);
    }

    public SharedFloydWarshallMap(IGhostAgent bot, int badEdgeFlag, Logger log) {
        super(bot, log);
        this.badEdgeFlag = badEdgeFlag;
        worldView.addEventListener(MapPointListObtained.class, mapListener);
    }

    /**
     * Transforms a list of navpoints owned by the (bot owning the) shared map
     * to navpoints that we own. This is important with regards to visibility.
     *
     * @param shared
     * @return
     */
    private List<NavPoint> clean(List<NavPoint> shared) {

        //Empty path.
        if (shared == null) {
            return null;
        }

        List<NavPoint> clean = new ArrayList<NavPoint>(shared.size());

        for (NavPoint navpoint : shared) {
            clean.add(clean(navpoint));
        }

        return clean;
    }

    /**
     * Transforms a navpoint owned by the (bot owning the) shared map to
     * navpoints that we own. This is important with regards to visibility.
     *
     * @param shared
     * @return
     */
    private NavPoint clean(NavPoint shared) {
        return worldView.get(shared.getId(), NavPoint.class);
    }

    /**
     * Returns path between navpoints 'from' -> 'to'. The path begins with
     * 'from' and ends with 'to'. If such path does not exist, it returns
     * zero-sized path.
     * <p>
     * <p>
     * Throws exception if object is disabled, see
     * {@link FloydWarshallMap#setEnabled(boolean)}. Note that the object is
     * enabled by default.
     *
     * @param from
     * @param to
     * @return
     */
    @Override
    public IPathFuture<NavPoint> computePath(NavPoint from, NavPoint to) {
        return new PrecomputedPathFuture<NavPoint>(from, to, getPath(from, to));
    }

    /**
     * Whether navpoint 'to' is reachable from the navpoint 'from'.
     * <p>
     * <p>
     * Throws exception if object is disabled, see
     * {@link FloydWarshallMap#setEnabled(boolean)}. Note that the object is
     * enabled by default.
     *
     * @param from
     * @param to
     * @return
     */
    public boolean reachable(NavPoint from, NavPoint to) {
        return sharedMap.reachable(from, to);
    }

    /**
     * Calculate's distance between two nav points (using pathfinding).
     * <p>
     * <p>
     * Throws exception if object is disabled, see
     * {@link FloydWarshallMap#setEnabled(boolean)}. Note that the object is
     * enabled by default.
     *
     * @return Distance or POSITIVE_INFINITY if there's no path.
     */
    public float getDistance(NavPoint from, NavPoint to) {
        return sharedMap.getDistance(from, to);
    }

    /**
     * Returns path between navpoints 'from' -> 'to'. The path begins with
     * 'from' and ends with 'to'. If such path doesn't exist, returns null.
     * <p>
     * <p>
     * Throws exception if object is disabled, see
     * {@link FloydWarshallMap#setEnabled(boolean)}. Note that the object is
     * enabled by default.
     *
     * @param from
     * @param to
     * @return
     */
    public List<NavPoint> getPath(NavPoint from, NavPoint to) {
        return clean(sharedMap.getPath(from, to));
    }

    /**
     * Checks whether the edge is usable.
     *
     * @param from Starting nav point.
     * @param edge NeighNav object representing the edge.
     * @return boolean
     */
    public boolean checkLink(NavPointNeighbourLink edge) {
        return sharedMap.checkLink(edge);

    }

    /**
     * Hook where to perform clean up of data structures of the module.
     */
    @Override
    protected void cleanUp() {
        super.cleanUp();
        sharedMap = null;
    }

    public void refreshPathMatrix() {
        List<NavPoint> navPoints = MyCollections.asList(agent.getWorldView().getAll(NavPoint.class).values());
        sharedMap.refreshPathMatrix(navPoints);
    }
}
