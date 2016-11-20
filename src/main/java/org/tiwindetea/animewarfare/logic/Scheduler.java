package org.tiwindetea.animewarfare.logic;

import org.lomadriel.lfc.statemachine.DefaultStateMachine;
import org.lomadriel.lfc.statemachine.StateMachine;
import org.tiwindetea.animewarfare.logic.states.FirstTurnHiringState;

public class Scheduler {
	private final StateMachine stateMachine;

	public Scheduler(int numberOfPlayers) {
		this.stateMachine = new DefaultStateMachine(new FirstTurnHiringState(new GameBoard(numberOfPlayers)));
	}
}
