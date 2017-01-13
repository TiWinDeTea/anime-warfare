package org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters;

import javafx.scene.control.MenuItem;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.game.CostModifierMonitor;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetInvokeUnitRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LelouchInvocation extends AbstractUnitFilter {
	@Override
	public List<MenuItem> apply(FactionType factionType, GUnit gUnit) {
		if (GamePhaseMonitor.getCurrentPhase() == PhaseChangedEvent.Phase.ACTION && GlobalChat.getClientFaction(
				PlayerTurnMonitor.getCurrentPlayer()) == factionType) {
			if (gUnit.getType() == UnitType.CC) {
				int cost = UnitType.LELOUCH.getDefaultCost() + CostModifierMonitor.getUnitCostModifier(UnitType.LELOUCH);
				if (GameLayoutController.getLocalPlayerInfoPane()
				                        .getStaffCounter()
				                        .getValue() >= cost) {
					MenuItem menuItem = new MenuItem("Replace by Lelouch");
					menuItem.setOnAction(e -> MainApp.getGameClient()
					                                 .send(new NetInvokeUnitRequest(UnitType.LELOUCH,
							                                 gUnit.getZone())));

					return Arrays.asList(menuItem);
				}
			}
		}

		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return "lelouch invocation";
	}

	@Override
	public void destroy() {

	}
}
