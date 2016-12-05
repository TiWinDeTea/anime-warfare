package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class PlayingOrderChoiceEvent implements Event<PlayingOrderChoiceEventListener> {
	private final Boolean clockWise;

	public PlayingOrderChoiceEvent(boolean clockWise) {
		this.clockWise = Boolean.valueOf(clockWise);
	}

	@Override
	public void notify(PlayingOrderChoiceEventListener listener) {
		listener.handlePlayingOrder(this);
	}

	public Boolean isClockWiseTurn() {
		return this.clockWise;
	}
}
