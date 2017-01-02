package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.Player;

abstract class PlayerActivable implements SelfActivable {
	protected final Player player;

	protected PlayerActivable(Player player) {
		this.player = player;
	}
}
