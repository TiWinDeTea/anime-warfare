package org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters;

import javafx.scene.control.MenuItem;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.gui.game.gameboard.GStudio;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MarketFlooding extends DrawUnit {
	@Override
	public List<MenuItem> apply(FactionType factionType, GStudio studio) {
		if (GamePhaseMonitor.getCurrentPhase() != PhaseChangedEvent.Phase.ACTION
				|| GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) != factionType
				|| actionMenuState != GCAMState.NOTHING) {
			return Collections.emptyList();
		}

		List<MenuItem> items = new LinkedList<>();
		generateDrawUnitMenu(factionType, studio, items);

		return items;
	}

	@Override
	public void destroy() {

	}
}
