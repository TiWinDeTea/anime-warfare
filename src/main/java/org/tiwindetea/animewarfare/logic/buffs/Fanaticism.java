package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRightsPool;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.states.events.ConventionOrganizedEvent;
import org.tiwindetea.animewarfare.logic.states.events.ConventionOrganizedEventListener;

public class Fanaticism extends Buff implements ConventionOrganizedEventListener {
	private final AdvertisingCampaignRightsPool pool;

	public Fanaticism(AdvertisingCampaignRightsPool pool) {
		super(1);
		this.pool = pool;

		LogicEventDispatcher.registerListener(ConventionOrganizedEvent.class, this);
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		LogicEventDispatcher.unregisterListener(ConventionOrganizedEvent.class, this);
	}

	@Override
	public void onConventionOrganized(ConventionOrganizedEvent event) {
		this.pool.addAdvertisingCampaignRightToPlayer(event.getPlayer());
	}
}
