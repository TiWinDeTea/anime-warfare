package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRightsPool;
import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.capacity.events.ActivateFlyingStudioEvent;
import org.tiwindetea.animewarfare.logic.capacity.events.ActivateFlyingStudioEventListener;
import org.tiwindetea.animewarfare.logic.capacity.events.FlyingStudioZoneChoiceEvent;
import org.tiwindetea.animewarfare.logic.capacity.events.FlyingStudioZoneChoiceEventListener;
import org.tiwindetea.animewarfare.logic.units.Studio;

import java.lang.ref.WeakReference;
import java.util.List;

public class FlyingStudio extends PlayerCapacity implements FlyingStudioZoneChoiceEventListener {
	public static class FlyingStudioActivable extends PlayerActivable implements ActivateFlyingStudioEventListener {
		private final List<WeakReference<Player>> players;
		private final AdvertisingCampaignRightsPool pool;
		private final GameMap gameMap;

		public FlyingStudioActivable(Player player,
		                             List<WeakReference<Player>> players,
		                             AdvertisingCampaignRightsPool pool, GameMap gameMap) {
			super(player);
			this.players = players;
			this.pool = pool;
			this.gameMap = gameMap;

			LogicEventDispatcher.registerListener(ActivateFlyingStudioEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(ActivateFlyingStudioEvent.class, this);
			this.players.clear();
		}

		@Override
		public void onFlyingStudioActivationRequest(ActivateFlyingStudioEvent event) {
			if (event.getPlayerID() == getPlayer().getID()) {
				for (WeakReference<Player> playerWeakReference : this.players) {
					Player player = playerWeakReference.get();
					if (player != null && player.getID() != getPlayer().getID()
							&& player.getActivatedCapacities().size() == 6) {
						getPlayer().addAdvertisingCampaignRights(this.pool.getAdvertisingCampaignRight());
					}
				}

				activateAndDestroy(new FlyingStudio(getPlayer(), this.gameMap));
			}
		}
	}

	private static final int COST = 1;
	private final GameMap gameMap;

	FlyingStudio(Player player, GameMap gameMap) {
		super(player);
		this.gameMap = gameMap;
	}

	@Override
	public void use() {
		if (!getPlayer().hasRequiredStaffPoints(COST)) {
			return;
		}

		LogicEventDispatcher.registerListener(FlyingStudioZoneChoiceEvent.class, this);
	}

	@Override
	public void handleZoneChoice(FlyingStudioZoneChoiceEvent event) {
		LogicEventDispatcher.unregisterListener(FlyingStudioZoneChoiceEvent.class, this);

		if (this.gameMap.isValid(event.getID())) {
			Zone zone = this.gameMap.getZone(event.getID());
			Studio studio = zone.getStudio();

			if (studio != null) {
				if (getPlayer().hasFaction(studio.getCurrentFaction())) {
					getPlayer().decrementStaffPoints(COST);
					zone.setStudio(null);
					getPlayer().setStudio(studio);
				}
			} else {
				studio = getPlayer().getStudio();
				if (studio != null) {
					getPlayer().decrementStaffPoints(COST);
					getPlayer().setStudio(null);
					zone.setStudio(studio);
				}
			}
		}
	}

	@Override
	public CapacityName getName() {
		return CapacityName.FLYING_STUDIO;
	}
}
