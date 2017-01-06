package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.Player;

import java.lang.ref.WeakReference;
import java.util.Objects;

abstract class PlayerCapacity implements Capacity {
	private final WeakReference<Player> player;

	protected PlayerCapacity(Player player) {
		this.player = new WeakReference<>(player);
	}

	public Player getPlayer() {
		Player player = this.player.get();

		Objects.requireNonNull(player);

		return player;
	}

	//protected abstract void destroy();
}
