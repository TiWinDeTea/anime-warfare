package org.tiwindetea.animewarfare.net.networkrequests.server;

import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEvent;

public class NetStudioControllerChanged {
	private final int zoneID;
	private final FactionType controllerFaction;
	private final int controllerID;

	/* For Kryo net */
	public NetStudioControllerChanged() {
		this.zoneID = 0;
		this.controllerFaction = null;
		this.controllerID = 0;
	}

	public NetStudioControllerChanged(StudioControllerChangedEvent event) {
		this.zoneID = event.getZoneID();
		this.controllerFaction = event.getControllerFaction();
		this.controllerID = event.getControllerID();
	}

	public int getZoneID() {
		return this.zoneID;
	}

	public FactionType getControllerFaction() {
		return this.controllerFaction;
	}

	public int getControllerID() {
		return this.controllerID;
	}
}
