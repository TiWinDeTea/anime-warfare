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
