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
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Holocaust extends PlayerCapacity {
	public static class HolocaustActivable extends PlayerActivable implements UnitCounterEventListener {
		private final GameMap map;

		public HolocaustActivable(Player player, GameMap map) {
			super(player);
			this.map = map;

			LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitCounterEvent event) {
			if (event.getUnitType() == UnitType.LELOUCH) {
				activateAndDestroy(new Holocaust(getPlayer(), this.map));
			}
		}
	}

	private final ExecutorService pool = Executors.newSingleThreadExecutor();
	private final GameMap map;
	private final Random random = new Random();

	Holocaust(Player player, GameMap map) {
		super(player);
		this.map = map;
	}

	@Override
	public void use() {
		int result = this.random.nextInt(6) + 1;

		// TODO: Send random for the GUI.

		int count = (int) this.map.getZones()
		                          .stream()
		                          .filter(z -> z.hasUnitOfWithLevelEqualOrGreater(UnitLevel.LVL1))
		                          .count();

		if (result > count) {
			// TODO: Asks player to move a mascot
		} else {
			// TODO: Sinon, les adversaires ont deux minutes pour se répartir le résultat du dé en humiliation de mascottes.

			this.pool.submit(() -> {
				try {
					Thread.sleep(TimeUnit.MINUTES.toMillis(2));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				endOfTheTimer();
			});
		}
	}

	private void endOfTheTimer() {
		// TODO: S'ils n'y parviennent pas, vous choisissez qui est humilié.
	}

	@Override
	public CapacityName getName() {
		return CapacityName.HOLOCAUST;
	}
}
