package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

public class AskPlayingOrderEvent implements Event<AskPlayingOrderEventListener> {
	private final int player;

	public AskPlayingOrderEvent(int player) {
		this.player = player;
	}

	@Override
	public void notify(AskPlayingOrderEventListener listener) {
		listener.askPlayingOrder(this);
	}

	public int getFirstPlayer() {
		return this.player;
	}
}
