package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class OpenStudioEvent implements Event<OpenStudioEventListener> {
	private final int playerID;
	private final int zone;

	public OpenStudioEvent(int playerID, int zone) {
		this.playerID = playerID;
		this.zone = zone;
	}

	@Override
	public void notify(OpenStudioEventListener listener) {
		listener.handleOpenStudioEvent(this);
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public int getZone() {
		return this.zone;
	}
}
