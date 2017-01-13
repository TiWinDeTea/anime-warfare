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

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;

import java.util.HashSet;
import java.util.Set;

public class MarketFlooding implements Capacity {
	public static class MarketFloodingActivable extends PlayerActivable implements UnitMovedEventListener {
		private Set<Integer> zoneWherePlayerDrewUnits = new HashSet<>();

		public MarketFloodingActivable(Player player) {
			super(player);

			activateAndDestroy(new MarketFlooding());
			LogicEventDispatcher.registerListener(UnitMovedEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitMovedEvent.class, this);
		}

		@Override
		public void handleUnitMoved(UnitMovedEvent event) {
			if (event.getSource() == null) {
				boolean result = this.zoneWherePlayerDrewUnits.add(new Integer(event.getDestination().getID()));
				if (result && this.zoneWherePlayerDrewUnits.size() == 4) {
					activateAndDestroy(new MarketFlooding());
				}
			}
		}
	}

	MarketFlooding() {
	}

	@Override
	public void use() {
		// Nothing to do
	}

	@Override
	public void destroy() {
		// Nothin to do here.
	}

	@Override
	public CapacityName getName() {
		return CapacityName.MARKET_FLOODING;
	}
}
