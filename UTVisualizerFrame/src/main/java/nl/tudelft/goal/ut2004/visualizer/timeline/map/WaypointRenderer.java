package nl.tudelft.goal.ut2004.visualizer.timeline.map;

import static javax.media.opengl.GL2.GL_COMPILE;

import java.awt.Color;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;

/**
 * Sub renderer for Flag objects.
 * 
 * 
 * @author M.P. Korstanje
 * 
 */
public class WaypointRenderer implements ISubGLRenderer<IUnrealWaypoint> {

	
	public static final double NAVPOINT_OUTER_RADIUS = 30;
	public static final double NAVPOINT_INNER_RADIUS = 0;
	public static final int NAVPOINT_SLICES = 32;
	public static final int NAVPOINT_RINGS = 1;
	public static final double NAVPOINT_FLOAT = 1;
	
	private static final Color WAYPOINT_COLOR_DEFAULT = Color.BLUE;
	private static final String WAYPOINT_COLOR_KEY = "WAYPOINT_COLOR_KEY";
	
	
	private static final Preferences preferences = Preferences.userNodeForPackage(WaypointRenderer.class);
	private PreferenceChangeListener changeListener = new PreferenceChangeListener() {

		@Override
		public void preferenceChange(PreferenceChangeEvent evt) {
			dirty = true;
		}
	};
	private final IUnrealWaypoint waypoint;
	private final int glName;
	private int displayList;
	private boolean dirty = false;

	/**
	 * Create a new subrenderer with passed waypoint as source of data.
	 * 
	 * @param renderableUTAgent
	 *            agent used as source of data.
	 */
	public WaypointRenderer(IUnrealWaypoint waypoint, int glName) {
		this.waypoint = waypoint;
		this.glName = glName;
		preferences.addPreferenceChangeListener(changeListener);
	}

	@Override
	public void prepare(GL gl) {
		
	}

	@Override
	public void render(GL gla) {
		GL2 gl = gla.getGL2();

		try {
			
			if (dirty  || !gl.glIsList(displayList)) {
				updateDisplayLists(gl);
				dirty = false;
			}
			
			gl.glLoadName(glName);
			gl.glCallList(displayList);
			gl.glLoadName(-1);

		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

	private void updateDisplayLists(GL2 gl) {
		// delete old
		gl.glDeleteLists(displayList, 1);
		
		//make new
		displayList = gl.glGenLists(1);
		gl.glNewList(displayList, GL_COMPILE);
		renderWayPoint(gl);
		gl.glEndList();
	}

	private void renderWayPoint(GL2 gl) {
		GLU glu = new GLU();
		GLUquadric qaudric = glu.gluNewQuadric();

		GlColor color = new GlColor(getColor());
		Location position = waypoint.getLocation();
		if(position == null){
			return;
		}
		

		
		gl.glPushMatrix();
		{
			gl.glTranslated(position.x, position.y, position.z + NAVPOINT_FLOAT);
			gl.glColor4d(color.r, color.g, color.b, color.a);
			glu.gluDisk(qaudric, NAVPOINT_INNER_RADIUS, NAVPOINT_OUTER_RADIUS, NAVPOINT_SLICES, NAVPOINT_RINGS);

		}
		gl.glPopMatrix();
		
	}

	@Override
	public IUnrealWaypoint getObject() {
		return waypoint;
	}
	

	public static Color getColor() {
		int rgb = preferences.getInt(WAYPOINT_COLOR_KEY, WAYPOINT_COLOR_DEFAULT.getRGB());
		return new Color(rgb);
	}
	
	public static void setColor(Color c){
		preferences.putInt(WAYPOINT_COLOR_KEY, c.getRGB());
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
