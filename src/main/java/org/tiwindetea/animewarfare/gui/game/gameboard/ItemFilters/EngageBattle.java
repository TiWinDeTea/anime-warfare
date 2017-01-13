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
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.game.CostModifierMonitor;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetStartBattleRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Benoît CORTIER
 * @since 0.1.0
 */
public class EngageBattle extends AbstractZoneFilter implements BattleNeteventListener {
	public EngageBattle() {
		EventDispatcher.registerListener(BattleNetevent.class, this);
	}

	@Override
	public List<MenuItem> apply(FactionType factionType, Integer zoneId) {
		if (GamePhaseMonitor.getCurrentPhase() != PhaseChangedEvent.Phase.ACTION
				|| GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) != factionType
				|| (actionMenuState != GCAMState.NOTHING)) {
			return Collections.emptyList();
		}

		if (GameLayoutController.getMap().getComponents(zoneId).stream()
				.noneMatch(c -> c.getFaction() == factionType
						&& c instanceof GUnit
						&& ((GUnit) c).getType().getUnitLevel() != UnitLevel.MASCOT)) {
			return Collections.emptyList();
		}

		Set<FactionType> possibleFactions = GameLayoutController.getMap().getComponents(zoneId).stream()
				.filter(c -> c.getFaction() != factionType
						&& c instanceof GUnit
						&& ((GUnit) c).getType().getUnitLevel() != UnitLevel.MASCOT)
				.map(c -> c.getFaction())
				.collect(Collectors.toSet());

		if (possibleFactions.size() == 0) {
			return Collections.emptyList();
		}

		int cost = 1 + CostModifierMonitor.getBattleCostModifier();

		List<MenuItem> items = new ArrayList<>();

		for (FactionType possibleFaction : possibleFactions) {
			MenuItem item = new MenuItem("Engage battle with " + possibleFaction.toString() + " (" + cost + " SP)"); // todo: externalie
			item.setOnAction(e -> MainApp.getGameClient().send(new NetStartBattleRequest(GlobalChat.getFactionClient(possibleFaction), zoneId)));
			items.add(item);
		}

		if (GameLayoutController.getLocalPlayerInfoPane().getStaffCounter().getValue() < cost) {
			items.forEach(m -> m.setDisable(true));
		}

		return items;
	}

	@Override
	public void destroy() {
		EventDispatcher.unregisterListener(BattleNetevent.class, this);
	}

	@Override
	public void handlePreBattle(BattleNetevent event) {
		AbstractFilter.actionMenuState = GCAMState.BATTLE;
	}

	@Override
	public void handleDuringBattle(BattleNetevent event) {
		// nothing to do
	}

	@Override
	public void handlePostBattle(BattleNetevent event) {
		// nothing to do
	}

	@Override
	public void handleBattleFinished(BattleNetevent event) {
		AbstractFilter.actionMenuState = GCAMState.NOTHING;
	}
}
