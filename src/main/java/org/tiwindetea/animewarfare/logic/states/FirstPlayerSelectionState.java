package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.Player;

import java.util.ArrayList;
import java.util.List;

public class FirstPlayerSelectionState extends GameState {
	private Player firstPlayer;
	private Boolean clockWise;

	public FirstPlayerSelectionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		int maxStaff = 0;
		List<Player> maxPlayers = new ArrayList<>();

		for (Player player : this.gameBoard.getPlayers()) {
			if (player.getStaffAvailable() > maxStaff) {
				maxPlayers.clear();
				maxPlayers.add(player);
				maxStaff = player.getStaffAvailable();
			} else if (player.getStaffAvailable() == maxStaff) {
				maxPlayers.add(player);
			}
		}

		if (maxPlayers.size() == 1) {
			this.firstPlayer = maxPlayers.get(0);
		} else {
			// TODO: Asks the last first player to get the new first player.
		}
	}

	@Override
	public void update() {
		// Nothing to do.
	}

	@Override
	public void onExit() {
		// Nothing to do.
	}

	public State next() {
		if (this.firstPlayer == null || this.clockWise == null) {
			throw new RuntimeException("Turn not initialized");
		}

		return new MarketingState(this.gameBoard);
	}

	// TODO: Handle the first player
	// TODO: Handle the playing order
}
