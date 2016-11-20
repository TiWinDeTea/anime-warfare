package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;

public class FirstTurnFirstPlayerSelectionState extends FirstPlayerSelectionState {
	public FirstTurnFirstPlayerSelectionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		// TODO: Select a player randomly.
	}

	@Override
	public State next() {
		return new ActionState(this.gameBoard);
	}
}
