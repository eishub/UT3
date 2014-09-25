package nl.tudelft.goal.unreal.floydwarshall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Point3d;

import cz.cuni.amis.pogamut.base.agent.navigation.IPathFuture;
import cz.cuni.amis.pogamut.base.agent.navigation.IPathPlanner;
import cz.cuni.amis.pogamut.base.agent.navigation.impl.PrecomputedPathFuture;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.NavigationGraphBuilder;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPointNeighbourLink;
import cz.cuni.amis.pogamut.ut2004.communication.translator.shared.events.MapPointListObtained;
import cz.cuni.amis.pogamut.ut2004.utils.LinkFlag;
import cz.cuni.amis.utils.collections.MyCollections;

/**
 * Private map using Floyd-Warshall for path-finding.
 * <p><p>
 * It should be initialized upon receiving {@link MapPointListObtained} event.
 * It precomputes all the paths inside the environment using Floyd-Warshall
 * algorithm (O(n^3)). Use getReachable(), getDistance(), getPath() to obtain
 * the info about the path.
 * <p><p>
 * If needed you may call {@link FloydWarshallMap#refreshPathMatrix()} to
 * recompute Floyd-Warshall. Especially useful if you're using
 * {@link NavigationGraphBuilder} to modify the underlying navpoints/edges.
 * <p><p>
 * Based upon the implementation from Martin Krulis with his kind consent -
 * Thank you!
 * <p><p>
 * NOTE: requires O(navpoints.size^3) of memory ~ which is 10000^3 at max for
 * UT2004 (usually the biggest maps have 3000 navpoints max, but small maps,
 * e.g., DM-1on1-Albatross has 200 navpoints).
 * <p><p>
 *
 * @author Martin Krulis
 * @author Jimmy
 * @author mpkorstanje
 */
public class FloydWarshallMap implements IPathPlanner<NavPoint> {

    public static class PathMatrixNode {

        private float distance = Float.POSITIVE_INFINITY;
        private Integer viaNode = null;
        private List<NavPoint> path = null;

        /**
         * Doesn't leading anywhere
         */
        public PathMatrixNode() {
        }

        public PathMatrixNode(float distance) {
            this.distance = distance;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        /**
         * Returns indice.
         *
         * @return
         */
        public Integer getViaNode() {
            return viaNode;
        }

        public void setViaNode(Integer indice) {
            this.viaNode = indice;
        }

        public List<NavPoint> getPath() {
            return path;
        }

        public void setPath(List<NavPoint> path) {
            this.path = path;
        }
    }
    /**
     * Flag mask representing unusable edge.
     */
    public static final int BAD_EDGE_FLAG = LinkFlag.FLY.get()
            | LinkFlag.LADDER.get() | LinkFlag.PROSCRIBED.get()
            | LinkFlag.SWIM.get() | LinkFlag.PLAYERONLY.get();

    public static boolean isWalkable(int flag) {
        return ((flag & BAD_EDGE_FLAG) == 0) && ((flag & LinkFlag.SPECIAL.get()) == 0);
    }
    /**
     * Prohibited edges.
     */
    protected int badEdgeFlag = 0;
    /**
     * Hash table converting navPoint IDs to our own indices.
     */
    protected Map<UnrealId, Integer> navPointIndices;
    /**
     * Mapping indices to nav points.
     * <p>
     * <p>
     * WILL BE NULL AFTER CONSTRUCTION! SERVES AS A TEMPORARY "GLOBAL VARIABLE"
     * DURING FLOYD-WARSHALL COMPUTATION AND PATH RECONSTRUCTION.
     */
    protected Map<Integer, NavPoint> indicesNavPoints;
    // Warshall's matrix of distances.
    protected PathMatrixNode[][] pathMatrix;
    /**
     * Synchronizing access to object with respect to
     * {@link FloydWarshallMap#enabled}.
     */
    protected Object mutex = new Object();
    /**
     * Log for logging
     */
    private Logger log;

    public FloydWarshallMap(Logger log) {
        this(BAD_EDGE_FLAG, log);
    }

    public FloydWarshallMap(int badEdgeFlag, Logger log) {
        this.badEdgeFlag = badEdgeFlag;
        this.log = log;
    }

    /**
     * Force FloydWarshall to run again, useful if you modify navpoints using
     * {@link NavigationGraphBuilder}.
     */
    public void refreshPathMatrix(List<NavPoint> navPoints) {
        synchronized (mutex) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(this + ": Refreshing path matrix...");
            }
            performFloydWarshall(navPoints);
            if (log.isLoggable(Level.WARNING)) {
                log.warning(this + ": Path matrix refreshed!");
            }
        }
    }

    protected void performFloydWarshall(MapPointListObtained map) {
        List<NavPoint> navPoints = MyCollections.asList(map.getNavPoints().values());
        performFloydWarshall(navPoints);
    }

    protected void performFloydWarshall(List<NavPoint> navPoints) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(this + ": Beginning Floyd-Warshall algorithm...");
        }
        long start = System.currentTimeMillis();

        // prepares data structures
        int size = navPoints.size();
        navPointIndices = new HashMap<UnrealId, Integer>(size);
        indicesNavPoints = new HashMap<Integer, NavPoint>(size);
        pathMatrix = new PathMatrixNode[size][size];

        // Initialize navPoint indices mapping.
        for (int i = 0; i < navPoints.size(); ++i) {
            navPointIndices.put(navPoints.get(i).getId(), i);
            indicesNavPoints.put(i, navPoints.get(i));
        }

        // Initialize distance matrix.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pathMatrix[i][j] = new PathMatrixNode((i == j) ? 0.0f
                        : Float.POSITIVE_INFINITY);
            }
        }

        // Set edge lengths into distance matrix.
        for (int i = 0; i < size; i++) {
            Point3d location = navPoints.get(i).getLocation().getPoint3d();
            for (NavPointNeighbourLink link : navPoints.get(i)
                    .getOutgoingEdges().values()) {
                if (checkLink(link)) {
                    pathMatrix[i][navPointIndices.get(link.getToNavPoint()
                            .getId())].setDistance((float) location
                            .distance(link.getToNavPoint().getLocation()
                            .getPoint3d()));
                }
            }
        }

        // Perform Floyd-Warshall.
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    float newLen = pathMatrix[i][k].getDistance()
                            + pathMatrix[k][j].getDistance();
                    if (pathMatrix[i][j].getDistance() > newLen) {
                        pathMatrix[i][j].setDistance(newLen);
                        pathMatrix[i][j].setViaNode(k);
                    }
                }
            }
        }

        // Construct paths + log unreachable paths.
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (pathMatrix[i][j].getDistance() == Float.POSITIVE_INFINITY) {
                    if (log.isLoggable(Level.FINER)) {
                        log.finer("Unreachable path from " + navPoints.get(i).getId().getStringId()
                                + " -> " + navPoints.get(j).getId().getStringId());
                    }
                    count++;
                } else {
                    // path exists ... retrieve it
                    pathMatrix[i][j].setPath(retrievePath(i, j));
                }
            }
        }

        if (count > 0) {
            if (log.isLoggable(Level.WARNING)) {
                log.warning(this + ": There are " + count + " unreachable nav point pairs (if you wish to see more, set logging to Level.FINER).");
            }
        }

        if (log.isLoggable(Level.INFO)) {
            log.info(this + ": computation for " + size + " navpoints took " + (System.currentTimeMillis() - start) + " millis");
        }

        // null unneeded field to free some memory
        indicesNavPoints = null;
    }

    /**
     * Checks whether the edge is usable.
     *
     * @param from Starting nav point.
     * @param edge NeighNav object representing the edge.
     * @return boolean
     */
    public boolean checkLink(NavPointNeighbourLink edge) {
        // Bad flags (prohibited edges, swimming, flying...).        
        if ((edge.getFlags() & badEdgeFlag) != 0) {
            return false;
        }
        
        // Bot can't move over translocator paths.
        if (edge.isOnlyTranslocator()) {
            return false;
        }

        // Lift flags.
        if ((edge.getFlags() & LinkFlag.SPECIAL.get()) != 0) {
            return true;
        }

        // Check whether the climbing angle is not so steep.
//		if ((edge.getFromNavPoint().getLocation().getPoint3d().distance(
//				edge.getToNavPoint().getLocation().getPoint3d()) < (edge
//				.getToNavPoint().getLocation().z - edge.getFromNavPoint()
//				.getLocation().z))
//				&& (edge.getFromNavPoint().getLocation().getPoint3d().distance(
//						edge.getToNavPoint().getLocation().getPoint3d()) > 100)) {
//			return false;
//		}

        // Check whether the jump is not so high.
//		if (((edge.getFlags() & LinkFlag.JUMP.get()) != 0)
//				&& (edge.getToNavPoint().getLocation().z
//						- edge.getFromNavPoint().getLocation().z > 80)) {
//			return false;
//		}
        //Check whether there is NeededJump attribute set - this means the bot has to 
        //provide the jump himself - if the Z of the jump is too high it means he
        //needs to rocket jump or ShieldGun jump - we will erase those links
        //as our bots are not capable of this
        if (edge.getNeededJump() != null && edge.getNeededJump().z > 680) {
            return false;
        }

        //This is a navpoint that requires lift jump - our bots can't do this - banning the link!
        if (edge.getToNavPoint().isLiftJumpExit()) {
            return false;
        }

        return true;
    }

    /**
     * Sub-routine of retrievePath - do not use! ... Well you may, it returns
     * path without 'from', 'to' or null if such path dosn't exist.
     * <p>
     * <p>
     * DO NOT USE OUTSIDE CONSTRUCTOR (relies on indicesNavPoints).
     *
     * @param from
     * @param to
     * @return
     */
    private List<NavPoint> retrievePathInner(Integer from, Integer to) {
        PathMatrixNode node = pathMatrix[from][to];
        if (node.getDistance() == Float.POSITIVE_INFINITY) {
            return null;
        }
        if (node.getViaNode() == null) {
            return new ArrayList<NavPoint>(0);
        }
        if (node.getViaNode() == null) {
            return new ArrayList<NavPoint>(0);
        }

        List<NavPoint> path = new ArrayList<NavPoint>();
        path.addAll(retrievePathInner(from, node.getViaNode()));
        path.add(indicesNavPoints.get(node.getViaNode()));
        path.addAll(retrievePathInner(node.getViaNode(), to));

        return path;
    }

    /**
     * Returns path between from-to or null if path doesn't exist. Path begins
     * with 'from' and ends with 'to'.
     * <p>
     * <p>
     * DO NOT USE OUTSIDE CONSTRUCTOR (relies on indicesNavPoints).
     *
     * @param from
     * @param to
     * @return
     */
    private List<NavPoint> retrievePath(Integer from, Integer to) {
        List<NavPoint> path = new ArrayList<NavPoint>();
        path.add(indicesNavPoints.get(from));
        path.addAll(retrievePathInner(from, to));
        path.add(indicesNavPoints.get(to));
        return path;
    }

    protected PathMatrixNode getPathMatrixNode(NavPoint np1, NavPoint np2) {
        return pathMatrix[navPointIndices.get(np1.getId())][navPointIndices
                .get(np2.getId())];
    }

    /**
     * Whether navpoint 'to' is reachable from the navpoint 'from'.
     * <p><p>
     * Throws exception if object is disabled, see
     * {@link FloydWarshallMap#setEnabled(boolean)}. Note that the object is
     * enabled by default.
     *
     * @param from
     * @param to
     * @return
     */
    public boolean reachable(NavPoint from, NavPoint to) {
        if ((from == null) || (to == null)) {
            return false;
        }
        return getPathMatrixNode(from, to).getDistance() != Float.POSITIVE_INFINITY;
    }

    /**
     * Calculate's distance between two nav points (using pathfinding).
     * <p><p>
     * Throws exception if object is disabled, see
     * {@link FloydWarshallMap#setEnabled(boolean)}. Note that the object is
     * enabled by default.
     *
     * @return Distance or POSITIVE_INFINITY if there's no path.
     */
    public float getDistance(NavPoint from, NavPoint to) {
        if ((from == null) || (to == null)) {
            return Float.POSITIVE_INFINITY;
        }
        return getPathMatrixNode(from, to).getDistance();
    }

    /**
     * Returns path between navpoints 'from' -> 'to'. The path begins with
     * 'from' and ends with 'to'. If such path doesn't exist, returns null.
     * <p><p>
     * Throws exception if object is disabled, see
     * {@link FloydWarshallMap#setEnabled(boolean)}. Note that the object is
     * enabled by default.
     *
     * @param from
     * @param to
     * @return
     */
    public List<NavPoint> getPath(NavPoint from, NavPoint to) {
        synchronized (mutex) {
            if ((from == null) || (to == null)) {
                return null;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("Retrieving path: " + from.getId().getStringId() + "[" + from.getLocation() + "] -> " + to.getId().getStringId() + "[" + to.getLocation() + "]");
            }
            List<NavPoint> path = getPathMatrixNode(from, to).getPath();
            if (path == null) {
                if (log.isLoggable(Level.WARNING)) {
                    log.warning("PATH NOT EXIST: " + from.getId().getStringId() + "[" + from.getLocation() + "] -> " + to.getId().getStringId() + "[" + to.getLocation() + "]");
                }
            } else {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Path exists - " + path.size() + " navpoints.");
                }
            }
            return path;
        }
    }

    protected void cleanUp() {
        pathMatrix = null;
        navPointIndices = null;
    }

    @Override
    public String toString() {
        return "FloydWarshallMap";
    }

    /**
     * Returns path between navpoints 'from' -> 'to'. The path begins with
     * 'from' and ends with 'to'. If such path does not exist, it returns
     * zero-sized path.
     * <p><p>
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
}
