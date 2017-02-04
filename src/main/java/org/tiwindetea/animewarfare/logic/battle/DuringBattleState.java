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

import com.esotericsoftware.minlog.Log;
import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.battle.event.BattleDeadsSelectedEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.net.logicevent.SelectUnitsEvent;
import org.tiwindetea.animewarfare.net.logicevent.SelectUnitsEventListener;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Benoît CORTIER
 */
public class DuringBattleState extends BattleState implements SelectUnitsEventListener {
	private final Random random = new Random();

	private Set<Integer> playersReady = new LinkedHashSet<>();

	public DuringBattleState(BattleContext battleContext, GameMap map) {
		super(battleContext, map);
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
		LogicEventDispatcher.registerListener(SelectUnitsEvent.class, this);
	}

	@Override
	protected void onExit() {
		for (BattleSide battleSide : this.battleContext.getBattleSides()) {
			for (Unit unit : battleSide.getDeads()) {
				unit.removeFromMap();
				battleSide.getPlayer().getUnitCounter().removeUnit(unit.getType(), unit);
			}
		}

		// unregister events.
		LogicEventDispatcher.unregisterListener(SelectUnitsEvent.class, this);
	}

	@Override
	public void handleSelectUnits(SelectUnitsEvent event) {
		if (this.playersReady.contains(event.getPlayerID())) {
			Log.debug(getClass().getName(), event.getPlayerID() + " has already choosen his deads.");
			return;
		}

		BattleSide battleSide;
		if (event.getPlayerID() == this.battleContext.getAttacker().getPlayer().getID()) {
			battleSide = this.battleContext.getAttacker();
		} else if (event.getPlayerID() == this.battleContext.getDefender().getPlayer().getID()) {
			battleSide = this.battleContext.getDefender();
		} else {
			Log.debug(getClass().getName(), event.getPlayerID() + " doesn't belongs to a battle side.");
			return;
		}

		if (event.getUnits().size() != battleSide.getNumberOfDeads()
				&& event.getUnits().size() != battleSide.getUnits().size()) {
			Log.debug(getClass().getName(), "Number of deads doesn't match.");
			return;
		}

		if (event.getUnits().stream()
				.anyMatch(id -> battleSide.getUnits().stream()
						.noneMatch(u -> id == u.getID()))
				) {
			Log.debug(getClass().getName(), "Unit not concerned!");
			return; // ohw! This unit is not concerned!
		}

		for (Unit unit : battleSide.getUnits()) {
			if (event.getUnits().contains(unit.getID())) {
				if (unit.getUnitBuffedCharacteristics().isAttackable()) {
					battleSide.addDead(unit);
				} else {
					// if not attackable, the unit is not dead, just removed
					// from the battle (to avoid taking wounds as well later).
					battleSide.removeUnit(unit);
				}
			}
		}

		this.playersReady.add(event.getPlayerID());

		if (this.playersReady.size() >= 2) {
			LogicEventDispatcher.send(new BattleDeadsSelectedEvent(this.battleContext));

			this.nextState = new PostBattleState(this.battleContext, this.map);
			this.machine.get().update();
		}
	}

	// helper
	private void computeWoundedsAndDeads(BattleSide attacker, BattleSide target) {
		for (int i = 0; i < attacker.getAttack(); i++) {
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
