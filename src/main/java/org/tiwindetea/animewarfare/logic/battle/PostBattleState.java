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
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.capacity.Capacity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Benoît CORTIER
 */
public class PostBattleState extends BattleState {
	private final List<Capacity> attackerCapacities = new ArrayList<>();
	private final List<Capacity> defenderCapacities = new ArrayList<>();
	private final List<Capacity> thirdPartiesCapacities = new ArrayList<>();

	public PostBattleState(BattleContext battleContext) {
		super(battleContext);
	}

	@Override
	protected void onEnter() {
		LogicEventDispatcher.getInstance().fire(new BattleEvent(BattleEvent.Type.POST_BATTLE, this.battleContext));

		// register events.
	}

	@Override
	protected void onExit() {
		this.attackerCapacities.forEach(Capacity::use);
		this.defenderCapacities.forEach(Capacity::use);
		this.thirdPartiesCapacities.forEach(Capacity::use);

		LogicEventDispatcher.getInstance().fire(new BattleEvent(BattleEvent.Type.BATTLE_FINISHED, this.battleContext));

		// unregister events.
	}

	// TODO: listen for woundeds selection and move.
	// attacker first.
	// defender second.
	// kill units that cannot escape (except if invincible).

	// then
	// TODO: listen for post battles uses from server.

	// TODO: update when receiving battlePhaseReady event.
	/*
		this.nextState = new BattleEndedState()
		update();
	 */
}
