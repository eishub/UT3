/**
 * BaseUnrealEnvironment, an implementation of the environment interface standard that 
 * facilitates the connection between GOAL and the UT2004 engine. 
 * 
 * Copyright (C) 2012 BaseUnrealEnvironment authors.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.unreal.messages;

import java.util.logging.Level;

import nl.tudelft.goal.ut2004.util.Skin;
import nl.tudelft.goal.ut2004.util.Team;
import cz.cuni.amis.pogamut.base.agent.impl.AgentId;
import cz.cuni.amis.pogamut.base.agent.params.IAgentParameters;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.ut2004.bot.params.UT2004BotParameters;
/**
 * Holds parameters specific for the bot.
 * <ul>
 * Parameters stored are:
 * <li>{@link BotParametersKey#BOTNAME}</li>
 * <li>{@link BotParametersKey#LEADTARGET}</li>
 * <li>{@link BotParametersKey#LOGLEVEL}</li>
 * <li>{@link BotParametersKey#BOT_SERVER}</li>
 * <li>{@link BotParametersKey#SKILL}</li>
 * <li>{@link BotParametersKey#SKIN}</li>
 * <li>{@link BotParametersKey#TEAM}</li>
 * <li>{@link BotParametersKey#STARTLOCATION}</li>
 * <li>{@link BotParametersKey#STARTROTATION}</li>
 * </ul>
 * 
 * Also provides functionality to assign defaults to parameters that have not
 * been assigned.
 * 
 * @author M.P. Korstanje
 * 
 */
public class BotParameters extends Parameters  {
	
	// Bot parameters
	private Level logLevel;
	private Boolean shouldLeadTarget;
	private Integer skill;
	private Skin skin;


	@Override
	public void assignDefaults(IAgentParameters defaults) {
		super.assignDefaults(defaults);

		if (defaults instanceof BotParameters) {
			BotParameters parameters = (BotParameters) defaults;

			if (logLevel == null)
				logLevel = parameters.getLogLevel();
			if (shouldLeadTarget == null)
				shouldLeadTarget = parameters.shouldLeadTarget();
			if (skill == null)
				skill = parameters.getSkill();
			if (skin == null)
				skin = parameters.getSkin();
		}
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public int getSkill() {
		return skill;
	}

	public Skin getSkin() {

		return skin;
	}

	public BotParameters setLogLevel(Level level) {
		assert level != null;
		this.logLevel = level;
		return this;
	}

	public BotParameters setShouldLeadTarget(boolean shouldLeadTarget) {
		this.shouldLeadTarget = shouldLeadTarget;
		return this;
	}

	/**
	 * Sets the bots skill between 0..7. The bots skill only influences the bots
	 * accuracy. Values outside the valid range are clamped.
	 * 
	 * @param skill
	 *            an integer in the range 0..7
	 */
	public BotParameters setSkill(int skill) {
		// Clamp between 0 and 7.
		this.skill = Math.min(7, Math.max(skill, 0));
		return this;
	}

	public BotParameters setSkin(Skin skin) {
		this.skin = skin;
		return this;
	}

	/**
	 * Sets the bots preferred team. Typically teams are either 0 or 1.
	 * 
	 * @param team
	 */
	public BotParameters setTeam(Team team) {
		super.setTeam(team.id());
		return this;
	}

	@Override
	public BotParameters setInitialLocation(Location location) {
		super.setInitialLocation(location);
		return this;
	}
	
	@Override
	public BotParameters setInitialRotation(Rotation rotation) {
		super.setInitialRotation(rotation);
		return this;
	}

	public Boolean shouldLeadTarget() {
		return shouldLeadTarget;
	}

	@Override
	public String toString() {
		return "BotParameters [logLevel=" + logLevel + ", shouldLeadTarget=" + shouldLeadTarget + ", skill=" + skill
				+ ", skin=" + skin + ", getTeam()=" + getTeam() + ", getWorldAddress()=" + getWorldAddress()
				+ ", getAgentId()=" + getAgentId() + "]";
	}
	
	public UT2004BotParameters setAgentId(String agentName) {
		super.setAgentId(new AgentId(agentName));
		return this;
	}

	
	public static BotParameters getDefaults() {

		BotParameters parameters = new BotParameters();

		parameters.logLevel = Level.WARNING;
		parameters.shouldLeadTarget = true;
		parameters.skill = 3;
		parameters.skin = Skin.BotB;
		
		return parameters;
	}




}
