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

import com.esotericsoftware.minlog.Log;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.battle.BattleContext;
import org.tiwindetea.animewarfare.logic.battle.BattleSide;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;
import org.tiwindetea.animewarfare.net.logicevent.SelectUnitsEvent;
import org.tiwindetea.animewarfare.net.logicevent.SelectUnitsEventListener;

/*
 * @author Benoît CORTIER
 */
public class ForcedRetreat extends PlayerCapacity implements BattleEventListener, SelectUnitsEventListener {
	public static class ForcedRetreatActivable extends PlayerActivable implements UnitMovedEventListener {
		public ForcedRetreatActivable(Player player) {
			super(player);
			LogicEventDispatcher.registerListener(UnitMovedEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitMovedEvent.class, this);
		}

		@Override
		public void handleUnitMoved(UnitMovedEvent event) {
			if (event.getUnit().isLevel(UnitLevel.HERO)) {
				if (isHeroInTheSameZoneThanAnEnnemyHero(event)) {
					activateAndDestroy(new ForcedRetreat(getPlayer()));
				}
			}
		}

		private boolean isHeroInTheSameZoneThanAnEnnemyHero(UnitMovedEvent event) {
			boolean ennemyPresent = false;
			boolean heroPresent = false;

			for (Unit unit : event.getDestination().getUnits()) {
				if (unit.isLevel(UnitLevel.HERO)) {
					if (unit.hasFaction(getPlayer().getFaction())) {
						heroPresent = true;
					} else {
						ennemyPresent = true;
					}
				}
			}

			return ennemyPresent && heroPresent;
		}
	}

	private BattleContext battleContext;

	ForcedRetreat(Player player) {
		super(player);

		LogicEventDispatcher.registerListener(BattleEvent.class, this);
	}

	@Override
	public void handlePreBattle(BattleEvent event) {
		this.battleContext = event.getBattleContext();
		LogicEventDispatcher.registerListener(SelectUnitsEvent.class, this);
	}

	@Override
	public void handleDuringBattle(BattleEvent event) {
		// nothing to do.
	}

	@Override
	public void handlePostBattle(BattleEvent event) {
		// nothing to do.
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {
		// nothing to do.
	}

	@Override
	public void handleSelectUnits(SelectUnitsEvent event) {
		if (event.getUnits().size() != 1) {
			Log.debug(getClass().getName(), "One and only one unit should be provided.");
			return;
		}

		if (!getPlayer().hasRequiredStaffPoints(getName().getStaffCost())) {
			Log.debug(getClass().getName().toString(), "Not enough staff points.");
			return;
		}

		for (BattleSide battleSide : this.battleContext.getBattleSides()) {
			for (Unit unit : battleSide.getUnits()) {
				if (event.getUnits().contains(unit)) {
					if (unit.getType().getUnitLevel().equals(UnitLevel.HERO)) {
						Log.debug(getClass().getName().toString(), "Doesn't works against heros.");
						return;
					} else {
						battleSide.removeUnit(unit);
						break;
					}
				}
			}
		}

		getPlayer().decrementStaffPoints(getName().getStaffCost());
		LogicEventDispatcher.unregisterListener(SelectUnitsEvent.class, this);
	}

	@Override
	public void use() {
		this.battleContext = null;
	}

	@Override
	public void destroy() {
		LogicEventDispatcher.unregisterListener(BattleEvent.class, this);
		this.battleContext = null;
	}

	@Override
	public CapacityName getName() {
		return CapacityName.FORCED_RETREAT;
	}
}
