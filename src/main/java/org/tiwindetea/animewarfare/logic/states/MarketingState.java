package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.Player;

public class MarketingState extends GameState {
	private Player currentPlayer;

	protected MarketingState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		this.currentPlayer = this.gameBoard.getFirstPlayer();
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
			return new ActionState(this.gameBoard);
		} else {
			return this;
		}*/
		// TODO
		return null;
	}
}
