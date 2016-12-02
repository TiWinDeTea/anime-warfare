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

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.states.events.UnitMovedEvent;

public class Unit {
	private final UnitType type;
	private Zone zone;
	private final FactionType faction;
	private final UnitBuffedCharacteristics unitBuffedCharacteristics;

	public Unit(UnitType type) {
		this.faction = type.getDefaultFaction();
		this.type = type;
		this.unitBuffedCharacteristics = new UnitBuffedCharacteristics(type);
	}

	/**
	 * Returns the previous zone.
	 */
	public Zone move(Zone zone) {
		Zone previousZone = this.zone;
		previousZone.removeUnit(this);

		this.zone = zone;
		this.zone.addUnit(this);

		EventDispatcher.getInstance().fire(new UnitMovedEvent(getType(), previousZone.getID(), this.zone.getID()));

		return previousZone;
	}

	/**
	 * Returns the number of attacks points for this unit.
	 *
	 * @return the number of attacks points
	 */
	public int getAttackPoints() {
		return this.unitBuffedCharacteristics.getAttackPoints()
				+ this.type.getUnitBasicCharacteristics()
				           .getBaseAttackPoints();
	}

	public FactionType getFaction() {
		return this.faction;
	}

	public UnitType getType() {
		return this.type;
	}

	public boolean isLevel(UnitLevel level) {
		return this.type.getUnitLevel() == level;
	}

	// TODO: getters

}
