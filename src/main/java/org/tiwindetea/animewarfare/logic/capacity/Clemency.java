package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.event.UnitEvent;
import org.tiwindetea.animewarfare.logic.event.UnitEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class Clemency extends PlayerCapacity {
	public static class ClemencyActivable extends PlayerActivable implements UnitEventListener {
		public ClemencyActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(UnitEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitEvent event) {
			if (event.getType() == UnitEvent.Type.ADDED && event.getUnitType() == UnitType.SAKAMAKI_IZAYOI) {
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
	public CapacityType getType() {
		return CapacityType.CLEMENCY;
	}
}
