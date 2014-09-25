/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tudelft.goal.ut2004.visualizer.timeline.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.media.opengl.GL;

/**
 * Basically list of {@link ISubGLRenderer}s with stuff for manipulating it and
 * rendering it.
 * 
 * Holds order of subrenderers.
 * 
 * @param <T>
 *            Type of object subrenderers of this collection draws.
 * @author Honza
 */
public class CollectionRenderer<T> implements ISubGLRenderer<Collection<T>> {

	private List<ISubGLRenderer<T>> renderers = Collections.synchronizedList(new ArrayList<ISubGLRenderer<T>>());

	/**
	 * Add renderer to collection. It will be rendered after all previous
	 * renders are drawn.
	 * 
	 * @param subrenderer
	 */
	public synchronized void addRenderer(ISubGLRenderer<? extends T> subrenderer) {
		renderers.add((ISubGLRenderer<T>) subrenderer);
	}

	/**
	 * Remove subrenderer from collection
	 * 
	 * @param subrenderer
	 *            renderer to be removed
	 * @return true if subrenderes was in collection
	 */
	public synchronized boolean removeRenderer(ISubGLRenderer<? extends T> subrenderer) {
		return renderers.remove(subrenderer);
	}

	/**
	 * Remove all renderers that draw object o.
	 * 
	 * @param o
	 *            object that may have renderers in collection we want to remove
	 */
	public synchronized void removeRenderersOf(Object o) {
		Collection<ISubGLRenderer<T>> toRemove = new ArrayList<ISubGLRenderer<T>>();

		for (ISubGLRenderer<T> renderer : renderers) {
			if (renderer.getObject() == o) {
				toRemove.add(renderer);
			}

		}

		renderers.removeAll(toRemove);
	}

	/**
	 * Get set of all objects this collection draws.
	 * 
	 * @return all objects this collection draws
	 */
	public synchronized Set<T> getDrawnObjects() {
		HashSet<T> set = new HashSet<T>();
		for (ISubGLRenderer<T> renderer : renderers) {
			set.add(renderer.getObject());
		}
		return set;
	}

	@Override
	public synchronized void render(GL gl) {
		for (ISubGLRenderer<T> subrenderer : renderers) {
			subrenderer.render(gl);
		}
	}

	@Override
	public synchronized Collection<T> getObject() {
		Collection<T> objects = new ArrayList<T>();
		for (ISubGLRenderer<T> subrenderer : renderers) {
			objects.add(subrenderer.getObject());
		}

		return objects;
	}

	/**
	 * Prepare all renderers for rendering
	 * 
	 * @param gl
	 */
	@Override
	public synchronized void prepare(GL gl) {
		for (ISubGLRenderer<T> renderer : renderers) {
			renderer.prepare(gl);
		}
	}

	public synchronized T getObjectsByGLName(int glName) {
		// find that miserable renderer in agents
		for (ISubGLRenderer<T> renderer : renderers) {
			if (renderer.getGLName() == glName) {
				return renderer.getObject();
			}

		}

		return null;

	}

	public synchronized Collection<T> getObjectsByGLName(int[] list) {

		Collection<T> results = new ArrayList<T>();
		for (int glName : list) {
			T result = getObjectsByGLName(glName);
			if (result != null) {
				results.add(result);
			}
		}

		return results;

	}

	public void destroy() {
		for(ISubGLRenderer<T> renderer : renderers){
			renderer.destroy();
		}
		
	}

	@Override
	public int getGLName() {
		return -1;
	}
}
