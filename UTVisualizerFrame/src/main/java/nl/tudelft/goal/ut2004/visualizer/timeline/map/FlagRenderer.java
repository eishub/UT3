package nl.tudelft.goal.ut2004.visualizer.timeline.map;

import java.awt.Color;
import java.util.prefs.Preferences;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;

/**
 * Sub renderer for Flag objects.
 * 
 * 
 * @author M.P. Korstanje
 * 
 */
public class FlagRenderer implements ISubGLRenderer<FlagInfo> {

	private static final double SPHERE_RADIUS = 64;
	private static final int SPHERE_SLICES = 32;
	private static final int SPHERE_STACKS = 32;
	private static final double CYLINDER_RADIUS = 16;
	private static final int CYLINDER_SLICES = 5;
	private static final int CYLINDER_STACKS = 1;
	private static final double CYLINDER_HEIGHT = SPHERE_RADIUS * 3;

	private static final Color BLUE_FLAG_COLOR_DEFAULT = Color.BLUE;
	private static final Color RED_FLAG_COLOR_DEFAULT = Color.RED;
	private static final Color OTHER_FLAG_COLOR_DEFAULT = Color.ORANGE;

	private static final String BLUE_FLAG_COLOR_KEY = "BLUE_FLAG_COLOR_KEY";
	private static final String RED_FLAG_COLOR_KEY = "RED_FLAG_COLOR_KEY";
	private static final String OTHER_FLAG_COLOR_KEY = "OTHER_FLAG_COLOR_KEY";

	private static final Preferences preferences = Preferences.userNodeForPackage(FlagRenderer.class);

	private final FlagInfo flag;
	private final int glName;

	/**
	 * Create a new subrenderer with passed agent as source of data.
	 * 
	 * @param renderableUTAgent
	 *            agent used as source of data.
	 */
	public FlagRenderer(FlagInfo flag, int glName) {
		this.flag = flag;
		this.glName = glName;
	}

	@Override
	public void prepare(GL gl) {

	}

	@Override
	public void render(GL gla) {
		GL2 gl = gla.getGL2();

		try {
			Location flagLocation = flag.getLocation();
			if (flagLocation == null) {
				return;
			}
			Location center = new Location(flagLocation.x, flagLocation.y, flagLocation.z);
			GlColor color = new GlColor(getColor());
			gl.glLoadName(glName);
			renderFlag(gl, color, center);
			gl.glLoadName(-1);

		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

	private void renderFlag(GL gla, GlColor color, Location position) {
		GLU glu = new GLU();
		GLUT glut = new GLUT();
		GLUquadric quadratic = glu.gluNewQuadric();

		GL2 gl = gla.getGL2();
		gl.glPushMatrix();
		{
			gl.glTranslated(position.x, position.y, position.z);
			gl.glColor4d(color.r, color.g, color.b, color.a);
			glu.gluCylinder(quadratic, CYLINDER_RADIUS, CYLINDER_RADIUS, CYLINDER_HEIGHT, CYLINDER_SLICES, CYLINDER_STACKS);
			gl.glTranslated(0, 0, CYLINDER_HEIGHT);
			glu.gluSphere(quadratic, SPHERE_RADIUS, SPHERE_SLICES, SPHERE_STACKS);
		}
		gl.glPopMatrix();
	}

	@Override
	public FlagInfo getObject() {
		return flag;
	}

	public static Color getRedFlagColor() {
		int rgb = preferences.getInt(RED_FLAG_COLOR_KEY, RED_FLAG_COLOR_DEFAULT.getRGB());
		return new Color(rgb);
	}

	public static void setRedFlagColor(Color c) {
		preferences.putInt(RED_FLAG_COLOR_KEY, c.getRGB());
	}

	public static Color getBlueFlagColor() {
		int rgb = preferences.getInt(BLUE_FLAG_COLOR_KEY, BLUE_FLAG_COLOR_DEFAULT.getRGB());
		return new Color(rgb);
	}

	public static void setBlueFlagColor(Color c) {
		preferences.putInt(BLUE_FLAG_COLOR_KEY, c.getRGB());
	}

	public static Color getOtherFlagColor() {
		int rgb = preferences.getInt(OTHER_FLAG_COLOR_KEY, OTHER_FLAG_COLOR_DEFAULT.getRGB());
		return new Color(rgb);
	}

	public static void setOtherFlagColor(Color c) {
		preferences.putInt(OTHER_FLAG_COLOR_KEY, c.getRGB());
	}

	public Color getColor() {
		switch (flag.getTeam()) {
		case 0:
			return getRedFlagColor();
		case 1:
			return getBlueFlagColor();
		default:
			return getOtherFlagColor();
		}
	}

	@Override
	public int getGLName() {
		return glName;
	}

	@Override
	public void destroy() {
		// Does nothing.
	}

}
