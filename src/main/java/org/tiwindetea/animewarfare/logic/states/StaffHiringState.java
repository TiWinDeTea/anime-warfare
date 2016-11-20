package org.tiwindetea.animewarfare.logic.states;

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;

public class StaffHiringState extends GameState {
	public StaffHiringState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		// TODO
		computeStaffAvailable();
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
		return new FirstPlayerSelectionState(this.gameBoard);
	}

	protected void computeStaffAvailable() {
		// TODO
	}
}
