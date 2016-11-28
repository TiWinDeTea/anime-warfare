package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

import java.util.List;

public class GameEndedEvent implements Event<GameEndedEventListener> {
	private final List<Integer> winner;

	public GameEndedEvent(List<Integer> winner) {
		this.winner = winner;
	}

	@Override
	public void notify(GameEndedEventListener listener) {
		listener.handleGameEndedEvent(this);
	}

	/**
	 * Returns winners' id or an empty list if there is no winner.
	 *
	 * @return winners' id or an empty list if there is no winner.
	 */
	public List<Integer> getWinners() {
		return this.winner;
	}
}
