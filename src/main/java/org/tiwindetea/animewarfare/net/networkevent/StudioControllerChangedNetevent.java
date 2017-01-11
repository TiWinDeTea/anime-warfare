package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStudioControllerChanged;

public class StudioControllerChangedNetevent implements Event<StudioControllerChangedNeteventListener> {
	private final int controllerID;
	private final FactionType controllerFaction;
	private final int zoneID;

	public StudioControllerChangedNetevent(NetStudioControllerChanged studioControllerChanged) {
		this.zoneID = studioControllerChanged.getZoneID();
		this.controllerFaction = studioControllerChanged.getControllerFaction();
		this.controllerID = studioControllerChanged.getControllerID();
	}

	@Override
	public void notify(StudioControllerChangedNeteventListener listener) {
		listener.handleStudioControllerChanged(this);
	}

	public int getControllerID() {
		return this.controllerID;
	}

	public FactionType getControllerFaction() {
		return this.controllerFaction;
	}

	public int getZoneID() {
		return this.zoneID;
	}
}
