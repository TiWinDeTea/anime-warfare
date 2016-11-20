package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

public class GameEndedEvent implements Event<GameEndedEventListener> {
	private final Integer winner;

	public GameEndedEvent(Integer winner) {
		this.winner = winner;
	}

	@Override
	public void notify(GameEndedEventListener listener) {
		listener.handleGameEndedEvent(this);
	}

	public Integer getWinner() {
		return this.winner;
	}
}
