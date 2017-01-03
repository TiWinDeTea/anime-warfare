package org.tiwindetea.animewarfare.logic.event;

import org.lomadriel.lfc.event.Event;

public class StudioEvent implements Event<StudioEventListener> {
	private final int studioID;
	private final Type type;

	public enum Type {
		ADDED,
		REMOVED
	}

	public StudioEvent(Type type, int studioID) {
		this.studioID = studioID;
		this.type = type;
	}

	@Override
	public void notify(StudioEventListener listener) {
		if (this.type == Type.ADDED) {
			listener.handleStudioAddedEvent(this);
		} else {
			listener.handleStudioRemovedEvent(this);
		}
	}

	public int getStudioID() {
		return this.studioID;
	}
}
