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
import org.tiwindetea.animewarfare.logic.events.StaffPointUpdatedEvent;
import org.tiwindetea.animewarfare.logic.events.StaffPointUpdatedEventListener;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEvent;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEventListener;

public class Loan extends PlayerCapacity {
	public static class LoanActivable extends PlayerActivable implements StudioControllerChangedEventListener, StaffPointUpdatedEventListener {
		private int controlledStudioCount;

		public LoanActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(StudioControllerChangedEvent.class, this);
			LogicEventDispatcher.registerListener(StaffPointUpdatedEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(StudioControllerChangedEvent.class, this);
			LogicEventDispatcher.unregisterListener(StaffPointUpdatedEvent.class, this);
		}

		@Override
		public void handleStudioController(StudioControllerChangedEvent event) {
			if (getPlayer().hasFaction(event.getControllerFaction())) {
				++this.controlledStudioCount;
			}

			if (this.controlledStudioCount == 4) {
				getPlayer().activateCapacity(new Loan(getPlayer()));
			}
		}

		@Override
		public void onStaffPointChange(StaffPointUpdatedEvent event) {
			if (getPlayer().getID() == event.getPlayerID() && event.getStaffAvailable() >= 10) {
				getPlayer().activateCapacity(new Loan(getPlayer()));
			}
		}
	}

	Loan(Player player) {
		super(player);
	}

	@Override
	public void use() {
		// TODO
	}

	@Override
	public CapacityName getName() {
		return CapacityName.LOAN;
	}
}
