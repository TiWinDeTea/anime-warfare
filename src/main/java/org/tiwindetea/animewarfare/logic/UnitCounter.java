package org.tiwindetea.animewarfare.logic;

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

	public void addUnit(UnitType type) {
		++this.numberOfUnitsByType[type.ordinal()];
		++this.numberOfUnitsByLevel[type.getUnitLevel().ordinal()];
	}

	public void removeUnit(UnitType type) {
		--this.numberOfUnitsByType[type.ordinal()];
		--this.numberOfUnitsByLevel[type.getUnitLevel().ordinal()];
	}
}
