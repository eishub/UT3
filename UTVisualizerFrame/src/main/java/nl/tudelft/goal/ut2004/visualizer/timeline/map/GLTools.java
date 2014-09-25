/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tudelft.goal.ut2004.visualizer.timeline.map;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import static javax.media.opengl.GL.*; // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;

/**
 * 
 * @author Honza
 */
public class GLTools {

	private static GLU glu = new GLU();
	private static GLUT glut = new GLUT();

	public static Location getWorldCoordinates(GL gla, GLU glu, Location screen) {
		GL2 gl = gla.getGL2();

		// Modelview matrix
		FloatBuffer mvBuffer = FloatBuffer.allocate(16);
		gl.glGetFloatv(GL_MODELVIEW_MATRIX, mvBuffer);
		// Projection_matrix
		FloatBuffer prBuffer = FloatBuffer.allocate(16);
		gl.glGetFloatv(GL_PROJECTION_MATRIX, prBuffer);
		// Viewport matrix
		IntBuffer vpBuffer = IntBuffer.allocate(16);
		gl.glGetIntegerv(GL_VIEWPORT, vpBuffer);

		FloatBuffer result = FloatBuffer.allocate(3);

		glu.gluUnProject((float) screen.x, (float) screen.y, (float) screen.z, mvBuffer, prBuffer, vpBuffer, result);

		return new Location(result.get(0), result.get(1), result.get(2));
	}

	public static Location getScreenCoordinates(GL gla, GLU glu, Location worldPosition) {
		GL2 gl = gla.getGL2();

		// Modelview matrix
		FloatBuffer mvBuffer = FloatBuffer.allocate(16);
		gl.glGetFloatv(GL_MODELVIEW_MATRIX, mvBuffer);
		// Projection_matrix
		FloatBuffer prBuffer = FloatBuffer.allocate(16);
		gl.glGetFloatv(GL_PROJECTION_MATRIX, prBuffer);
		// Viewport matrix
		IntBuffer vpBuffer = IntBuffer.allocate(16);
		gl.glGetIntegerv(GL_VIEWPORT, vpBuffer);

		FloatBuffer result = FloatBuffer.allocate(3);

		glu.gluProject((float) worldPosition.x, (float) worldPosition.y, (float) worldPosition.z, mvBuffer, prBuffer,
				vpBuffer, result);

		return new Location(result.get(0), result.get(1), result.get(2));
	}

	private static Texture texture;

	/**
	 * Render window for text to be drawn into it.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void renderWindow(GL gla, int x, int y, int width, int height) {
		GL2 gl = gla.getGL2();
		pushMatrixMode(gl);
		setOrthoViewport(gl);

		try {
			double z = 0;
			if (texture == null) {
				texture = TextureIO.newTexture(new File("c:/temp/windowTexture.PNG"), false);
			}
			texture.bind(gl);
			texture.enable(gl);

			gl.glBegin(GL_QUADS);
			{
				gl.glColor3d(1, 1, 1);
				gl.glTexCoord2d(0, 0);
				gl.glVertex3d(x, y, z);

				gl.glTexCoord2d(1, 0);
				gl.glVertex3d(x + width, y, z);

				gl.glTexCoord2d(1, 1);
				gl.glVertex3d(x + width, y + height, z);

				gl.glTexCoord2d(0, 1);
				gl.glVertex3d(x, y + height, z);
			}
			gl.glEnd();

			texture.disable(gl);
		} catch (IOException ex) {
			// TODO: Handle this gracefully
			ex.printStackTrace();
		} catch (GLException ex) {
			// TODO: Handle this gracefully
			ex.printStackTrace();
		}

		popMatrixMode(gl);
	}

	/**
	 * Set mode according to viewport. 0,0 is at left top
	 */
	public static void setOrthoViewport(GL gla) {
		GL2 gl = gla.getGL2();
		Rectangle viewport = getViewport(gl);
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(viewport.getMinX(), viewport.getMaxX(), viewport.getMinY(), viewport.getMaxY(), -1, 1);
		gl.glMatrixMode(GL_MODELVIEW);
	}

	public static Rectangle getViewport(GL gl) {
		int viewport[] = new int[4];
		gl.glGetIntegerv(GL_VIEWPORT, viewport, 0);
		return new Rectangle(viewport[0], viewport[1], viewport[2], viewport[3]);
	}

	public static void pushMatrixMode(GL gla) {
		GL2 gl = gla.getGL2();

		gl.glMatrixMode(GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
	}

	public static void popMatrixMode(GL gla) {
		GL2 gl = gla.getGL2();

		gl.glMatrixMode(GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glPopMatrix();
	}
}
