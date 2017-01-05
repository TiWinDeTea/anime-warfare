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
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.units.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class holding battle context informations.
 *
 * @author Benoît CORTIER
 */
public class BattleContext {
	private final Player attacker;
	private final Player defenser;

	private final List<Unit> attackerUnits = new ArrayList<>();
	private final List<Unit> defenserUnits = new ArrayList<>();

	private final Zone zone;

	public BattleContext(Player attacker, Player defenser, Zone zone) {
		this.attacker = attacker;
		this.defenser = defenser;
		this.zone = zone;
	}

	public Player getAttacker() {
		return this.attacker;
	}

	public Player getDefender() {
		return this.defenser;
	}

	public List<Unit> getAttackerUnits() {
		return Collections.unmodifiableList(this.attackerUnits);
	}

	public List<Unit> getDefenserUnits() {
		return Collections.unmodifiableList(this.defenserUnits);
	}

	public Zone getZone() {
		return this.zone;
	}
}
