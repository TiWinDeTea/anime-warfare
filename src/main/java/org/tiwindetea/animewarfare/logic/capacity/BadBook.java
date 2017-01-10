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
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.BadBookCapacityChoseEvent;
import org.tiwindetea.animewarfare.net.logicevent.BadBookCapacityChoseEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BadBook extends PlayerCapacity implements BadBookCapacityChoseEventListener {
	public static class BadBookActivable extends PlayerActivable implements StudioEventListener {
		private int studioCounter;

		public BadBookActivable(Player player) {
			super(player);
			LogicEventDispatcher.registerListener(StudioEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(StudioEvent.class, this);
		}

		@Override
		public void handleStudioAddedEvent(StudioEvent event) {
			// nothing to do
		}

		@Override
		public void handleStudioRemovedEvent(StudioEvent studioEvent) {
			// nothing to do
		}

		@Override
		public void handleStudioBuiltOrDestroyed(StudioEvent studioEvent) {
			if (studioEvent.getType() == StudioEvent.Type.CREATED) {
				if (++this.studioCounter == 8) {
					activateAndDestroy(new BadBook(getPlayer()));
				}
			} else if (studioEvent.getType() == StudioEvent.Type.DELETED) {
				--this.studioCounter;
			}
		}
	}

	private final List<CapacityName> capacities = new ArrayList<>();
	private CapacityName capacityToDesactivate;

	BadBook(Player player) {
		super(player);

		LogicEventDispatcher.registerListener(BadBookCapacityChoseEvent.class, this);
	}

	@Override
	public void use() {
		getPlayer().desactivateCapactiy(this.capacityToDesactivate);

		CapacityName capacityName;
		Random random = new Random();
		do {
			capacityName = this.capacities.get(random.nextInt(this.capacities.size()));
		} while (!getPlayer().hasCapacity(capacityName));

		// TODO: Big switch
	}

	@Override
	public void destroy() {
		LogicEventDispatcher.unregisterListener(BadBookCapacityChoseEvent.class, this);
	}

	@Override
	public void handleCapacityChose(BadBookCapacityChoseEvent event) {
		if (getPlayer().getActivatedCapacities().size() == 6 || !getPlayer().hasRequiredStaffPoints(getName().getStaffCost())) {
			return;
		}

		this.capacityToDesactivate = event.getName();
		getPlayer().decrementStaffPoints(getName().getStaffCost());
	}

	@Override
	public CapacityName getName() {
		return CapacityName.BAD_BOOK;
	}
}
