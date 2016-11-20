package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class PlayingOrderChoiceEvent implements Event<PlayingOrderChoiceListener> {
	private final Boolean clockWise;

	public PlayingOrderChoiceEvent(boolean clockWise) {
		this.clockWise = Boolean.valueOf(clockWise);
	}

	@Override
	public void notify(PlayingOrderChoiceListener listener) {
		listener.handlePlayingOrder(this);
	}

	public Boolean getClockWise() {
		return this.clockWise;
	}
}
