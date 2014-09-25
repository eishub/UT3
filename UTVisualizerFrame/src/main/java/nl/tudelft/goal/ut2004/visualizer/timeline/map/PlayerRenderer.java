package nl.tudelft.goal.ut2004.visualizer.timeline.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import nl.tudelft.goal.ut2004.visualizer.map.TeamShade;

import com.jogamp.opengl.util.gl2.GLUT;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;

/**
 * Sub-renderer for object {@link IRenderableUTAgent}
 * 
 * <b>Implementation note:</b> be careful when asking twice for same field of
 * agent (like agent.getRotation()), because it is dependent on time when you
 * ask and returned value is not guaranteed to be same (like first can be valid
 * and second null).
 * 
 * @author Honza
 */
public class PlayerRenderer implements ISubGLRenderer<IPlayer> {

	private final double SPHERE_RADIUS = 60;
	private final int SPHERE_SLICES = 32;
	private final int SPHERE_STACKS = 32;

	private static final Color BLUE_TEAM_COLOR_DEFAULT = Color.BLUE;
	private static final Color RED_TEAM_COLOR_DEFAULT = Color.RED;

	private static final String BLUE_TEAM_COLOR_KEY = "BLUE_TEAM_COLOR_KEY";
	private static final String RED_TEAM_COLOR_KEY = "RED_TEAM_COLOR_KEY";

	private static final Preferences preferences = Preferences.userNodeForPackage(PlayerRenderer.class);
	private PreferenceChangeListener changeListener = new PreferenceChangeListener() {

		@Override
		public void preferenceChange(PreferenceChangeEvent evt) {
			dirty = true;
			color.setBlueTeam(getBlueTeamColor());
			color.setRedTeam(getRedTeamColor());

		}
	};
	private boolean dirty = true;

	private final int glName;
	private final IPlayer agent;
	private final TeamShade color;

	/**
	 * Create a new subrenderer with passed agent as source of data.
	 * 
	 * @param renderableUTAgent
	 *            agent used as source of data.
	 */
	public PlayerRenderer(IPlayer utAgent, int glName) {
		this.agent = utAgent;
		this.glName = glName;
		this.color = new TeamShade(getRedTeamColor(), getBlueTeamColor());
		preferences.addPreferenceChangeListener(changeListener);

	}

	@Override
	public void prepare(GL gl) {

	}

	@Override
	public IPlayer getObject() {
		return agent;
	}

	@Override
	public void render(GL gla) {
		GL2 gl = gla.getGL2();

		try {
			Location entityLocation = agent.getLocation();
			if (entityLocation == null) {
				return;
			}

			Location center = new Location(entityLocation.x, entityLocation.y, entityLocation.z + SPHERE_RADIUS * 1.1);
			GlColor color = new GlColor(getColor());

			gl.glLoadName(glName);
			renderAgent(gl, color, center);
			gl.glLoadName(-1);
			
			renderInfo(gl, color, center);


		} catch (RuntimeException e) {
			e.printStackTrace();
			// TODO handle situation when an agent disconnects
		}
	}

	/**
	 * Render agent at specified position
	 * 
	 * @param gl
	 * @param position
	 */
	private void renderAgent(GL gla, GlColor color, Location position) {
		final GLU glu = new GLU();
		final GLUquadric quadratic = glu.gluNewQuadric();

		GL2 gl = gla.getGL2();

		gl.glPushMatrix();
		{
			gl.glTranslated(position.x, position.y, position.z);
			// draw sphere
			gl.glColor4d(color.r, color.g, color.b, color.a);
			glu.gluSphere(quadratic, SPHERE_RADIUS, SPHERE_SLICES, SPHERE_STACKS);

		}
		gl.glPopMatrix();

		Rotation rot = agent.getRotation();
		if (rot != null) {
			renderRotation(gl, new GlColor(1, 0, 0), position, rot);
		}

	}

	/**
	 * Draw rotation arrow
	 * 
	 * @param gl
	 * @param color
	 *            What color should arrow be
	 * @param center
	 *            Where is center of arrow
	 * @param rotation
	 *            In what direction does arrow points
	 */
	private void renderRotation(GL gla, GlColor color, Location center, Rotation rotation) {
		final GLUT glut = new GLUT();
		GL2 gl = gla.getGL2();

		gl.glPushMatrix();
		{
			gl.glTranslated(center.x, center.y, center.z);

			Location endOfArrow = rotation.toLocation().getNormalized().scale(SPHERE_RADIUS * 2.5);

			gl.glBegin(GL.GL_LINES);
			gl.glColor4d(color.r, color.g, color.b, color.a);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(endOfArrow.x, endOfArrow.y, endOfArrow.z);
			gl.glEnd();

			gl.glTranslated(endOfArrow.x, endOfArrow.y, endOfArrow.z);
			// XXX: This works only in 2D, not 3D, because I am not in the mood
			// to figure out direction of Roll, Yaw and Pitch as well as order
			// of
			// transformations. And rotation.toLocation() returns 2D coords
			// anyway.

			double yaw = rotation.getYaw() / 32767 * 180; // left right, aka
															// around z
			double roll = rotation.getRoll() / 32767 * 180; // clockwise/counter?
															// around x
			double pitch = rotation.getPitch() / 32767 * 180; // up and down,
																// around y

			/*
			 * gl.glRotated(pitch, ); gl.glRotated(yaw, ); gl.glRotated(roll, );
			 */
			// return res.mul(pitch).mul(yaw).mul(roll);

			// gl.glRotated(roll, 1,0,0);
			gl.glRotated(yaw, 0, 0, 1);
			// gl.glRotated(pitch, 0,1,0);

			gl.glRotated(90, 0, 1, 0);

			glut.glutSolidCone(20, 40, 16, 16);
		}
		gl.glPopMatrix();

	}
	
	/**
	 * Render info about agent (in most cases event messages) and its name.
	 * TODO: Ugly code, refactor
	 * 
	 * @param gl
	 * @param color
	 *            What color should be used to render info.
	 * @param location
	 *            position of agent
	 */
	private void renderInfo(GL gla, GlColor color, Location location) {
		final GLU glu = new GLU();
		final GLUT glut = new GLUT();

		GL2 gl = gla.getGL2();

		// get text
		List<String> infos = new ArrayList<String>();
		infos.add(0, '*' + agent.getName() + '*');

		Location topHead = new Location(location.x, location.y, location.z + 2 * SPHERE_RADIUS * 1.1);

		Location top2d = GLTools.getScreenCoordinates(gl, glu, topHead);

		int lineGap = 12;
		int font = GLUT.BITMAP_HELVETICA_10;

		int maxWidth = 0;
		for (String line : infos) {
			int lineWidth = glut.glutBitmapLength(font, line);
			if (lineWidth > maxWidth) {
				maxWidth = lineWidth;
			}
		}

		// update starting position
		top2d = new Location(top2d.x - maxWidth / 2, top2d.y + (infos.size() - 1) * lineGap, top2d.z);

		GlColor textColor = color.getMixedWith(new GlColor(0, 0, 0), 80);

		gl.glColor3d(textColor.r, textColor.g, textColor.b);
		for (int i = 0; i < infos.size(); i++) {
			String text = infos.get(i);
			if (i == 0) {
				gl.glColor3d(color.r, color.g, color.b);
			} else {
				gl.glColor3d(textColor.r, textColor.g, textColor.b);
				// gl.glColor3d(0, 0, 0);
			}
			Location textPos = GLTools.getWorldCoordinates(gl, glu, top2d);
			gl.glRasterPos3d(textPos.x, textPos.y, textPos.z);
			glut.glutBitmapString(font, text);

			top2d = top2d.addY(-lineGap);
		}
	}

	public Color getColor() {
		return color.getColor(agent.getTeam());
	}

	public static Color getRedTeamColor() {
		int rgb = preferences.getInt(RED_TEAM_COLOR_KEY, RED_TEAM_COLOR_DEFAULT.getRGB());
		return new Color(rgb);
	}

	public static void setRedTeamColor(Color c) {
		preferences.putInt(RED_TEAM_COLOR_KEY, c.getRGB());
	}

	public static Color getBlueTeamColor() {
		int rgb = preferences.getInt(BLUE_TEAM_COLOR_KEY, BLUE_TEAM_COLOR_DEFAULT.getRGB());
		return new Color(rgb);
	}

	public static void setBlueTeamColor(Color c) {
		preferences.putInt(BLUE_TEAM_COLOR_KEY, c.getRGB());
	}

	@Override
	public int getGLName() {
		return glName;
	}
	@Override
	public void destroy() {
		preferences.removePreferenceChangeListener(changeListener);
	}
}
