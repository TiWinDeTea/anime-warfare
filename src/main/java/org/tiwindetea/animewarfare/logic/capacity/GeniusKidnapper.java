package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.Player;

public class GeniusKidnapper extends PlayerCapacity {
	public static class GeniusKidnapperActivable extends PlayerActivable {
		public GeniusKidnapperActivable(Player player) {
			super(player);
		}

		@Override
		public void destroy() {

		}
	}

	GeniusKidnapper(Player player) {
		super(player);
	}

	@Override
	public void use() {

	}

	@Override
	public CapacityName getType() {
		return CapacityName.GENIUS_KIDNAPPER;
	}
}
