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

import org.tiwindetea.animewarfare.logic.events.CostModifiedEvent;
import org.tiwindetea.animewarfare.logic.units.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostModifier {
	private final int playerID;
	private final Map<UnitType, List<Mask>> unitCost = new HashMap<>();
	private final List<Mask> battleCost = new ArrayList<>();
	private final List<Mask> uniqueActionCost = new ArrayList<>();

	public CostModifier(int playerID) {
		this.playerID = playerID;
	}

	int getUnitCostModifier(UnitType unitType) {
		if (!this.unitCost.containsKey(unitType)) {
			return 0;
		}

		return getCost(this.unitCost.get(unitType));
	}

	public void addUnitCost(UnitType unitType, Mask costMask) {
		this.unitCost.getOrDefault(unitType, new ArrayList<>()).add(costMask);

		LogicEventDispatcher.send(new CostModifiedEvent(this.playerID, unitType, getUnitCostModifier(unitType)));
	}

	public void removeUnitCost(UnitType unitType, Mask costMask) {
		List<Mask> costMaskList = this.unitCost.get(unitType);

		if (costMaskList != null) {
			if (costMaskList.remove(costMask)) {
				LogicEventDispatcher.send(new CostModifiedEvent(this.playerID, CostModifiedEvent.Type.UNIT,
						getUnitCostModifier(unitType)));
			}
		}
	}

	int getBattleCostModifier() {
		return getCost(this.battleCost);
	}

	public void addBattleCost(Mask costMask) {
		if (this.battleCost.add(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(this.playerID,
					CostModifiedEvent.Type.BATTLE,
					getBattleCostModifier()));
		}
	}

	public void removeBattleCost(Mask costMask) {
		if (this.battleCost.remove(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(this.playerID,
					CostModifiedEvent.Type.BATTLE,
					getBattleCostModifier()));
		}
	}

	public int getUniqueActionModifier() {
		return getCost(this.uniqueActionCost);
	}

	public void addUniqueActionCostMask(Mask costMask) {
		if (this.uniqueActionCost.add(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(this.playerID, CostModifiedEvent.Type.UNIQUE_ACTION,
					getUniqueActionModifier()));
		}
	}

	public void removeUniqueActionCostMask(Mask costMask) {
		if (this.uniqueActionCost.remove(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(this.playerID, CostModifiedEvent.Type.UNIQUE_ACTION,
					getUniqueActionModifier()));
		}
	}

	private static int getCost(List<Mask> costMasks) {
		int cost = 0;

		for (Mask costMask : costMasks) {
			if (costMask.nulled) {
				cost = 0;
				break;
			} else {
				cost += costMask.value;
			}
		}
		return cost;
	}
}
