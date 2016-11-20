package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;

abstract class GameState implements State {
	protected final GameBoard gameBoard;

	protected GameState(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}
}
