package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.Player;

import java.lang.ref.WeakReference;

abstract class PlayerActivable implements SelfActivable {
	private final WeakReference<Player> player;

	protected PlayerActivable(Player player) {
		this.player = new WeakReference<>(player);
	}

	protected void activateAndDestroy(Capacity capacity) {
		getPlayer().activateCapacity(capacity);
		destroy();
	}

	protected Player getPlayer() {
		Player player = this.player.get();
		if (player == null) {
			destroy();
			throw new NullPointerException();
		}

		return player;
	}
}
