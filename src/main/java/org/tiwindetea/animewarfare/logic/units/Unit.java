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

package org.tiwindetea.animewarfare.logic.units;

import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.buffs.BuffManager;

public class Unit {
	private final UnitType type;
	private Zone zone;
	private FactionType faction;

	public Unit(UnitType type) {
		this.faction = type.getDefaultFaction();
		this.type = type;
	}

	/**
	 * Returns the previous zone.
	 */
	public Zone move(Zone zone) {
		// TODO: Fire event
		return this.zone = zone;
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

	public FactionType getFaction() {
		return this.faction;
	}

	// TODO: getters

}
