package nl.tudelft.goal.ut2004.visualizer.connection;

import java.io.Serializable;
import java.util.logging.Level;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;

import nl.tudelft.goal.ut2004.util.Skin;
import nl.tudelft.goal.ut2004.util.Team;

/**
 * Command to add bots to the game. Supports the relevant options from
 * {@link AddBotCommand}. AddBotCommand is not used directly because it is not
 * serializable.
 * 
 * @author mpkorstanje
 * 
 */
public final class AddBotCommand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201210241640L;

	private String botName;
	private Level logLevel;
	private Boolean shouldLeadTarget;
	private Integer skill;
	private Skin skin;
	private Team team;
	private Rotation rotation;
	private Location location;

	public String getBotName() {
		return botName;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

	public Boolean getShouldLeadTarget() {
		return shouldLeadTarget;
	}

	public void setShouldLeadTarget(Boolean shouldLeadTarget) {
		this.shouldLeadTarget = shouldLeadTarget;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getSkill() {
		return skill;
	}

	public void setSkill(Integer skill) {
		this.skill = skill;
	}

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "AddBotCommand [botName=" + botName + ", logLevel=" + logLevel + ", shouldLeadTarget=" + shouldLeadTarget + ", skill="
				+ skill + ", skin=" + skin + ", team=" + team + ", rotation=" + rotation + ", location=" + location + "]";
	}

}
