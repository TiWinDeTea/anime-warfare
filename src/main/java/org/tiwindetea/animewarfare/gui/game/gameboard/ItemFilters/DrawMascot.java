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

package org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters;

import javafx.scene.control.MenuItem;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.game.CostModifierMonitor;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.gui.game.UnitCountMonitor;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetInvokeUnitRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Benoît CORTIER
 * @since 0.1.0
 */
public class DrawMascot extends AbstractZoneFilter {

	private UnitType cachedFactionMascot;

	@Override
	public List<MenuItem> apply(FactionType factionType, Integer zoneId) {
		if (GamePhaseMonitor.getCurrentPhase() != PhaseChangedEvent.Phase.ACTION
				|| GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) != factionType
				|| actionMenuState != GCAMState.NOTHING) {
			return Collections.emptyList();
		}

		System.out.println(UnitCountMonitor.getInstance().getNumberOfUnitsByFaction(factionType));
		if (GameLayoutController.getMap().getComponents(zoneId).stream().noneMatch(c -> c.getFaction() == factionType)
				&& UnitCountMonitor.getInstance().getNumberOfUnitsByFaction(factionType) != 0) {
			return Collections.emptyList();
		}

		if (this.cachedFactionMascot == null || this.cachedFactionMascot.getDefaultFaction() != factionType) {
			this.cachedFactionMascot = Arrays.stream(UnitType.values())
					.filter(u -> u.getDefaultFaction() == factionType && u.getUnitLevel() == UnitLevel.MASCOT)
					.findFirst()
					.orElse(null);
		}

		List<MenuItem> items = new LinkedList<>();
		int cost = this.cachedFactionMascot.getDefaultCost() + CostModifierMonitor.getUnitCostModifier(this.cachedFactionMascot);

		MenuItem item = new MenuItem("Draw " + this.cachedFactionMascot.toString() + " (" + cost + " SP)"); // todo: externalie
		item.setOnAction(e -> MainApp.getGameClient().send(new NetInvokeUnitRequest(this.cachedFactionMascot, zoneId)));
		items.add(item);

		if (GameLayoutController.getLocalPlayerInfoPane().getStaffCounter().getValue() < cost) {
			item.setDisable(true);
		}
		return items;
	}

	@Override
	public String getName() {
		return "draw_mascot";
	}

	@Override
	public void destroy() {
		// nothing to do
	}
}
