package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.event.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.event.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class Clemency extends PlayerCapacity {
	public static class ClemencyActivable extends PlayerActivable implements UnitCounterEventListener {
		public ClemencyActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitCounterEvent event) {
			if (event.getType() == UnitCounterEvent.Type.ADDED && event.getUnitType() == UnitType.SAKAMAKI_IZAYOI) {
				activateAndDestroy(new Clemency(getPlayer()));
			}
		}
	}

	Clemency(Player player) {
		super(player);
	}

	@Override
	public void use() {
		// TODO
	}

	@Override
	public CapacityName getType() {
		return CapacityName.CLEMENCY;
	}
}
