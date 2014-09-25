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
package nl.tudelft.goal.ut2004.util;

public enum Skin {

	NightMaleA("HumanMaleA.NightMaleA"), NightMaleB("HumanMaleA.NightMaleB"), MercMaleA("HumanMaleA.MercMaleA"), MercMaleB(
			"HumanMaleA.MercMaleB"), MercMaleC("HumanMaleA.MercMaleC"), MercMaleD("HumanMaleA.MercMaleD"), EgyptMaleA(
			"HumanMaleA.EgyptMaleA"), EgyptMaleB("HumanMaleA.EgyptMaleB"), MercFemaleA("HumanFemaleA.MercFemaleA"), MercFemaleB(
			"HumanFemaleA.MercFemaleB"), MercFemaleC("HumanFemaleA.MercFemaleC"), NightFemaleA("HumanFemaleA.NightFemaleA"), NightFemaleB(
			"HumanFemaleA.NightFemaleB"), EgyptFemaleA("HumanFemaleA.EgyptFemaleA"), EgyptFemaleB("HumanFemaleA.EgyptFemaleB"), JakobM(
			"ThunderCrash.JakobM"), AlienMaleA("Aliens.AlienMaleA"), AlienMaleB("Aliens.AlienMaleB"), AlienFemaleA("Aliens.AlienFemaleA"), AlienFemaleB(
			"Aliens.AlienFemaleB"), BotA("Bot.BotA"), BotB("Bot.BotB"), BotC("Bot.BotC"), BotD("Bot.BotD"), Hellion_Kane(
			"Hellions.Hellion_Kane"), Hellion_Garrett("Hellions.Hellion_Garrett"), Hellion_Gitty("Hellions.Hellion_Gitty"), JuggMaleA(
			"Jugg.JuggMaleA"), JuggMaleB("Jugg.JuggMaleB"), JuggFemaleA("Jugg.JuggFemaleA"), JuggFemaleB("Jugg.JuggFemaleB"), AbaddonM(
			"NewNightmare.AbaddonM"), Ophelia("NewNightmare.Ophelia"), Skaarj2("SkaarjAnims.Skaarj2"), Skaarj3("SkaarjAnims.Skaarj3"), Skaarj4(
			"SkaarjAnims.Skaarj4"), XanM03("XanRobots.XanM03"), XanM02("XanRobots.XanM02"), XanF02("XanRobots.XanF02"), EnigmaM(
			"XanRobots.EnigmaM");

	private final String skinName;

	private Skin(String skinName) {
		this.skinName = skinName;
	}

	/**
	 * 
	 * Returns a String that can be used in the initialized to select the bots
	 * skin.
	 * 
	 * @return String representing UT Skin name of this skin.
	 */
	public String getUnrealName() {
		return skinName;
	}

}
