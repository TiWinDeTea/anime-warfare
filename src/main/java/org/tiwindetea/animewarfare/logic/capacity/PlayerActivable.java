////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

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
