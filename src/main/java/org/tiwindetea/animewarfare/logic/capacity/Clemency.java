package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRight;
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
import org.tiwindetea.animewarfare.net.logicevent.NumberOfUnitiesToReconfortEvent;
import org.tiwindetea.animewarfare.net.logicevent.NumberOfUnitiesToReconfortEventListener;

public class Clemency extends PlayerCapacity implements BattleEventListener, PhaseChangedEventListener, NumberOfUnitiesToReconfortEventListener {
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

	private static final int COST = -1; // FIXME

	private final AdvertisingCampaignRightsPool pool;

	private BattleSide battleSide;
	private boolean usedThisTurn;

	Clemency(Player player, AdvertisingCampaignRightsPool pool) {
		super(player);
		this.pool = pool;

		LogicEventDispatcher.registerListener(BattleEvent.class, this);
		LogicEventDispatcher.registerListener(PhaseChangedEvent.class, this);
	}

	@Override
	public void use() {
		if (this.battleSide == null || this.usedThisTurn || getPlayer().hasRequiredStaffPoints(COST)) {
			return;
		}

		LogicEventDispatcher.registerListener(NumberOfUnitiesToReconfortEvent.class, this);
		this.usedThisTurn = true;
	}

	@Override
	public void handleNumberOfUnitiesToReconfortEvent(NumberOfUnitiesToReconfortEvent numberOfUnitiesToReconfortEvent) {
		if (numberOfUnitiesToReconfortEvent.getNumber() > getPlayer().getAdvertisingCampaignRights().size()) {
			int weight = 1;
			int counter = 0;
			do {
				AdvertisingCampaignRight right = getPlayer().removeAdvertisingCampainRights(weight);
				if (right == null) {
					++weight;
				} else {
					this.pool.returnAdvertisingCampaignRight(right);
					++counter;
				}

			} while (counter != numberOfUnitiesToReconfortEvent.getNumber());

			this.battleSide.decrementDeads(counter);
		}

		LogicEventDispatcher.unregisterListener(NumberOfUnitiesToReconfortEvent.class, this);
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
		if (event.getBattleContext().getAttacker().getPlayer().equals(getPlayer())) {
			this.battleSide = event.getBattleContext().getAttacker();
		} else if (event.getBattleContext().getDefender().getPlayer().equals(getPlayer())) {
			this.battleSide = event.getBattleContext().getDefender();
		}
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {
		// Nothing to do
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
