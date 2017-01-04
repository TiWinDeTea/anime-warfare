////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

package org.tiwindetea.animewarfare.logic;

import org.tiwindetea.animewarfare.logic.units.Studio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Game map model.
 *
 * @author Jérôme BOULMIER
 * @author Benoît CORTIER
 */
public class GameMap {
	private static final int NUMBER_OF_ZONES = 17;
	private static final int[][] DISTANCES = new int[][]{
			//0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14,15,16
			{0, 1, 2, 2, 3, 3, 2, 1, 1, 3, 3, 2, 1, 2, 1, 2, 1}, // 0
			{0, 0, 1, 1, 2, 1, 3, 2, 2, 2, 3, 2, 3, 3, 2, 2, 1}, // 1
			{0, 0, 0, 1, 1, 2, 3, 2, 1, 1, 3, 3, 3, 2, 2, 3, 2}, // 2
			{0, 0, 0, 0, 2, 1, 3, 1, 2, 1, 2, 2, 2, 3, 3, 3, 2}, // 3
			{0, 0, 0, 0, 0, 3, 4, 3, 2, 2, 4, 4, 4, 3, 3, 4, 3}, // 4
			{0, 0, 0, 0, 0, 0, 2, 2, 3, 1, 1, 1, 2, 3, 3, 2, 2}, // 5
			{0, 0, 0, 0, 0, 0, 0, 2, 2, 3, 1, 1, 1, 1, 2, 1, 2}, // 6
			{0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 2, 1, 2, 2, 3, 2}, // 7
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 2, 1, 1, 2, 2}, // 8
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 3, 3, 3, 3, 3}, // 9
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2}, // 10
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1}, // 11
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2}, // 12
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2}, // 13
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}, // 14
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, // 15
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 16
	};

	static {
		for (int i = 0; i < NUMBER_OF_ZONES - 1; i++) {
			for (int j = i + 1; j < NUMBER_OF_ZONES; j++) {
				DISTANCES[j][i] = DISTANCES[i][j];
			}
		}
	}

	private final List<Zone> zones;

	public GameMap(int numberOfPlayers) {
		this.zones = initializeZones(numberOfPlayers);
	}

	private static List<Zone> initializeZones(int numberOfPlayers) {
		List<Zone> zones = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_ZONES; i++) {
			zones.add(new Zone(i, i < NUMBER_OF_ZONES / 2 ? true : false));
		}

		return zones;
	}

	public static int getDistanceBetween(int source, int destination) {
		return DISTANCES[source][destination];
	}

	public static boolean areAdjacent(int source, int destination) {
		return DISTANCES[source][destination] == 1;
	}

	public Zone getZone(int id) {
		return this.zones.get(id);
	}

	public Zone getRandomZone(int exceptionID) {
		Random random = new Random();
		int selected;
		do {
			selected = random.nextInt(this.zones.size());
		} while (selected == exceptionID);

		return this.zones.get(selected);
	}

	public boolean isValid(int zoneID) {
		return !(zoneID < 0 || (zoneID >= this.zones.size()));
	}

	public List<Studio> getStudios() {
		return this.zones.stream()
		                 .map(Zone::getStudio)
		                 .filter(Objects::nonNull)
		                 .collect(Collectors.toList());
	}
}
