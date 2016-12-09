package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

import java.util.EventListener;

public abstract class ActionEvent<T extends EventListener> implements Event<T> {
	private final int playerID;

	protected ActionEvent(int playerID) {
		this.playerID = playerID;
	}

	public final int getPlayerID() {
		return this.playerID;
	}
}
