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
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkevent.CostModifiedNetEvent;
import org.tiwindetea.animewarfare.net.networkevent.CostModifiedNetEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Benoît CORTIER
 */
public class CostModifierMonitor implements CostModifiedNetEventListener {
	private static final CostModifierMonitor MONITOR = new CostModifierMonitor();

	public static int getUnitCostModifier(UnitType type) {
		return CostModifierMonitor.MONITOR.unitCostModifiers.getOrDefault(type, 0);
	}

	public static int getUniqueActionCostModifier() {
		return CostModifierMonitor.MONITOR.uniqueActionCostModifier;
	}

	public static int getBattleCostModifier() {
		return CostModifierMonitor.MONITOR.battleCostModifier;
	}

	public static void init() {
	}

	private int battleCostModifier = 0;

	private int uniqueActionCostModifier = 0;

	private Map<UnitType, Integer> unitCostModifiers = new HashMap<>();

	private CostModifierMonitor() {
		EventDispatcher.registerListener(CostModifiedNetEvent.class, this);
	}

	@Override
	public void handleUniqueActionCostModifierEvent(CostModifiedNetEvent event) {
		if (!event.getGameClientInfo().equals(MainApp.getGameClient().getClientInfo())) {
			return;
		}

		this.uniqueActionCostModifier = event.getCost();
	}

	@Override
	public void handleBattleCostModifierEvent(CostModifiedNetEvent event) {
		if (!event.getGameClientInfo().equals(MainApp.getGameClient().getClientInfo())) {
			return;
		}

		this.battleCostModifier = event.getCost();
	}

	@Override
	public void handleUnitCostModifierEvent(CostModifiedNetEvent event) {
		if (!event.getGameClientInfo().equals(MainApp.getGameClient().getClientInfo())) {
			return;
		}

		this.unitCostModifiers.put(event.getUnitType(), event.getCost());
	}
}
