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

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;

public class BadLuck extends PlayerCapacity {
	public static class BadLuckActivable extends PlayerActivable implements BattleEventListener {
		public BadLuckActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(BattleEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(BattleEvent.class, this);
		}

		@Override
		public void handlePreBattle(BattleEvent event) {
			// Nothing to do
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
			int deadCount = 0;
			if (event.getBattleContext().getAttacker().getPlayer().equals(getPlayer())) {
				deadCount = event.getBattleContext().getAttacker().getNumberOfDeads();
			} else if (event.getBattleContext().getDefender().getPlayer().equals(getPlayer())) {
				deadCount = event.getBattleContext().getDefender().getNumberOfDeads();
			}

			if (deadCount > 0) {
				activateAndDestroy(new BadLuck(getPlayer()));
			}
		}
	}

	BadLuck(Player player) {
		super(player);
	}

	@Override
	public void use() {
		// TODO
	}

	@Override
	public CapacityName getName() {
		return CapacityName.BAD_LUCK;
	}
}
