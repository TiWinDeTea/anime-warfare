package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class PlayingOrderChoiceEvent implements Event<PlayingOrderChoiceListener> {
	public final Boolean clockWise;

	public PlayingOrderChoiceEvent(boolean clockWise) {
		this.clockWise = clockWise;
	}

	@Override
	public void notify(PlayingOrderChoiceListener listener) {
		listener.handlePlayingOrder(this);
	}
}
