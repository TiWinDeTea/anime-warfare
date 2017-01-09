////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRightsPool;
import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.net.logicevent.ActivateFlyingStudioEvent;
import org.tiwindetea.animewarfare.net.logicevent.ActivateFlyingStudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.FlyingStudioZoneChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.FlyingStudioZoneChoiceEventListener;

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
						this.pool.addAdvertisingCampaignRightToPlayer(getPlayer());
					}
				}

				activateAndDestroy(new FlyingStudio(getPlayer(), this.gameMap));
			}
		}
	}

	private final GameMap gameMap;

	FlyingStudio(Player player, GameMap gameMap) {
		super(player);
		this.gameMap = gameMap;
	}

	@Override
	public void use() {
		if (!getPlayer().hasRequiredStaffPoints(getName().getStaffCost())) {
			return;
		}

		LogicEventDispatcher.registerListener(FlyingStudioZoneChoiceEvent.class, this);
	}

	@Override
	public void destroy() {
		LogicEventDispatcher.unregisterListener(FlyingStudioZoneChoiceEvent.class, this);
	}

	@Override
	public void handleZoneChoice(FlyingStudioZoneChoiceEvent event) {
		LogicEventDispatcher.unregisterListener(FlyingStudioZoneChoiceEvent.class, this);

		if (this.gameMap.isValid(event.getID())) {
			Zone zone = this.gameMap.getZone(event.getID());
			Studio studio = zone.getStudio();

			if (studio != null) {
				if (getPlayer().hasFaction(studio.getCurrentFaction())) {
					getPlayer().decrementStaffPoints(getName().getStaffCost());
					zone.setStudio(null);
					getPlayer().setStudio(studio);
				}
			} else {
				studio = getPlayer().getStudio();
				if (studio != null) {
					getPlayer().decrementStaffPoints(getName().getStaffCost());
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
