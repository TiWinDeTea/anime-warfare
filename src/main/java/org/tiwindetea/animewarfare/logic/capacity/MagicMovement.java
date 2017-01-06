package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.Player;

public class MagicMovement implements Capacity {
	public static class MagicMovementActivable extends PlayerActivable {
		private final GameMap map;

		public MagicMovementActivable(Player player, GameMap map) {
			super(player);
			this.map = map;
		}

		@Override
		public void destroy() {
		}

		// TODO
	}

	MagicMovement() {
	}

	@Override
	public void use() {

	}

	@Override
	public CapacityName getName() {
		return CapacityName.MAGIC_MOVEMENT;
	}
}
