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
