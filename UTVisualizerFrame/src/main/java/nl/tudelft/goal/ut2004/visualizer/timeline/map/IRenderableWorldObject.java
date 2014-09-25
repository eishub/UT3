package nl.tudelft.goal.ut2004.visualizer.timeline.map;

import java.awt.Color;

import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;

/**
 * Wrapper around world objects to simplify rendering.
 * 
 * TODO: Merge/inherit with IRenderableUTAgent.
 * 
 * 
 * @author M.P. Korstanje
 *
 */
public interface IRenderableWorldObject extends ILocated {

    /**
     * Get color of agent. It should not change if possible, otherwise it can confuse a user.
     * @return Color that will be used to render agent in map.
     */
    public Color getColor();

    /**
     * Return source of all data that are providing stuff used.
     * Why do I want it? The selection, I am putting this stuff to lookup and
     * some other component can look it up and select nodes representing data sources.
     *
     * XXX: return type should be more general, but this saves trouble and no need to
     * generalize too soon.
     */
    public Object getDataSource();
    
    /**
     * Return OpenGl name used for selection (see selection mode of opengl).
     * Basically after all is rendered, when get glNames(ints) of objects that were
     * rendered in viewvolume. But we need to map it back. That is what this is for.
     * @return
     */
    public int getGLName();
    
}
