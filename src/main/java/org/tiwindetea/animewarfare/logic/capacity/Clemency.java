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
import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRightsPool;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.battle.BattleSide;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.logicevent.NumberOfUnitiesToReconfortEventListener;
import org.tiwindetea.animewarfare.net.logicevent.NumberOfUnitsToReconfortEvent;
import org.tiwindetea.animewarfare.net.logicevent.UseCapacityEvent;
import org.tiwindetea.animewarfare.net.logicevent.UseCapacityEventListener;

/*
 * @author Benoît CORTIER
 */
public class Clemency extends PlayerCapacity implements BattleEventListener,
		PhaseChangedEventListener, NumberOfUnitiesToReconfortEventListener,
		UseCapacityEventListener {

	public static class ClemencyActivable extends PlayerActivable implements UnitCounterEventListener {
		private final AdvertisingCampaignRightsPool pool;

		public ClemencyActivable(Player player, AdvertisingCampaignRightsPool pool) {
			super(player);
			this.pool = pool;

			LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitCounterEvent event) {
			if (event.getType() == UnitCounterEvent.Type.ADDED && event.getUnitType() == UnitType.SAKAMAKI_IZAYOI) {
				activateAndDestroy(new Clemency(getPlayer(), this.pool));
			}
		}
	}

	private final AdvertisingCampaignRightsPool pool;

	private BattleSide opponentSide;
	private boolean usedThisTurn;

	private int numberOfUnitsToReconfort;

	Clemency(Player player, AdvertisingCampaignRightsPool pool) {
		super(player);
		this.pool = pool;

		LogicEventDispatcher.registerListener(BattleEvent.class, this);
		LogicEventDispatcher.registerListener(PhaseChangedEvent.class, this);
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
		if (this.usedThisTurn) {
			Log.debug(getClass().getName(), "Clemency has already been used.");
			return;
		}

		if (event.getBattleContext().getAttacker().getPlayer().equals(getPlayer())) {
			this.opponentSide = event.getBattleContext().getDefender();
		} else if (event.getBattleContext().getDefender().getPlayer().equals(getPlayer())) {
			this.opponentSide = event.getBattleContext().getAttacker();
		}

		LogicEventDispatcher.registerListener(UseCapacityEvent.class, this);
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {
		// Nothing to do
	}

	@Override
	public void handlePlayerUseCapacity(UseCapacityEvent event) {
		if (event.getName() == getName()) {
			LogicEventDispatcher.unregisterListener(UseCapacityEvent.class, this);
			LogicEventDispatcher.registerListener(NumberOfUnitsToReconfortEvent.class, this);
			this.numberOfUnitsToReconfort = 0;
		}
	}

	@Override
	public void handleNumberOfUnitiesToReconfortEvent(NumberOfUnitsToReconfortEvent numberOfUnitsToReconfortEvent) {
		int numberOfUnitsToReconfort;
		if (numberOfUnitsToReconfortEvent.getNumber() > this.opponentSide.getWoundeds().size()) {
			Log.debug(getClass().getName(), "Number of unities to reconfort > number of unities woundeds");
			numberOfUnitsToReconfort = this.opponentSide.getWoundeds().size();
		} else {
			numberOfUnitsToReconfort = numberOfUnitsToReconfortEvent.getNumber();
		}

		if (!getPlayer().hasRequiredStaffPoints(numberOfUnitsToReconfort)) {
			Log.debug(getClass().getName(), "Not enough staff points.");
			return;
		}
		getPlayer().decrementStaffPoints(numberOfUnitsToReconfort);

		this.numberOfUnitsToReconfort = numberOfUnitsToReconfort;

		LogicEventDispatcher.unregisterListener(NumberOfUnitsToReconfortEvent.class, this);
	}

	@Override
	public void use() {
		for (int i = 0; i < this.numberOfUnitsToReconfort && this.opponentSide.getWoundeds().size() != 0; i++) {
			this.opponentSide.removeWounded(this.opponentSide.getWoundeds().get(0));
			this.pool.addAdvertisingCampaignRightToPlayer(getPlayer());
		}

		this.numberOfUnitsToReconfort = 0;
		this.opponentSide = null;

		this.usedThisTurn = true;
	}

	@Override
	public void destroy() {
		LogicEventDispatcher.unregisterListener(NumberOfUnitsToReconfortEvent.class, this);
		LogicEventDispatcher.registerListener(BattleEvent.class, this);
		LogicEventDispatcher.registerListener(PhaseChangedEvent.class, this);
	}

	@Override
	public void handlePhaseChanged(PhaseChangedEvent event) {
		if (event.getNewPhase() == PhaseChangedEvent.Phase.ACTION) {
			this.usedThisTurn = false;
		}
	}

	@Override
	public CapacityName getName() {
		return CapacityName.CLEMENCY;
	}
}
