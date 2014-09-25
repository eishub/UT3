/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tudelft.goal.ut2004.visualizer.timeline.map;

import javax.media.opengl.GL;
import javax.media.opengl.GLEventListener;



/**
 * This is a subrenderer, it's job is to render some stuff.
 * Basically I had few {@link GLEventListener}s and there were troubles
 * with using them directly (order of rendering), so I have now only these
 * subrenderes that are part of {@link CollectionRenderer}.
 *
 * @param <T> Class of object this renderer draws.
 * @author Honza
 */
public interface ISubGLRenderer<T> {
    /**
     * Here should be done preparation for rendering (e.g. generation of display
     * lists from massive data)
     * @param gl
     */
    public void prepare(GL gl);

    /**
     * Display stuff you want to. Assume that settings have already been set in
     * {@link CollectionRenderer}
     * @param gl
     */
    public void render(GL gl);

    /**
     * Return object this renderer draws.
     *
     * Because objects we want to draw can change rapidly, we have to remove and
     * add subrenderers based on passed objects (renderer R draws object A, now
     * we don't want to draw A anymore, we have to go through subrenderers to find
     * which ones draws it).
     * @return Object this renderer draws.
     */
    public T getObject();

    
    public int getGLName();

    /**
     * Call renderer to clean up any resources it used.
     */
	public void destroy();

}
