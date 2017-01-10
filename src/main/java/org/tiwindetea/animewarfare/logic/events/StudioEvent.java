package org.tiwindetea.animewarfare.logic.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.units.Studio;

public class StudioEvent implements Event<StudioEventListener> {
	private int zoneID;
	private int playerID;
	private Type type;

	private Studio studio;

	public enum Type {
		CREATED,
		ADDED_TO_MAP,
		ADDED_TO_PLAYER,
		REMOVED_FROM_MAP,
		REMOVED_FROM_PLAYER,
		DELETED
	}

	private StudioEvent(Studio studio, Type type) {
		this.zoneID = this.playerID = -1;
		this.type = type;
		this.studio = studio;
	}

	public static StudioEvent created(Studio studio, int zoneID, int playerID) {
		StudioEvent event = new StudioEvent(studio, Type.CREATED);
		event.zoneID = zoneID;
		event.playerID = playerID;
		return event;
	}

	public static StudioEvent deleted(Studio studio, int zoneID) {
		StudioEvent event = new StudioEvent(studio, Type.DELETED);
		event.zoneID = zoneID;
		return event;
	}

	public static StudioEvent addedToMap(Studio studio, int zoneID) {
		StudioEvent event = new StudioEvent(studio, Type.ADDED_TO_MAP);
		event.zoneID = zoneID;
		return event;
	}

	public static StudioEvent removedFromMap(Studio studio, int zoneID) {
		StudioEvent event = new StudioEvent(studio, Type.REMOVED_FROM_MAP);
		event.zoneID = zoneID;
		return event;
	}

	public static StudioEvent addedToPlayer(Studio studio, int playerID) {
		StudioEvent event = new StudioEvent(studio, Type.ADDED_TO_PLAYER);
		event.playerID = playerID;
		return event;
	}

	public static StudioEvent removedFromPlayer(Studio studio, int playerID) {
		StudioEvent event = new StudioEvent(studio, Type.REMOVED_FROM_PLAYER);
		event.playerID = playerID;
		return event;
	}

	@Override
	public void notify(StudioEventListener listener) {
		if (this.type == Type.ADDED_TO_MAP || this.type == Type.ADDED_TO_PLAYER) {
			listener.handleStudioAddedEvent(this);
		} else if (this.type == Type.REMOVED_FROM_MAP || this.type == Type.REMOVED_FROM_PLAYER) {
			listener.handleStudioRemovedEvent(this);
		} else {
			listener.handleStudioBuiltOrDestroyed(this);
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

	public Studio getStudio() {
		return this.studio;
	}

	public Type getType() {
		return this.type;
	}
}
