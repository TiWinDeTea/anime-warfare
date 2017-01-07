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
import org.tiwindetea.animewarfare.net.logicevent.BattlePhaseReadyEvent;
import org.tiwindetea.animewarfare.net.logicevent.BattlePhaseReadyEventListener;

import java.util.Random;

/**
 * @author Benoît CORTIER
 */
public class DuringBattleState extends BattleState implements BattlePhaseReadyEventListener {
	private final Random random = new Random();

	private boolean deadsSelected = false;

	private int numberOfReady = 0;

	public DuringBattleState(BattleContext battleContext) {
		super(battleContext);
	}

	@Override
	protected void onEnter() {
		for (BattleSide battleSide : this.battleContext.getBattleSides()) {
			battleSide.getUnits().stream()
					.filter(unit -> unit.getUnitBuffedCharacteristics().canAttack())
					.forEach(unit -> battleSide.incrementAttack(unit.getAttackPoints()));
		}

		computeWoundedsAndDeads(this.battleContext.getAttacker(), this.battleContext.getDefender());
		computeWoundedsAndDeads(this.battleContext.getDefender(), this.battleContext.getAttacker());

		LogicEventDispatcher.getInstance().fire(new BattleEvent(BattleEvent.Type.DURING_BATTLE, this.battleContext));

		// register events.
		LogicEventDispatcher.registerListener(BattlePhaseReadyEvent.class, this);
	}

	@Override
	protected void onExit() {
		// unregister events.
		LogicEventDispatcher.unregisterListener(BattlePhaseReadyEvent.class, this);
	}

	// TODO: listen for deads selection.
	// event should provides all units to be killed and ignore those that are invincibles.

	@Override
	public void handleBattlePhaseReadyCapacity(BattlePhaseReadyEvent event) {
		if (this.deadsSelected) {
			this.numberOfReady++;
			if (this.numberOfReady >= 2) {
				this.nextState = new PostBattleState(this.battleContext);
				update();
			}
		}
	}

	// helper
	private void computeWoundedsAndDeads(BattleSide attacker, BattleSide target) {
		for (int i = 0; i < this.battleContext.getAttacker().getAttack(); i++) {
			int roll = this.random.nextInt() % 6;
			if (roll >= 3) {
				if (roll == 5) { // dead
					target.incrementDeads(1);
				} else { // wounded
					target.incrementWoundeds(1);
				}
			}
		}
	}
}
