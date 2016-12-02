package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class SkipTurnEvent implements Event<SkipTurnEventListener> {
	private final int playerID;

	public SkipTurnEvent(int playerID) {
		this.playerID = playerID;
	}

	@Override
	public void notify(SkipTurnEventListener listener) {
		listener.handleSkipTurnEvent(this);
	}

	public int getPlayerID() {
		return this.playerID;
	}
}
