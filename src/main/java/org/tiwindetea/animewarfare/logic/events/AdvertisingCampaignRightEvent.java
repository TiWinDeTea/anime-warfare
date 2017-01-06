package org.tiwindetea.animewarfare.logic.events;

import org.lomadriel.lfc.event.Event;

import java.util.EventListener;

abstract class AdvertisingCampaignRightEvent<T extends EventListener> implements Event<T> {
	private final int playerID;
	private final int weight;

	public AdvertisingCampaignRightEvent(int playerID, int weight) {
		this.playerID = playerID;
		this.weight = weight;
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public int getAdvertisingCampaignRightWeight() {
		return this.weight;
	}
}
