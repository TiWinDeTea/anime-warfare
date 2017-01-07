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

package org.tiwindetea.animewarfare.logic.battle;

import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.units.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class holding battle side informations.
 *
 * @author Benoît CORTIER
 */
public class BattleSide {
	private final Player player;

	private final List<Unit> units = new ArrayList<>();
	private final List<Unit> deadUnits = new ArrayList<>();
	private final List<Unit> woundedUnits = new ArrayList<>();

	private int attack = 0;
	private int woundeds = 0;
	private int deads = 0;

	public BattleSide(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

	public List<Unit> getUnits() {
		return Collections.unmodifiableList(this.units);
	}

	public void addUnit(Unit unit) {
		this.units.add(unit);
	}

	public void removeUnit(Unit unit) {
		this.units.remove(unit);
	}

	public List<Unit> getDeads() {
		return Collections.unmodifiableList(this.deadUnits);
	}

	public void addDead(Unit unit) {
		this.deadUnits.add(unit);
	}

	public void removeDead(Unit unit) {
		this.deadUnits.remove(unit);
	}

	public List<Unit> getWoundeds() {
		return Collections.unmodifiableList(this.woundedUnits);
	}

	public void addWounded(Unit unit) {
		this.woundedUnits.add(unit);
	}

	public void removeWounded(Unit unit) {
		this.woundedUnits.remove(unit);
	}

	public int getAttack() {
		return this.attack;
	}

	public int getNumberOfWoundeds() {
		return this.woundeds;
	}

	public int getNumberOfDeads() {
		return this.deads;
	}

	public void incrementAttack(int value) {
		this.attack += value;
	}

	public void decrementAttack(int value) {
		this.attack -= value;
	}

	public void incrementWoundeds(int value) {
		this.woundeds += value;
	}

	public void decrementWoundeds(int value) {
		this.woundeds -= value;
	}

	public void incrementDeads(int value) {
		this.deads += value;
	}

	public void decrementDeads(int value) {
		this.deads -= value;
	}
}
