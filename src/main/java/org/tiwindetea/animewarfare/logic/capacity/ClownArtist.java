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

import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.battle.BattleSide;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEventListener;

public class ClownArtist extends PlayerCapacity implements BattleEventListener {
	public static class ClownArtistActivable extends PlayerActivable implements StudioEventListener {
		private final GameMap map;

		private int numberOfControlledStudioInTown;
		private int numberOfStudioInTown;

		public ClownArtistActivable(Player player, GameMap map) {
			super(player);
			this.map = map;

			LogicEventDispatcher.registerListener(StudioEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(StudioEvent.class, this);
		}

		@Override
		public void handleStudioAddedEvent(StudioEvent event) {
			Zone zone = this.map.getZone(event.getZoneID());

			if (!zone.isCountrySide()) {
				++this.numberOfStudioInTown;

				if (getPlayer().hasFaction(zone.getStudio().getCurrentFaction())) {
					++this.numberOfControlledStudioInTown;
				}

				if (this.numberOfControlledStudioInTown >= 3 || this.numberOfStudioInTown >= 4) {
					activateAndDestroy(new ClownArtist(getPlayer()));
				}
			}
		}

		@Override
		public void handleStudioRemovedEvent(StudioEvent event) {
			Zone zone = this.map.getZone(event.getZoneID());

			if (!zone.isCountrySide()) {
				--this.numberOfStudioInTown;

				if (getPlayer().hasFaction(zone.getStudio().getCurrentFaction())) {
					--this.numberOfControlledStudioInTown;
				}
			}
		}
	}

	private static final int COST = 2;
	private BattleSide enemy;

	ClownArtist(Player player) {
		super(player);

		LogicEventDispatcher.registerListener(BattleEvent.class, this);
	}

	@Override
	public void use() {
		if (!getPlayer().hasRequiredStaffPoints(COST) || this.enemy == null) {
			return;
		}

		this.enemy.decrementAttack(this.enemy.getAttack() / 2);
	}

	@Override
	public void destroy() {
		LogicEventDispatcher.unregisterListener(BattleEvent.class, this);
	}

	@Override
	public void handlePreBattle(BattleEvent event) {
		LogicEventDispatcher.unregisterListener(BattleEvent.class, this);

		if (event.getBattleContext().getAttacker().equals(getPlayer())) {
			this.enemy = event.getBattleContext().getDefender();
		} else if (event.getBattleContext().getDefender().equals(getPlayer())) {
			this.enemy = event.getBattleContext().getAttacker();
		}
	}

	@Override
	public void handleDuringBattle(BattleEvent event) {
		// Nothing to do
	}

	@Override
	public void handlePostBattle(BattleEvent event) {
		// Nothing to do
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {
		// Nothing to do
	}

	@Override
	public CapacityName getName() {
		return CapacityName.CLOWN_ARTIST;
	}
}
