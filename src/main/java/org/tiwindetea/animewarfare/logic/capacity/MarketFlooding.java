package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;

import java.util.HashSet;
import java.util.Set;

public class MarketFlooding implements Capacity {
	public static class MarketFloodingActivable extends PlayerActivable implements UnitMovedEventListener {
		private Set<Integer> zoneWherePlayerDrewUnits = new HashSet<>();

		public MarketFloodingActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(UnitMovedEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitMovedEvent.class, this);
		}

		@Override
		public void handleUnitMoved(UnitMovedEvent event) {
			if (event.getSource() == null) {
				boolean result = this.zoneWherePlayerDrewUnits.add(new Integer(event.getDestination().getID()));
				if (result && this.zoneWherePlayerDrewUnits.size() == 4) {
					activateAndDestroy(new MarketFlooding());
				}
			}
		}
	}

	MarketFlooding() {
	}

	@Override
	public void use() {
		// Nothing to do
	}

	@Override
	public CapacityName getName() {
		return CapacityName.MARKET_FLOODING;
	}
}
