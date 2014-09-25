package nl.tudelft.goal.ut2004.visualizer.map;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import java.awt.Color;

import nl.tudelft.goal.ut2004.visualizer.timeline.map.GlColor;


/**
 * Structure holding info for one vertex of blended polygon.
 * Only color and position (in worldspace) for now.
 * @author Honza
 */
public class BlendVertex {
    /**
     * Color of vertex
     */
    protected GlColor color;

    /**
     * Location of vertex in worldspace
     */
    protected Location location;

    public BlendVertex(Location location, Color color) {
        this.location = location;
        this.color = new GlColor(color);
    }

    public BlendVertex(Location location, GlColor color) {
        this.location = location;
        this.color = new GlColor(color);
    }

    /**
     * Return color of vertex.
     * @return color instance, not a copy. Change in will reflect in vertex.
     */
    public GlColor getColor() {
        return color;
    }

    /**
     * Return location of vertex
     * @return actual location instance, not a copy. Changes made to the location
     *         will reflext in the vertex
     */
    public Location getLocation() {
        return location;
    }
}
