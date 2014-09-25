/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;
import java.util.logging.Logger;

import javax.media.opengl.GLCapabilities;

import nl.tudelft.goal.ut2004.visualizer.services.ISelectionHandler;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealMap;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.map.UT2004Map;

/**
 * This is MapGLPanel that is adding selection behavior, when bot is clicked in
 * the map, list of selected bots in this map IPogamutEnvironments global lookup
 * will change.
 * 
 * @author Honza
 * 
 * @since 13-Jan-2011 : Replaced selection handler by an interface to make it
 *        work for now.
 */
abstract public class SelectableMapGLPanel extends MapGLPanel implements
		MouseListener {

	ISelectionHandler selectionHandler;

	public SelectableMapGLPanel(GLCapabilities caps, UT2004Map map) {
		super(caps, map);
		this.addMouseListener(this);
	}

	public SelectableMapGLPanel(IUnrealMap map, Logger log) {
		super(map, log);
		this.addMouseListener(this);
	}

	@Override
	public void destroy() {
		this.removeMouseListener(this);
		super.destroy();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// we only want to select/deselect on left click, not middle click used
		// during drag or something like that
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		// get global selection object for this map
		ISelectionHandler selectionHandler = getSelectionHandler();
		if (selectionHandler == null) {
			return;
		}

		// Get list of selected bots
		Set<Object> selected = this.getObjectsAt(e.getPoint());

		selectionHandler.selected(selected, e.getPoint(), this);
	}

	protected ISelectionHandler getSelectionHandler() {
		return selectionHandler;
	}

	public void setSelectionHandler(ISelectionHandler selectionHandler) {
		this.selectionHandler = selectionHandler;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
