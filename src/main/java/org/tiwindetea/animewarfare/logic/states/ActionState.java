package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;

public class ActionState extends GameState {
	public ActionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		// TODO
	}

	@Override
	public void update() {
		// TODO
	}

	@Override
	public void onExit() {
		// TODO
	}

	public State next() {
		/*if (gameEnded) {
			return new GameEnded(winner, this.gameBoard);
		} else if (phaseEnded) {
			return new StaffHiringState(this.gameBoard);
		} else {
			return this;
		}*/

		// TODO
		return null;
	}

}
