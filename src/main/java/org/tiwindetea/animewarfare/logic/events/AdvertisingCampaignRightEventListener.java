package org.tiwindetea.animewarfare.logic.events;

import java.util.EventListener;

public interface AdvertisingCampaignRightEventListener extends EventListener {
	void onAdvertisingCampaignRightAdded(AdvertisingCampaignRightEvent event);

	void onAdvertisingCampaignRightRemoved(AdvertisingCampaignRightEvent event);

	void onAdvertisingCampaignRightRevealead(AdvertisingCampaignRightEvent event);
}
