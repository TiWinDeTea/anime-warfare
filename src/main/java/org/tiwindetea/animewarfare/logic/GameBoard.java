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

public class GameBoard {
	private final List<Zone> zones = new ArrayList<>();
	private final List<Player> players = new ArrayList<>();

	private Player lastFirstPlayer;
	private Player firstPlayer;
	private boolean clockWiseRotation;

	public GameBoard(int numberOfPlayers) {
		if (numberOfPlayers < 0 && numberOfPlayers > 4) {
			throw new IllegalArgumentException("Incorrect number of players, "
					+ numberOfPlayers
					+ " players requested, should be between 0 and 4");
		}

		initializePlayers(numberOfPlayers);
		initializeZones(numberOfPlayers);
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(this.players);
	}

	public void initalizeTurn(Player firstPlayer, boolean clockWiseRotation) {
		this.firstPlayer = firstPlayer;
		this.clockWiseRotation = clockWiseRotation;

	}

	public Player getFirstPlayer() {
		return this.firstPlayer;
	}

	public Player getNextPlayer() {
		// TODO
		return null;
	}

	private void initializePlayers(int numberOfPlayers) { // FIXME Should be the faction type.
		for (int i = 0; i < numberOfPlayers; ++i) {
			this.players.add(new Player());
		}
	}

	private void initializeZones(int numberOfPlayers) {
		// TODO
	}
}
