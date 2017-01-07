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

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.capacity.CapacityType;
import org.tiwindetea.animewarfare.net.logicevent.UseCapacityEvent;
import org.tiwindetea.animewarfare.net.logicevent.UseCapacityEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Benoît CORTIER
 */
public class PostBattleState extends BattleState implements UseCapacityEventListener {
	private final List<CapacityName> attackerCapacities = new ArrayList<>();
	private final List<CapacityName> defenderCapacities = new ArrayList<>();
	private final Map<Player, CapacityName> thirdPartiesCapacities = new HashMap<>();

	private boolean woundedsSelected = false;

	public PostBattleState(BattleContext battleContext) {
		super(battleContext);
	}

	@Override
	protected void onEnter() {
		LogicEventDispatcher.getInstance().fire(new BattleEvent(BattleEvent.Type.POST_BATTLE, this.battleContext));

		// register events.
		LogicEventDispatcher.registerListener(UseCapacityEvent.class, this);
	}

	@Override
	protected void onExit() {
		this.attackerCapacities.stream().forEach(c -> this.battleContext.getAttacker().getPlayer().useCapacity(c));
		this.defenderCapacities.stream().forEach(c -> this.battleContext.getDefender().getPlayer().useCapacity(c));
		for (Map.Entry<Player, CapacityName> playerCapacityNameEntry : this.thirdPartiesCapacities.entrySet()) {
			playerCapacityNameEntry.getKey().useCapacity(playerCapacityNameEntry.getValue());
		}

		LogicEventDispatcher.getInstance().fire(new BattleEvent(BattleEvent.Type.BATTLE_FINISHED, this.battleContext));

		// unregister events.
		LogicEventDispatcher.unregisterListener(UseCapacityEvent.class, this);
	}

	// TODO: listen for woundeds selection and move.
	// attacker first.
	// defender second.
	// kill units that cannot escape (except if invincible).

	@Override
	public void handlePlayerUseCapacity(UseCapacityEvent event) {
		if (event.getName().getType().equals(CapacityType.POST_BATTLE) && this.woundedsSelected) {
			takeCapacityIntoConsideration(event,
					this.attackerCapacities,
					this.defenderCapacities,
					this.thirdPartiesCapacities);
		}
	}

	// TODO: update when receiving battlePhaseReady event.
	/*
		this.nextState = new BattleEndedState()
		update();
	 */
}
