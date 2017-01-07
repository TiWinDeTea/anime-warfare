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

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.net.logicevent.UseCapacityEvent;

import java.util.List;
import java.util.Map;

/**
 * Abstract battle state.
 *
 * @author Benoît CORTIER
 */
public abstract class BattleState extends State {
	protected BattleContext battleContext;

	protected State nextState = this;

	public BattleState(BattleContext battleContext) {
		this.battleContext = battleContext;
	}

	@Override
	public void update() {
	}

	@Override
	public State next() {
		return this.nextState;
	}

	// helper
	protected void takeCapacityIntoConsideration(UseCapacityEvent event,
												 List<CapacityName> attackerCapacities,
												 List<CapacityName> defenderCapacities,
												 Map<Player, CapacityName> thirdPartiesCapacities) {
		if (event.getPlayerID() == this.battleContext.getAttacker().getPlayer().getID()) {
			if (this.battleContext.getAttacker().getPlayer().hasCapacity(event.getName())) {
				attackerCapacities.add(event.getName());
			}
		} else if (event.getPlayerID() == this.battleContext.getDefender().getPlayer().getID()) {
			if (this.battleContext.getDefender().getPlayer().hasCapacity(event.getName())) {
				defenderCapacities.add(event.getName());
			}
		} else {
			this.battleContext.getThirdPartPlayers().stream()
					.filter(player -> player.getID() == event.getPlayerID())
					.forEach(player -> thirdPartiesCapacities.put(player, event.getName()));
		}
	}
}
