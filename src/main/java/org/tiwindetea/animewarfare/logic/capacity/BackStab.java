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
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEventListener;

public class BackStab extends PlayerCapacity implements BattleEventListener {
	public static class BackStabActivable extends PlayerActivable implements FirstPlayerSelectedEventListener {
		private final GameMap map;

		public BackStabActivable(Player player, GameMap map) {
			super(player);
			this.map = map;

			LogicEventDispatcher.registerListener(FirstPlayerSelectedEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(FirstPlayerSelectedEvent.class, this);
		}

		@Override
		public void firstPlayerSelected(FirstPlayerSelectedEvent event) {
			if (getPlayer().getID() == event.getFirstPlayer()) {
				activateAndDestroy(new BackStab(getPlayer(), this.map));
			}
		}
	}

	private final GameMap map;
	private Zone battleZone;
	private BattleSide me;
	private BattleSide enemy;

	BackStab(Player player, GameMap map) {
		super(player);
		this.map = map;

		LogicEventDispatcher.registerListener(BattleEvent.class, this);
	}

	// Vos personnages niveau LV0 adjacents à la zone d'affrontement peuvent humilier les ennemis à coup sûr.
	@Override
	public void use() {
		if (!getPlayer().hasRequiredStaffPoints(getName().getStaffCost()) || this.battleZone == null) {
			return;
		}

		getPlayer().decrementStaffPoints(getName().getStaffCost());
	}

	@Override
	public void destroy() {
	}

	@Override
	public void handlePreBattle(BattleEvent event) {
	}

	@Override
	public void handleDuringBattle(BattleEvent event) {

	}

	@Override
	public void handlePostBattle(BattleEvent event) {
		LogicEventDispatcher.unregisterListener(BattleEvent.class, this);

		if (event.getBattleContext().getAttacker().getPlayer().equals(getPlayer())) {
			this.me = event.getBattleContext().getAttacker();
			this.enemy = event.getBattleContext().getDefender();
			this.battleZone = event.getBattleContext().getZone();
		} else if (event.getBattleContext().getDefender().getPlayer().equals(getPlayer())) {
			this.me = event.getBattleContext().getDefender();
			this.enemy = event.getBattleContext().getAttacker();
			this.battleZone = event.getBattleContext().getZone();
		}
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {

	}

	@Override
	public CapacityName getName() {
		return CapacityName.BACK_STAB;
	}
}
