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
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;

public class ForcedRetreat extends PlayerCapacity {
	public static class ForcedRetreatActivable extends PlayerActivable implements UnitMovedEventListener {
		public ForcedRetreatActivable(Player player) {
			super(player);
		}

		@Override
		public void destroy() {
		}

		@Override
		public void handleUnitMoved(UnitMovedEvent event) {
			if (event.getUnit().isLevel(UnitLevel.HERO)) {
				if (isHeroInTheSameZoneThanAnEnnemyHero(event)) {
					activateAndDestroy(new ForcedRetreat(getPlayer()));
				}
			}
		}

		private boolean isHeroInTheSameZoneThanAnEnnemyHero(UnitMovedEvent event) {
			boolean ennemyPresent = false;
			boolean heroPresent = false;

			for (Unit unit : event.getDestination().getUnits()) {
				if (unit.isLevel(UnitLevel.HERO)) {
					if (unit.hasFaction(getPlayer().getFaction())) {
						heroPresent = true;
					} else {
						ennemyPresent = true;
					}
				}
			}

			return ennemyPresent && heroPresent;
		}
	}

	ForcedRetreat(Player player) {
		super(player);
	}

	@Override
	public void use() {
	}

	@Override
	public CapacityName getName() {
		return CapacityName.FORCED_RETREAT;
	}
}
