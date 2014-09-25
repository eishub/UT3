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
import java.util.Collections;
import java.util.HashMap;

/**
 * Utility object that allows scores from players to be sorted in descending
 * order.
 * 
 * @author Lennard de Rijk
 *
 */
public class Score implements Comparable<Score>{

	private final int id;
	private final double score;

	public Score(int id, double score){
		this.id = id;
		this.score = score;
	}

	/**
	 * {@inheritDoc}
	 * Default order is descending.
	 */
	@Override
	public int compareTo(Score o) {
		double diff =  o.score - this.score;

		if(diff > 0){
			return 1;
		}else if(diff < 0)
		{
			return -1;
		}else{
			return 0;
		}
	}

	/**
	 * @return the ID from the player who has this score
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Returns an ordered lists of {@link Score} objects that is constructed
	 * from the given {@link HashMap}.
	 * @param scoreMap The {@link HashMap} that maps UnrealIDs to a score
	 * @return An {@link ArrayList} of {@link Score} in descending order
	 */
	public static ArrayList<Score> getOrderedScoresFor(
			HashMap<Integer, Double> scoreMap){

		ArrayList<Score> scores = new ArrayList<Score>(scoreMap.size());

		// For each entry create a new Score object
		for (Integer id : scoreMap.keySet()) {
			double s = scoreMap.get(id);
			Score score = new Score(id, s);
			scores.add(score);
		}

		// Sort and return
		Collections.sort(scores);
		return scores;
	}

}
