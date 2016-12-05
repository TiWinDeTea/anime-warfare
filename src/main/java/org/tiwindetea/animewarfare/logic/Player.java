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

import org.tiwindetea.animewarfare.logic.buffs.BuffManager;
import org.tiwindetea.animewarfare.logic.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private final int ID;

	private int fanNumber;
	private int staffAvailable;

	private final FactionType faction;
	private final BuffManager buffManager = new BuffManager();
	private final UnitCounter unitCounter = new UnitCounter();
	private final UnitCostModifier unitCostModifier = new UnitCostModifier();
	private final List<Unit> unitCaptured = new ArrayList<>();

	public Player(int id, FactionType faction) {
		this.ID = id;
		this.faction = faction;
	}

	public int getID() {
		return this.ID;
	}

	public void setStaffAvailable(int staffAvailable) {
		// TODO: Send Event
		this.staffAvailable = staffAvailable;
	}

	public boolean hasRequiredStaffPoints(int actionCost, int numberOfActions) {
		return actionCost * numberOfActions >= this.staffAvailable;
	}

	public boolean hasRequiredStaffPoints(int actionCost) {
		return hasRequiredStaffPoints(actionCost, 1);
	}

	public void decrementStaffPoints(int actionCost, int numberOfActions) {
		assert (hasRequiredStaffPoints(actionCost, numberOfActions));

		this.staffAvailable -= actionCost * numberOfActions;

		// TODO: Fire event
	}

	public void decrementStaffPoints(int actionCost) {
		decrementStaffPoints(actionCost, 1);
	}

	public int getStaffAvailable() {
		return this.staffAvailable;
	}

	public int getFanNumber() {
		return this.fanNumber;
	}

	public FactionType getFaction() {
		return this.faction;
	}

	public UnitCounter getUnitCounter() {
		return this.unitCounter;
	}

	public UnitCostModifier getUnitCostModifier() {
		return this.unitCostModifier;
	}

	public List<Unit> getUnitCaptured() {
		return this.unitCaptured;
	}
}
