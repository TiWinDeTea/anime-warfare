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
