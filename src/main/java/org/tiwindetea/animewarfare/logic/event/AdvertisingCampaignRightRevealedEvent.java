package org.tiwindetea.animewarfare.logic.event;

public class AdvertisingCampaignRightRevealedEvent extends AdvertisingCampaignRightEvent<AdvertisingCampaignRightRevealedEventListener> {
	public AdvertisingCampaignRightRevealedEvent(int id, int weight) {
		super(id, weight);
	}

	@Override
	public void notify(AdvertisingCampaignRightRevealedEventListener listener) {
		listener.onAdvertisingCampaignRightRevealead(this);
	}
}
