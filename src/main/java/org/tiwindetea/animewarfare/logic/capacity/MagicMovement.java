package org.tiwindetea.animewarfare.logic.capacity;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.event.UnitEvent;
import org.tiwindetea.animewarfare.logic.event.UnitEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class MagicMovement extends PlayerCapacity {
	public static class MagicMovementActivable extends PlayerActivable implements UnitEventListener {
		public MagicMovementActivable(Player player) {
			super(player);

			EventDispatcher.registerListener(UnitEvent.class, this);
		}

		@Override
		public void destroy() {
			EventDispatcher.unregisterListener(UnitEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitEvent event) {
			if (event.getType() == UnitEvent.Type.REMOVED && this.player.hasFaction(event.getFaction()) && event.getUnitType() == UnitType.NYARUKO) {
				this.player.activateCapacity(new MagicMovement(this.player));
				// TODO: Teleport Nyaruko.

				destroy();
			}
		}
	}

	private MagicMovement(Player player) {
		super(player);
	}

	@Override
	public void use() {

	}
}
