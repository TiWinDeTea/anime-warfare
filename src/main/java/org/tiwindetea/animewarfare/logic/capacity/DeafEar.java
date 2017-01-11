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
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.net.logicevent.DeafEarZoneChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.DeafEarZoneChoiceEventListener;

public class DeafEar extends PlayerCapacity implements DeafEarZoneChoiceEventListener {
	public static class DeafEarActivable extends PlayerActivable implements UnitCounterEventListener {
		public DeafEarActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitCounterEvent event) {
			if (event.getType() == UnitCounterEvent.Type.ADDED && event.getUnitType().isLevel(UnitLevel.HERO)) {
				if (!getPlayer().hasFaction(event.getUnitType().getDefaultFaction())) {
					activateAndDestroy(new DeafEar(getPlayer()));
				}
			}
		}
	}

	DeafEar(Player player) {
		super(player);
	}

	@Override
	public void use() {
		LogicEventDispatcher.registerListener(DeafEarZoneChoiceEvent.class, this);
	}

	@Override
	public void destroy() {
		LogicEventDispatcher.unregisterListener(DeafEarZoneChoiceEvent.class, this);
	}

	@Override
	public void handleDeafEarZoneChoice(DeafEarZoneChoiceEvent event) {

	}

	@Override
	public CapacityName getName() {
		return CapacityName.DEAF_EAR;
	}
}
