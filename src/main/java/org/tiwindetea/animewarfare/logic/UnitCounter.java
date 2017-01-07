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

package org.tiwindetea.animewarfare.logic;

import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class UnitCounter {
	private final int[] numberOfUnitsByType = new int[UnitType.values().length];
	private final int[] numberOfUnitsByLevel = new int[UnitLevel.values().length];

	public int getNumberOfUnits(UnitType type) {
		return this.numberOfUnitsByType[type.ordinal()];
	}

	public int getNumberOfUnits(UnitLevel level) {
		return this.numberOfUnitsByLevel[level.ordinal()];
	}

	public void addUnit(UnitType type, int unitID) {
		++this.numberOfUnitsByType[type.ordinal()];
		++this.numberOfUnitsByLevel[type.getUnitLevel().ordinal()];

		LogicEventDispatcher.send(new UnitCounterEvent(UnitCounterEvent.Type.ADDED, type.getDefaultFaction(), type, unitID));
	}

	public void removeUnit(UnitType type, int unitID) {
		--this.numberOfUnitsByType[type.ordinal()];
		--this.numberOfUnitsByLevel[type.getUnitLevel().ordinal()];

		LogicEventDispatcher.send(new UnitCounterEvent(UnitCounterEvent.Type.REMOVED, type.getDefaultFaction(), type, unitID));
	}

	public boolean hasUnits() {
		for (UnitLevel level : UnitLevel.values()) {
			if (this.numberOfUnitsByLevel[level.ordinal()] != 0) {
				return true;
			}
		}

		return false;
	}
}
