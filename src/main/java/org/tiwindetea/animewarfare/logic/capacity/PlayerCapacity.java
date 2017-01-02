package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.Player;

abstract class PlayerCapacity implements Capacity {
	protected final Player player;

	protected PlayerCapacity(Player player) {
		this.player = player;
	}
}
