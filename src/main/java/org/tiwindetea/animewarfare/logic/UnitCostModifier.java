package org.tiwindetea.animewarfare.logic;

import org.tiwindetea.animewarfare.logic.units.UnitType;

import java.util.Map;

public class UnitCostModifier {
	private Map<UnitType, Integer> cost;

	public int getCost(UnitType unitType) {
		assert (this.cost.containsKey(unitType));

		return this.cost.get(unitType).intValue() + unitType.getDefaultCost();
	}

	public void modifyCost(UnitType unitType, int relativeInt) {
		this.cost.put(unitType,
				new Integer(this.cost.getOrDefault(unitType,
						new Integer(0)).intValue() + relativeInt));
	}
}
