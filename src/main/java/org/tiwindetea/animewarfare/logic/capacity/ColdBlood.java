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

package org.tiwindetea.animewarfare.logic.capacity;

import com.esotericsoftware.minlog.Log;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.battle.BattleSide;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.logicevent.ColdBloodUnitTypeChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.ColdBloodUnitTypeChoiceEventListener;

public class ColdBlood extends PlayerCapacity implements BattleEventListener, PhaseChangedEventListener, ColdBloodUnitTypeChoiceEventListener {
	public static class ColdBloodActivable extends PlayerActivable implements BattleEventListener {
		BattleSide me;

		public ColdBloodActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(BattleEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(BattleEvent.class, this);
		}

		@Override
		public void handlePreBattle(BattleEvent event) {
			// Nothing to do
		}

		@Override
		public void handleDuringBattle(BattleEvent event) {
			if (event.getBattleContext().getAttacker().equals(getPlayer())) {
				if (event.getBattleContext().getDefender().getAttack() > 7) {
					this.me = event.getBattleContext().getAttacker();
				}
			} else if (event.getBattleContext().getDefender().equals(getPlayer())) {
				if (event.getBattleContext().getDefender().getAttack() > 7) {
					this.me = event.getBattleContext().getDefender();
				}
			}
		}

		@Override
		public void handlePostBattle(BattleEvent event) {

		}

		@Override
		public void handleBattleFinished(BattleEvent event) {
			if (this.me != null) {
				if (this.me.getNumberOfDeads() == 0) {
					activateAndDestroy(new ColdBlood(getPlayer()));
				}
			}
		}
	}

	private static final int COST = 4;
	private boolean usedThisTurn;
	private UnitType unitTypeToResurect;

	/*
	Vous choisissez une unitée (alliée ou ennemie).
	Pendant toute la phase, si cette unité est insultée à mort, la redessiner est gratuit.
	*/
	ColdBlood(Player player) {
		super(player);

		LogicEventDispatcher.registerListener(ColdBloodUnitTypeChoiceEvent.class, this);
	}

	@Override
	public void use() {
		LogicEventDispatcher.registerListener(BattleEvent.class, this);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void handlePreBattle(BattleEvent event) {
		// Nothing to do
	}

	@Override
	public void handleDuringBattle(BattleEvent event) {
		// Nothing to do
	}

	@Override
	public void handlePostBattle(BattleEvent event) {
		// Nothing to do
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {
		LogicEventDispatcher.unregisterListener(BattleEvent.class, this);

		if (getPlayer().equals(event.getBattleContext().getAttacker().getPlayer())) {
			int numberOfBuffToApply = (int) event.getBattleContext()
			                                     .getAttacker()
			                                     .getDeads()
			                                     .stream()
			                                     .filter(u -> u.getType() == this.unitTypeToResurect).count();
			if (numberOfBuffToApply > 0) {

			}
		}
	}

	@Override
	public void handlePhaseChanged(PhaseChangedEvent event) {
		this.usedThisTurn = false;
	}

	@Override
	public void handleUnitTypeChoice(ColdBloodUnitTypeChoiceEvent event) {
		if (this.usedThisTurn || !getPlayer().hasRequiredStaffPoints(COST)) {
			Log.debug(ColdBlood.class.getName(), "Already used this turn or hasRequiredStaffPoints is false.");
			return;
		}

		if (getPlayer().hasFaction(event.getUnitType().getDefaultFaction())) {
			getPlayer().decrementStaffPoints(COST);
			this.usedThisTurn = true;
			this.unitTypeToResurect = event.getUnitType();
		}
	}

	@Override
	public CapacityName getName() {
		return CapacityName.COLD_BLOOD;
	}
}
