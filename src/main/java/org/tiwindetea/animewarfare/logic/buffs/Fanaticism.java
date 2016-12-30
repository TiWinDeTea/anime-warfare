package org.tiwindetea.animewarfare.logic.buffs;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRightsPool;
import org.tiwindetea.animewarfare.logic.states.events.ConventionOrganizedEvent;
import org.tiwindetea.animewarfare.logic.states.events.ConventionOrganizedEventListener;

public class Fanaticism extends Buff implements ConventionOrganizedEventListener {
	private final AdvertisingCampaignRightsPool pool;

	public Fanaticism(AdvertisingCampaignRightsPool pool) {
		super(1);
		this.pool = pool;

		EventDispatcher.registerListener(ConventionOrganizedEvent.class, this);
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		EventDispatcher.unregisterListener(ConventionOrganizedEvent.class, this);
	}

	@Override
	public void onConventionOrganized(ConventionOrganizedEvent event) {
		event.getPlayer().addAdvertisingCampaignRights(this.pool.getAdvertisingCampaignRight());
	}
}
