package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRightsPool;
import org.tiwindetea.animewarfare.logic.states.events.ConventionOrganizedEvent;
import org.tiwindetea.animewarfare.logic.states.events.ConventionOrganizedEventListener;

public class Fanaticism extends Buff implements ConventionOrganizedEventListener {
	private final AdvertisingCampaignRightsPool pool;

	public Fanaticism(AdvertisingCampaignRightsPool pool) {
		super(1);
		this.pool = pool;
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
	}

	@Override
	public void onConventionOrganized(ConventionOrganizedEvent event) {
		event.getPlayer().addAdvertisingCampaignRights(this.pool.getAdvertisingCampaignRight());
	}
}
