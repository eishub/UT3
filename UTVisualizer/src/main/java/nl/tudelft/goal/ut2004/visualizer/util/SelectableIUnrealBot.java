/*
 * Copyright (C) 2010 Unreal Visualizer Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.ut2004.visualizer.util;

import java.util.ArrayList;
import java.util.Collection;

import cz.cuni.amis.pogamut.unreal.bot.IUnrealBot;

/**
 * Wrapper to provide a nice representation of bots in a list.
 * 
 * @author M.P. Korstanje
 * 
 */
public class SelectableIUnrealBot {

	IUnrealBot bot;

	public SelectableIUnrealBot(IUnrealBot bot) {
		this.bot = bot;
	}

	public IUnrealBot getIUnrealBot() {
		return bot;
	}

	@Override
	public String toString() {
		return bot.getName();
	}

	public static Collection<SelectableIUnrealBot> fromCollection(
			Collection<? extends IUnrealBot> agents) {

		Collection<SelectableIUnrealBot> ret = new ArrayList<SelectableIUnrealBot>(
				agents.size());

		for (IUnrealBot bot : agents) {
			ret.add(new SelectableIUnrealBot(bot));
		}

		return ret;
	}

}
