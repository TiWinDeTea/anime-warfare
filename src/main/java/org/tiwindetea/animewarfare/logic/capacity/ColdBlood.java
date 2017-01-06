package org.tiwindetea.animewarfare.logic.capacity;

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
				if (event.getBattleContext().getDefender().getNumberOfDices() > 7) {
					this.me = event.getBattleContext().getAttacker();
				}
			} else if (event.getBattleContext().getDefender().equals(getPlayer())) {
				if (event.getBattleContext().getDefender().getNumberOfDices() > 7) {
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
				if (this.me.getDeads() == 0) {
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
	}

	@Override
	public void use() {
		if (this.usedThisTurn || !getPlayer().hasRequiredStaffPoints(COST)) {
			return;
		}

		LogicEventDispatcher.registerListener(ColdBloodUnitTypeChoiceEvent.class, this);
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

		// TODO: Need dead unit type in BattleSide class.
	}

	@Override
	public void handlePhaseChanged(PhaseChangedEvent event) {
		this.usedThisTurn = false;
	}

	@Override
	public void handleUnitTypeChoice(ColdBloodUnitTypeChoiceEvent event) {
		LogicEventDispatcher.unregisterListener(ColdBloodUnitTypeChoiceEvent.class, this);
		LogicEventDispatcher.registerListener(BattleEvent.class, this);

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
