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
import org.tiwindetea.animewarfare.logic.buffs.HidingInTheBushBuff;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEventListener;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.logicevent.HidingInTheBushUnitsChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.HidingInTheBushUnitsChoiceEventListener;

import java.util.HashSet;
import java.util.Set;

public class HidingInTheBush extends PlayerCapacity implements PhaseChangedEventListener, HidingInTheBushUnitsChoiceEventListener {
	public static class HidingInTheBushActivable extends PlayerActivable implements UnitCounterEventListener {
		private final GameMap map;

		public HidingInTheBushActivable(Player player, GameMap map) {
			super(player);
			this.map = map;

			LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitCounterEvent event) {
			if (event.getUnitType() == UnitType.CTHUKO) {
				activateAndDestroy(new HidingInTheBush(getPlayer(), this.map));
			}
		}
	}

	private final GameMap map;
	private Set<Integer> unitsID;

	private HidingInTheBush(Player player, GameMap map) {
		super(player);
		this.map = map;

		LogicEventDispatcher.registerListener(PhaseChangedEvent.class, this);
	}

	@Override
	public void use() {
		if (this.unitsID == null) {
			return;
		}

		Set<Unit> units = new HashSet<>();

		for (Zone zone : this.map.getZones()) {
			zone.getUnits().stream().filter(u -> this.unitsID.contains(u.getID())).forEach(units::add);
		}

		int cost = 0;
		for (Unit unit : units) {
			if (unit.isLevel(UnitLevel.HERO)) {
				cost += 2;
			} else {
				++cost;
			}
		}

		if (!getPlayer().hasRequiredStaffPoints(cost)) {
			return;
		}

		getPlayer().decrementStaffPoints(cost);
		getPlayer().getBuffManager().addBuff(new HidingInTheBushBuff(units));
	}

	@Override
	public void handlePhaseChanged(PhaseChangedEvent event) {
		if (event.getNewPhase() == PhaseChangedEvent.Phase.ACTION) {
			LogicEventDispatcher.registerListener(HidingInTheBushUnitsChoiceEvent.class, this);
		} else {
			LogicEventDispatcher.unregisterListener(HidingInTheBushUnitsChoiceEvent.class, this);
		}
	}

	@Override
	public void handleUnitsChoiceEvent(HidingInTheBushUnitsChoiceEvent event) {
		this.unitsID = event.getUnits();
	}

	@Override
	public CapacityName getName() {
		return CapacityName.HIDING_BUSH;
	}
}
