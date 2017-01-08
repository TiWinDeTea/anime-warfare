package org.tiwindetea.animewarfare.logic.event;

import org.lomadriel.lfc.event.Event;

public class StudioAddedEvent implements Event<StudioEventListener> {
	private final int studioID;

	public StudioAddedEvent(int studioID) {
		this.studioID = studioID;
	}

	@Override
	public void notify(StudioEventListener listener) {
		listener.handleStudioAddedEvent(this);
	}

	public int getStudioID() {
		return this.studioID;
	}
}
