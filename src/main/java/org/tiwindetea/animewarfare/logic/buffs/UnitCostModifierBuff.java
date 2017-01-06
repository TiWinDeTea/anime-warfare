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

package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.Mask;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.units.UnitType;

import java.util.List;

abstract class UnitCostModifierBuff extends Buff {
	private final List<Player> players;
	private final Mask costMask;

	protected UnitCostModifierBuff(List<Player> players, int modifier) {
		super(1);

		this.players = players;
		this.costMask = new Mask(modifier);

		for (Player player : this.players) {
			for (UnitType unitType : UnitType.values()) {
				player.getCostModifier().addUnitCost(unitType, this.costMask);
			}

		}
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		for (Player player : this.players) {
			for (UnitType unitType : UnitType.values()) {
				player.getCostModifier().removeUnitCost(unitType, this.costMask);
			}

		}
	}
}
