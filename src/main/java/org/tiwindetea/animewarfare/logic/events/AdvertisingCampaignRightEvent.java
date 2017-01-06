package org.tiwindetea.animewarfare.logic.events;

import org.lomadriel.lfc.event.Event;

public class AdvertisingCampaignRightEvent implements Event<AdvertisingCampaignRightEventListener> {
	public enum Type {
		ADDED,
		REMOVED,
		REVEALED
	}

	private final int playerID;
	private final int weight;
	private final Type type;

	public AdvertisingCampaignRightEvent(Type type, int playerID, int weight) {
		this.playerID = playerID;
		this.weight = weight;
		this.type = type;
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public int getAdvertisingCampaignRightWeight() {
		return this.weight;
	}

	@Override
	public void notify(AdvertisingCampaignRightEventListener listener) {
		if (this.type == Type.ADDED) {
			listener.onAdvertisingCampaignRightAdded(this);
		} else if (this.type == Type.REMOVED) {
			listener.onAdvertisingCampaignRightRemoved(this);
		} else {
			listener.onAdvertisingCampaignRightRevealead(this);
		}
	}
}
