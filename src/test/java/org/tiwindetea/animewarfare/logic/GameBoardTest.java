package org.tiwindetea.animewarfare.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameBoardTest {
	private GameBoard gameBoard;
	private static final Map<Integer, FactionType> FACTIONS = new HashMap<>();

	static {
		FACTIONS.put(Integer.valueOf(1), FactionType.NO_NAME);
		FACTIONS.put(Integer.valueOf(2), FactionType.HAIYORE);
		FACTIONS.put(Integer.valueOf(3), FactionType.F_CLASS_NO_BAKA);
		FACTIONS.put(Integer.valueOf(4), FactionType.COOL_GUYS);
	}

	@Before
	public void initialize() {
		this.gameBoard = new GameBoard(FACTIONS);
	}

	@Test
	public void getPlayers() throws Exception {
		List<FactionType> playerFaction = this.gameBoard.getPlayers().stream()
				.map(Player::getFaction)
				.collect(Collectors.toList());

		Assert.assertEquals(new ArrayList<>(FACTIONS.values()), playerFaction);
	}

	@Test
	public void getPlayersInOrderClockwise() throws Exception {
		for (int i = 0; i < this.gameBoard.getPlayers().size(); i++) {
			this.gameBoard.initializeTurn(this.gameBoard.getPlayers().get(i), true);

			List<FactionType> factionsCopy = new ArrayList<>(FACTIONS.values());
			Collections.rotate(factionsCopy, -i);

			List<FactionType> playerFaction = this.gameBoard.getPlayersInOrder().stream()
					.map(Player::getFaction)
					.collect(Collectors.toList());

			Assert.assertEquals(factionsCopy, playerFaction);
		}
	}

	@Test
	public void getPlayersInOrderAntiClockwise() throws Exception {
		for (int i = 0; i < this.gameBoard.getPlayers().size(); i++) {
			this.gameBoard.initializeTurn(this.gameBoard.getPlayers().get(i), false);

			List<FactionType> factionsCopy = new ArrayList<>(FACTIONS.values());
			Collections.rotate(factionsCopy, -(i + 1));
			Collections.reverse(factionsCopy);

			List<FactionType> playerFaction = this.gameBoard.getPlayersInOrder().stream()
					.map(Player::getFaction)
					.collect(Collectors.toList());

			Assert.assertEquals(factionsCopy, playerFaction);
		}
	}

}