package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class ActivateFlyingStudioEvent implements Event<ActivateFlyingStudioEventListener> {
	private final int playerID;

	public ActivateFlyingStudioEvent(int playerID) {
		this.playerID = playerID;
	}

	@Override
	public void notify(ActivateFlyingStudioEventListener listener) {
		listener.onFlyingStudioActivationRequest(this);
	}

	public int getPlayerID() {
		return this.playerID;
	}
}
