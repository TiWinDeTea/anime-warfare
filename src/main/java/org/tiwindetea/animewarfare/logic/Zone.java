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

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.event.StudioAddedEvent;
import org.tiwindetea.animewarfare.logic.event.UnitEvent;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Zone {
	private final int ID;

	private final boolean isCountrySide;
	private final List<Unit> units = new ArrayList<>();

	private Studio studio;

	public Zone(int id, boolean isCountrySide) {
		this.ID = id;
		this.isCountrySide = isCountrySide;
	}

	public int getID() {
		return this.ID;
	}

	public List<Unit> getUnits() {
		return Collections.unmodifiableList(this.units);
	}

	public Unit getUnit(int unitID) {
		return this.units.stream()
		                 .filter(unit -> unit.getID() == unitID)
		                 .findFirst()
		                 .orElse(null);
	}

	public Unit getUnit(UnitLevel level, FactionType faction) {
		return this.units.stream()
		                 .filter(unit -> unit.isLevel(level) && unit.getFaction() == faction)
		                 .findFirst().orElse(null);
	}

	public void addUnit(Unit unit) {
		this.units.add(unit);
		EventDispatcher.getInstance().fire(new UnitEvent(UnitEvent.Type.ADDED, unit.getID(), this.ID));
	}

	public boolean removeUnit(Unit unit) {
		if (this.units.remove(unit)) {
			EventDispatcher.getInstance().fire(new UnitEvent(UnitEvent.Type.REMOVED, unit.getID(), this.ID));
			return true;
		}

		return false;
	}

	public Studio getStudio() {
		return this.studio;
	}

	public void setStudio(Studio studio) {
		this.studio = studio;
		EventDispatcher.getInstance().fire(new StudioAddedEvent(this.ID));
	}

	public boolean isCountrySide() {
		return this.isCountrySide;
	}

	public boolean hasUnitOfFaction(FactionType faction) {
		return this.units.stream()
		                 .anyMatch(unit -> unit.getFaction() == faction);
	}
}
