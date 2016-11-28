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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameBoard {
	private final List<Zone> zones = new ArrayList<>();
	private final List<Player> players = new ArrayList<>();
	private final List<Player> playersInOrder = new ArrayList<>();
	private int lastFirstPlayerIndex;
	private int firstPlayerIndex;
	private boolean clockWiseRotation;

	public GameBoard(Map<Integer, FactionType> players) {
		if (players.size() < 2 && players.size() > 4) {
			throw new IllegalArgumentException("Incorrect number of players, "
					+ players.size()
					+ " player(s) requested, should be between 2 and 4");
		}

		initializePlayers(players);
		initializeZones(players.size());
	}

	public int getLastFirstPlayerIndex() {
		return this.lastFirstPlayerIndex;
	}

	public Player getPlayer(int index) {
		return this.players.get(index);
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(this.players);
	}

	public List<Player> getPlayersInOrder() {
		return Collections.unmodifiableList(this.playersInOrder);
	}

	public List<Player> getPlayersWithMaxStaff() {
		List<Player> playersWithMaxStaff = new ArrayList<>();

		int maxStaff = 0;

		for (Player player : this.players) {
			if (player.getStaffAvailable() > maxStaff) {
				playersWithMaxStaff.clear();
				playersWithMaxStaff.add(player);
				maxStaff = player.getStaffAvailable();
			} else if (player.getStaffAvailable() == maxStaff) {
				playersWithMaxStaff.add(player);
			}
		}

		return playersWithMaxStaff;
	}

	public static List<Integer> getPlayersIndex(List<Player> players) {
		return players.stream().map(player -> Integer.valueOf(player.getID())).collect(Collectors.toList());
	}

	public void initializeTurn(Player firstPlayer, boolean clockWiseRotation) {
		this.lastFirstPlayerIndex = this.firstPlayerIndex;
		this.firstPlayerIndex = this.players.indexOf(firstPlayer);
		this.clockWiseRotation = clockWiseRotation;

		buildPlayerList();
	}

	private void initializePlayers(Map<Integer, FactionType> players) {
		players.entrySet()
		       .stream()
		       .map(entry -> new Player(entry.getKey().intValue(), entry.getValue()))
		       .collect(Collectors.toList()).addAll(this.players);
	}

	private void initializeZones(int numberOfPlayers) {
		// TODO
	}

	private void buildPlayerList() {
		this.playersInOrder.clear();
		if (this.clockWiseRotation) {
			for (int i = 0, j = this.firstPlayerIndex; i < this.players.size(); ++i) {
				this.playersInOrder.add(this.players.get((i + j) % this.players.size()));
			}
		} else {
			for (int i = this.players.size(), j = this.firstPlayerIndex; i > 0; --i) {
				this.playersInOrder.add(this.players.get((i + j) % this.players.size()));
			}
		}
	}

	public List<Zone> getZones() {
		return this.zones;
	}
}
