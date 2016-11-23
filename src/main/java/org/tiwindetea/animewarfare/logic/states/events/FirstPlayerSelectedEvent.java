package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

public class FirstPlayerSelectedEvent implements Event<FirstPlayerSelectedEventListener> {
	private final int player;

	public FirstPlayerSelectedEvent(int player) {
		this.player = player;
	}

	@Override
	public void notify(FirstPlayerSelectedEventListener listener) {
		listener.firstPlayerSelected(this);
	}

	public int getFirstPlayer() {
		return this.player;
	}
}
