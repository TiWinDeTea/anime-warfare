package org.tiwindetea.animewarfare.logic.event;

import org.lomadriel.lfc.event.Event;

public class StudioEvent implements Event<StudioEventListener> {
	private final int zoneID;
	private final int playerID;
	private final Type type;

	public enum Type {
		ADDED,
		ADDED_PLAYER,
		REMOVED,
		REMOVED_PLAYER,
	}

	public StudioEvent(Type type, int ID) {
		this.type = type;

		if (this.type == Type.ADDED_PLAYER && this.type == Type.REMOVED_PLAYER) {
			this.playerID = ID;
			this.zoneID = -1;
		} else {
			this.zoneID = ID;
			this.playerID = -1;
		}
	}

	@Override
	public void notify(StudioEventListener listener) {
		if (this.type == Type.ADDED || this.type == Type.ADDED_PLAYER) {
			listener.handleStudioAddedEvent(this);
		} else {
			listener.handleStudioRemovedEvent(this);
		}
	}

	public int getZoneID() {
		if (this.zoneID == -1) {
			throw new IllegalStateException();
		}

		return this.zoneID;
	}

	public int getPlayerID() {
		if (this.playerID == -1) {
			throw new IllegalStateException();
		}

		return this.playerID;
	}

	public Type getType() {
		return this.type;
	}
}
