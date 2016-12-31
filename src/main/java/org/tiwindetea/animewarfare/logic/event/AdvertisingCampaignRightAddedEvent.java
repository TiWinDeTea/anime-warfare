package org.tiwindetea.animewarfare.logic.event;

public class AdvertisingCampaignRightAddedEvent extends AdvertisingCampaignRightEvent<AdvertisingCampaignRightAddedEventListener> {
	public AdvertisingCampaignRightAddedEvent(int playerID, int advertisingCampaignRight) {
		super(playerID, advertisingCampaignRight);
	}

	@Override
	public void notify(AdvertisingCampaignRightAddedEventListener listener) {
		listener.onAdvertisingCampaignRightAdded(this);
	}
}
