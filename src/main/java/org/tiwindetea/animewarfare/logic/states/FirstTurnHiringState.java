package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;

public class FirstTurnHiringState extends StaffHiringState {
	public FirstTurnHiringState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public State next() {
		return new FirstTurnFirstPlayerSelectionState(this.gameBoard);
	}
}
