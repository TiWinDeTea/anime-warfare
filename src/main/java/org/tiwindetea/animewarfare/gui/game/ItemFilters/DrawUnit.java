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

package org.tiwindetea.animewarfare.gui.game.ItemFilters;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.GStudio;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEvent;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetInvokeUnitRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Benoît CORTIER
 * @since 0.1.0
 */
public class DrawUnit extends AbstractStudioFilter {
	private final List<MoveUnitsEvent.Movement> movements = new ArrayList<>();
	private final Map<Integer, Integer> linksID = new HashMap<>();
	private ZoneClickedEventListener lastListener;

	private static Button cancel;
	private static Button done;

	@Override
	public List<MenuItem> apply(FactionType factionType, GStudio studio) {
		if (GamePhaseMonitor.getCurrentPhase() != PhaseChangedEvent.Phase.ACTION
				|| GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) != factionType
				|| actionMenuState != GCAMState.NOTHING
				|| factionType != studio.getFaction()) {
			return Collections.emptyList();
		}

		List<MenuItem> items = new LinkedList<>();
		for (UnitType unitType : UnitType.values()) {
			if (factionType == unitType.getDefaultFaction() && unitType.getUnitLevel() != UnitLevel.MASCOT) {
				MenuItem item = new MenuItem("Draw " + unitType.toString() + " (" + unitType.getDefaultCost() + "SP)"); // todo: externalie
				item.setOnAction(e -> MainApp.getGameClient().send(new NetInvokeUnitRequest(unitType, studio.getZoneID())));
				items.add(item);
			}
		}
		return items;
	}

	@Override
	public String getName() {
		return "draw_unit";
	}
}
