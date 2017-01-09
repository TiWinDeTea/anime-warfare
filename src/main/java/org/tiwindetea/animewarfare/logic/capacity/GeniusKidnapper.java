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

import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.events.UnitCapturedEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCapturedEventListener;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.net.logicevent.GeniusKidnapperMonsterChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.GeniusKidnapperMonsterChoiceEventListener;

import java.util.Optional;

public class GeniusKidnapper extends PlayerCapacity implements GeniusKidnapperMonsterChoiceEventListener {
	public static class GeniusKidnapperActivable extends PlayerActivable implements UnitCapturedEventListener {
		private final GameBoard gameBoard;
		private int counter;

		public GeniusKidnapperActivable(Player player, GameBoard gameBoard) {
			super(player);
			this.gameBoard = gameBoard;

			LogicEventDispatcher.registerListener(UnitCapturedEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitCapturedEvent.class, this);
		}

		@Override
		public void onUnitCaptured(UnitCapturedEvent event) {
			if (getPlayer().equals(event.getHunter()) && event.getCapturedUnitType().isLevel(UnitLevel.MASCOT)) {
				++this.counter;

				if (this.counter == 2) {
					activateAndDestroy(new GeniusKidnapper(getPlayer(), this.gameBoard));
				}
			}
		}
	}

	private static final int COST = 1;
	private final GameBoard gameBoard;

	private Unit victim;

	GeniusKidnapper(Player player, GameBoard gameBoard) {
		super(player);
		this.gameBoard = gameBoard;

		LogicEventDispatcher.registerListener(GeniusKidnapperMonsterChoiceEvent.class, this);
	}

	@Override
	public void use() {
		if (this.victim != null) {
			this.victim.removeFromMap();
			getPlayer().addUnitCaptured(this.victim, this.gameBoard.getPlayer(this.victim.getFaction()));

			this.victim = null;
		}
	}

	@Override
	public void handleGeniusKidnapperMonsterChoiceEvent(GeniusKidnapperMonsterChoiceEvent event) {
		if (!getPlayer().hasRequiredStaffPoints(COST)) {
			return;
		}

		if (!this.gameBoard.getMap().isValid(event.getZoneID())) {
			return;
		}

		Zone actionZone = this.gameBoard.getMap().getZone(event.getZoneID());
		Unit victim = actionZone.getUnit(event.getUnitID());

		Optional<Unit> hunter = actionZone.getUnits()
		                                  .stream()
		                                  .filter(u -> u.hasFaction(getPlayer().getFaction()))
		                                  .max(Unit::bestUnitComparator);

		if (!hunter.isPresent()) {
			return;
		}

		Optional<Unit> victimProtect = actionZone.getUnits()
		                                         .stream()
		                                         .filter(u -> u.hasFaction(victim.getFaction())
				                                         && Unit.bestUnitComparator(hunter.get(), u) <= 0)
		                                         .findFirst();

		if (victimProtect.isPresent()) {
			return;
		}

		this.victim = victim;
	}

	@Override
	public void destroy() {
		LogicEventDispatcher.unregisterListener(GeniusKidnapperMonsterChoiceEvent.class, this);
	}

	@Override
	public CapacityName getName() {
		return CapacityName.GENIUS_KIDNAPPER;
	}
}
