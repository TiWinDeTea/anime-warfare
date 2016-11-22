package org.tiwindetea.animewarfare.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoardTest {
	private GameBoard gameBoard;
	private final List<FactionType> factions = Arrays.asList(FactionType.NO_NAME,
			FactionType.HAIYORE,
			FactionType.F_CLASS_NO_BAKA,
			FactionType.COOL_GUYS);

	@Before
	public void initialize() {
		this.gameBoard = new GameBoard(this.factions);
	}

	@Test
	public void getPlayers() throws Exception {
		List<FactionType> playerFaction = this.gameBoard.getPlayers().stream().map(p -> p.getFaction()).collect(Collectors.toList());

		Assert.assertEquals(this.factions, playerFaction);
	}

	@Test
	public void getPlayersInOrderClockwise() throws Exception {
		for (int i = 0; i < this.gameBoard.getPlayers().size(); i++) {
			this.gameBoard.initializeTurn(this.gameBoard.getPlayers().get(i), true);

			List<FactionType> factionsCopy = new ArrayList<>(this.factions);
			Collections.rotate(factionsCopy, -i);

			List<FactionType> playerFaction = this.gameBoard.getPlayersInOrder().stream().map(p -> p.getFaction()).collect(Collectors.toList());

			Assert.assertEquals(factionsCopy, playerFaction);
		}
	}

	@Test
	public void getPlayersInOrderAntiClockwise() throws Exception {
		for (int i = 0; i < this.gameBoard.getPlayers().size(); i++) {
			this.gameBoard.initializeTurn(this.gameBoard.getPlayers().get(i), false);

			List<FactionType> factionsCopy = new ArrayList<>(this.factions);
			Collections.rotate(factionsCopy, -(i + 1));
			Collections.reverse(factionsCopy);

			List<FactionType> playerFaction = this.gameBoard.getPlayersInOrder().stream().map(p -> p.getFaction()).collect(Collectors.toList());

			Assert.assertEquals(factionsCopy, playerFaction);
		}
	}

}