package org.tiwindetea.animewarfare.logic.capacity;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.event.UnitEvent;
import org.tiwindetea.animewarfare.logic.event.UnitEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class Clemency extends PlayerCapacity {
	public static class ClemencyActivable extends PlayerActivable implements UnitEventListener {
		public ClemencyActivable(Player player) {
			super(player);

			EventDispatcher.registerListener(UnitEvent.class, this);
		}

		@Override
		public void destroy() {
			EventDispatcher.unregisterListener(UnitEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitEvent event) {
			if (event.getType() == UnitEvent.Type.ADDED && event.getUnitType() == UnitType.SAKAMAKI_IZAYOI) {
				this.player.activateCapacity(new Clemency(this.player));
				destroy();
			}
		}
	}

	private Clemency(Player player) {
		super(player);
	}

	@Override
	public void use() {
		// TODO
	}
}
