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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameMap {
	private final int[][] distances;
	private final List<Zone> zones;

	public GameMap(int numberOfPlayers) {
		this.zones = initializeZones(numberOfPlayers);
		this.distances = initializeDistances(this.zones);
	}

	private static List<Zone> initializeZones(int numberOfPlayers) {
		// TODO: Sends events
		return null;
	}

	private static int[][] initializeDistances(List<Zone> zones) {
		return null;
	}

	public int getDistanceBetween(int source, int destination) {
		return this.distances[source][destination];
	}

	public boolean areAdjacent(int source, int destination) {
		return this.distances[source][destination] == 1;
	}

	public Zone getZone(int id) {
		return this.zones.get(id);
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
