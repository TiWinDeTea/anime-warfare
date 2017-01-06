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
	public static class CostMask {
		final int value;
		final boolean nulled;

		public CostMask(int value, boolean nulled) {
			this.value = value;
			this.nulled = nulled;
		}

		public CostMask(int value) {
			this(value, false);
		}
	}

	private final Map<UnitType, List<CostMask>> unitCost = new HashMap<>();
	private final List<CostMask> battleCost = new ArrayList<>();
	private final List<CostMask> uniqueActionCost = new ArrayList<>();

	int getUnitCostModifier(UnitType unitType) {
		if (!this.unitCost.containsKey(unitType)) {
			throw new IllegalArgumentException();
		}

		return getCost(this.unitCost.get(unitType));
	}

	public void addUnitCost(UnitType unitType, CostMask costMask) {
		this.unitCost.getOrDefault(unitType, new ArrayList<>()).add(costMask);

		LogicEventDispatcher.send(new CostModifiedEvent(unitType, getUnitCostModifier(unitType)));
	}

	public void removeUnitCost(UnitType unitType, CostMask costMask) {
		List<CostMask> costMaskList = this.unitCost.get(unitType);

		if (costMaskList != null) {
			if (costMaskList.remove(costMask)) {
				LogicEventDispatcher.send(new CostModifiedEvent(CostModifiedEvent.Type.UNIT,
						getUnitCostModifier(unitType)));
			}
		}
	}

	int getBattleCostModifier() {
		return getCost(this.battleCost);
	}

	public void addBattleCost(CostMask costMask) {
		if (this.battleCost.add(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(CostModifiedEvent.Type.BATTLE, getBattleCostModifier()));
		}
	}

	public void removeBattleCost(CostMask costMask) {
		if (this.battleCost.remove(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(CostModifiedEvent.Type.BATTLE, getBattleCostModifier()));
		}
	}

	public int getUniqueActionModifier() {
		return getCost(this.uniqueActionCost);
	}

	public void addUniqueActionCostMask(CostMask costMask) {
		if (this.uniqueActionCost.add(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(CostModifiedEvent.Type.UNIQUE_ACTION,
					getUniqueActionModifier()));
		}
	}

	public void removeUniqueActionCostMask(CostMask costMask) {
		if (this.uniqueActionCost.remove(costMask)) {
			LogicEventDispatcher.send(new CostModifiedEvent(CostModifiedEvent.Type.UNIQUE_ACTION,
					getUniqueActionModifier()));
		}
	}

	private static int getCost(List<CostMask> costMasks) {
		int cost = 0;

		for (CostMask costMask : costMasks) {
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
