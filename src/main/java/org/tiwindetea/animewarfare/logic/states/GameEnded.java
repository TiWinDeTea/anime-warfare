package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.Player;

public class GameEnded extends GameState {
	private final Player winner;

	public GameEnded(Player winner, GameBoard gameBoard) {
		super(gameBoard);
		this.winner = winner;
	}

	@Override
	public void onEnter() {
		// TODO : Send an event to end the game.
	}

	@Override
	public void update() {
		throw new UnsupportedOperationException("End game event should be catched");
	}

	@Override
	public void onExit() {
		throw new UnsupportedOperationException("End game event should be catched");
	}

	@Override
	public State next() {
		throw new UnsupportedOperationException("End game event should be catched");
	}
}
