package org.tiwindetea.animewarfare.logic.units;

import org.tiwindetea.animewarfare.logic.BuffManager;
import org.tiwindetea.animewarfare.logic.Zone;

public class Unit extends Entity {
	private Zone zone;
	private final UnitType type;

	public Unit(UnitType type) {
		this.type = type;
	}

	/**
	 * Returns the previous zone.
	 */
	public Zone move(Zone zone) {
		return null;
	}

	/**
	 * Returns the number of attacks points for this unit.
	 *
	 * @param bManager
	 * @return the number of attacks points
	 */
	public int getAttackPoints(BuffManager bManager) {
		// TODO
		return 0;
	}

	// TODO: getters

}
