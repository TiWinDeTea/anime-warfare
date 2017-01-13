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
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.gui.game.gameboard.GStudio;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetAbandonStudioRequest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Benoît CORTIER
 * @since 0.1.0
 */
public class AbandonStudio extends AbstractStudioFilter {
	@Override
	public List<MenuItem> apply(FactionType factionType, GStudio studio) {
		if (GamePhaseMonitor.getCurrentPhase() != PhaseChangedEvent.Phase.ACTION
				|| GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) != factionType
				|| actionMenuState != GCAMState.NOTHING
				|| studio.getFaction() != factionType) {
			return Collections.emptyList();
		}

		List<MenuItem> items = new LinkedList<>();

		MenuItem item = new MenuItem("Abandon studio" + " (free)"); // todo: externalie
		item.setOnAction(e -> MainApp.getGameClient().send(new NetAbandonStudioRequest(studio.getZone())));
		items.add(item);

		return items;
	}

	@Override
	public void destroy() {
		// nothing to do
	}
}
