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

package org.tiwindetea.animewarfare.gui.game;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkevent.UnitCreatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitCreatedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.UnitDeletedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitDeletedNeteventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class UnitCountMonitor implements UnitCreatedNeteventListener, UnitDeletedNeteventListener {
	private static final UnitCountMonitor MONITOR = new UnitCountMonitor();

	private UnitCountMonitor() {
		EventDispatcher.registerListener(UnitCreatedNetevent.class, this);
		EventDispatcher.registerListener(UnitDeletedNetevent.class, this);
	}

	public static UnitCountMonitor getInstance() {
		return MONITOR;
	}

	public static void init() {
	}

	private final int[] numberOfUnitsByType = new int[UnitType.values().length];
	private final List<UnitType> heroesAlreadyInvoked = new ArrayList<>();

	public int getNumberOfUnits(UnitType type) {
		return this.numberOfUnitsByType[type.ordinal()];
	}

	public boolean hasBeenInvoked(UnitType type) {
		assert (type.isLevel(UnitLevel.HERO));

		return this.heroesAlreadyInvoked.contains(type);
	}

	public boolean aHeroWasInvoked() {
		return !this.heroesAlreadyInvoked.isEmpty();
	}

	public int getNumberOfUnits() {
		return IntStream.of(this.numberOfUnitsByType).sum();
	}

	public int getNumberOfUnitsByFaction(FactionType factionType) {
		int sum = 0;
		for (UnitType unitType : UnitType.values()) {
			if (unitType.getDefaultFaction() == factionType) {
				sum += getNumberOfUnits(unitType);
			}
		}
		return sum;
	}

	@Override
	public void handleUnitCreation(UnitCreatedNetevent event) {
		++this.numberOfUnitsByType[event.getUnitType().ordinal()];

		if (event.getUnitType().isLevel(UnitLevel.HERO) && !this.heroesAlreadyInvoked.contains(event.getUnitType())) {
			this.heroesAlreadyInvoked.add(event.getUnitType());
		}
	}

	@Override
	public void handleUnitDeletion(UnitDeletedNetevent event) {
		--this.numberOfUnitsByType[event.getUnitType().ordinal()];
	}
}
