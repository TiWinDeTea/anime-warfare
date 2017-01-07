package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.events.ProductionEvent;
import org.tiwindetea.animewarfare.logic.events.ProductionEventListener;
import org.tiwindetea.animewarfare.logic.events.UnitCapturedEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCapturedEventListener;

import java.util.HashMap;
import java.util.Map;

public class MoreFans extends PlayerCapacity implements UnitCapturedEventListener {
	public static class MoreFansActivable extends PlayerActivable implements ProductionEventListener {
		private final Map<Integer, Integer> numberOfProductions = new HashMap<>();

		public MoreFansActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(ProductionEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(ProductionEvent.class, this);
			this.numberOfProductions.clear();
		}

		@Override
		public void onProductionActivated(ProductionEvent event) {
			if (event.getPlayerID() != getPlayer().getID()) {
				this.numberOfProductions.put(Integer.valueOf(event.getPlayerID()),
						Integer.valueOf(this.numberOfProductions.getOrDefault(Integer.valueOf(event.getPlayerID()),
								new Integer(0)).intValue() + 1));
			}

			if (this.numberOfProductions.get(Integer.valueOf(event.getPlayerID())).intValue() == 6) {
				activateAndDestroy(new MoreFans(getPlayer()));
			}
		}

		@Override
		public void onProductionDisabled(ProductionEvent event) {
			if (event.getPlayerID() != getPlayer().getID()) {
				this.numberOfProductions.put(Integer.valueOf(event.getPlayerID()),
						Integer.valueOf(this.numberOfProductions.getOrDefault(Integer.valueOf(event.getPlayerID()),
								new Integer(0)).intValue() - 1));
			}
		}
	}

	private static final int COST = 4 - 1; // COST - CAPTURE_MASCOT_COST

	MoreFans(Player player) {
		super(player);
	}

	@Override
	public void use() {
		if (getPlayer().hasRequiredStaffPoints(COST + 1))
			LogicEventDispatcher.registerListener(UnitCapturedEvent.class, this);
	}

	@Override
	public void onUnitCaptured(UnitCapturedEvent event) {
		if (event.getHunter().getID() == getPlayer().getID()) {
			LogicEventDispatcher.unregisterListener(UnitCapturedEvent.class, this);
			getPlayer().decrementStaffPoints(COST);
			event.getHuntedPlayer().decrementFans(1);
			getPlayer().incrementFans(1);
		}
	}

	@Override
	public CapacityName getName() {
		return CapacityName.MORE_FANS;
	}
}
