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
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleWoundedsSelectedEvent;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.capacity.CapacityType;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.net.logicevent.BattlePhaseReadyEvent;
import org.tiwindetea.animewarfare.net.logicevent.BattlePhaseReadyEventListener;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEvent;
import org.tiwindetea.animewarfare.net.logicevent.SelectWoundedUnitsEventListener;
import org.tiwindetea.animewarfare.net.logicevent.SelectWoundedsUnitsEvent;
import org.tiwindetea.animewarfare.net.logicevent.UseCapacityEvent;
import org.tiwindetea.animewarfare.net.logicevent.UseCapacityEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Benoît CORTIER
 */
public class PostBattleState extends BattleState
		implements UseCapacityEventListener, BattlePhaseReadyEventListener, SelectWoundedUnitsEventListener {
	private final List<CapacityName> attackerCapacities = new ArrayList<>();
	private final List<CapacityName> defenderCapacities = new ArrayList<>();
	private final Map<Player, CapacityName> thirdPartiesCapacities = new HashMap<>();

	private Set<Integer> playersReady = new LinkedHashSet<>();

	public PostBattleState(BattleContext battleContext, GameMap map) {
		super(battleContext, map);
	}

	@Override
	protected void onEnter() {
		LogicEventDispatcher.getInstance().fire(new BattleEvent(BattleEvent.Type.POST_BATTLE, this.battleContext));

		LogicEventDispatcher.registerListener(SelectWoundedsUnitsEvent.class, this);
	}

	@Override
	protected void onExit() {
		this.attackerCapacities.stream().forEach(c -> this.battleContext.getAttacker().getPlayer().useCapacity(c));
		this.defenderCapacities.stream().forEach(c -> this.battleContext.getDefender().getPlayer().useCapacity(c));
		for (Map.Entry<Player, CapacityName> playerCapacityNameEntry : this.thirdPartiesCapacities.entrySet()) {
			playerCapacityNameEntry.getKey().useCapacity(playerCapacityNameEntry.getValue());
		}

		for (BattleSide battleSide : this.battleContext.getBattleSides()) {
			for (Unit unit : battleSide.getWoundeds()) {
				if (unit.getZone().equals(this.battleContext.getZone())) {
					unit.removeFromMap();
					battleSide.getPlayer().getUnitCounter().removeUnit(unit.getType(), unit.getID());
				}
			}
		}

		LogicEventDispatcher.getInstance().fire(new BattleEvent(BattleEvent.Type.BATTLE_FINISHED, this.battleContext));
	}

	@Override
	public void handleSelectWoundedUnits(SelectWoundedsUnitsEvent event) {
		if (this.playersReady.contains(event.getPlayerID())) {
			Log.debug(getClass().getName().toString(), event.getPlayerID() + " has already choosen his woundeds.");
			return; // no need to choose twice.
		}

		BattleSide currentSide, opponentSide;
		if (event.getPlayerID() == this.battleContext.getAttacker().getPlayer().getID()) {
			currentSide = this.battleContext.getAttacker();
			opponentSide = this.battleContext.getDefender();
		} else if (event.getPlayerID() == this.battleContext.getDefender().getPlayer().getID()) {
			if (!this.playersReady.contains(this.battleContext.getAttacker().getPlayer().getID())) {
				Log.debug(getClass().getName().toString(), "attacker should choose his woundeds first.");
				return; // attacker first!
			}
			currentSide = this.battleContext.getDefender();
			opponentSide = this.battleContext.getAttacker();
		} else {
			Log.debug(getClass().getName().toString(), event.getPlayerID() + " doesn't belongs to a battle side.");
			return;
		}

		if (event.getWoundedsToMove().size() != currentSide.getNumberOfDeads()
				&& event.getWoundedsToMove().size() != currentSide.getUnits().size()) {
			Log.debug(getClass().getName().toString(), "Number of woundeds doesn't match.");
			return;
		}

		if (event.getWoundedsToMove().stream()
				.anyMatch(m -> currentSide.getUnits().stream()
						.noneMatch(u -> m.getUnitID() == u.getID()))
				) {
			Log.debug(getClass().getName().toString(), "Unit not concerned!");
			return; // ohw! This unit is not concerned!
		}

		for (Unit unit : currentSide.getUnits()) {
			Optional<MoveUnitsEvent.Movement> movement =
					event.getWoundedsToMove().stream().filter(m -> m.getUnitID() == unit.getID()).findFirst();
			if (movement.isPresent()) {
				if (unit.getUnitBuffedCharacteristics().isAttackable()) {
					if (GameMap.getDistanceBetween(movement.get().getDestinationZone(),
							this.battleContext.getZone().getID()) == 1) {
						Zone destinationZone = this.map.getZone(movement.get().getDestinationZone());
						if (destinationZone.getUnits().stream()
								.noneMatch(u -> u.getFaction() == opponentSide.getPlayer().getFaction())) {
							unit.move(destinationZone);
							continue;
						}
					}
					currentSide.addWounded(unit);
				} else {
					// if not attackable, the unit is not wounded, just removed
					// from the battle.
					currentSide.removeUnit(unit);
				}
			}
		}

		if (this.playersReady.size() >= 2) {
			LogicEventDispatcher.send(new BattleWoundedsSelectedEvent(this.battleContext));

			LogicEventDispatcher.unregisterListener(SelectWoundedsUnitsEvent.class, this);

			LogicEventDispatcher.registerListener(UseCapacityEvent.class, this);
			LogicEventDispatcher.registerListener(BattlePhaseReadyEvent.class, this);
		}
	}

	@Override
	public void handlePlayerUseCapacity(UseCapacityEvent event) {
		if (event.getName().getType().equals(CapacityType.POST_BATTLE)) {
			takeCapacityIntoConsideration(event,
					this.attackerCapacities,
					this.defenderCapacities,
					this.thirdPartiesCapacities);
		} else {
			Log.debug(getClass().getName().toString(), event.getPlayerID() + ": " + event.getName() + " is not post battle.");
		}
	}

	@Override
	public void handleBattlePhaseReadyCapacity(BattlePhaseReadyEvent event) {
		this.playersReady.add(event.getPlayerID());
		if (this.playersReady.size() >= 2 + this.thirdPartiesCapacities.size()) {
			this.playersReady.clear();

			LogicEventDispatcher.unregisterListener(UseCapacityEvent.class, this);
			LogicEventDispatcher.unregisterListener(BattlePhaseReadyEvent.class, this);

			this.nextState = new BattleEndedState();
			update();
		}
	}
}
