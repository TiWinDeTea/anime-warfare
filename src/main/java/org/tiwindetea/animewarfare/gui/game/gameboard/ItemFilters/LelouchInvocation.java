package org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters;

import javafx.scene.control.MenuItem;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.game.CostModifierMonitor;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.gui.game.UnitCountMonitor;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetInvokeUnitRequest;

import java.util.Collections;
import java.util.List;

public class LelouchInvocation extends AbstractUnitFilter {
	@Override
	public List<MenuItem> apply(FactionType factionType, GUnit gUnit) {
		if (actionMenuState != GCAMState.NOTHING) {
			return Collections.emptyList();
		}

		if (GamePhaseMonitor.getCurrentPhase() == PhaseChangedEvent.Phase.ACTION
				&& GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) == factionType) {
			if (!UnitCountMonitor.getInstance().hasBeenInvoked(UnitType.LELOUCH) && gUnit.getType() == UnitType.CC) {
				int cost = UnitType.LELOUCH.getDefaultCost() + CostModifierMonitor.getUnitCostModifier(UnitType.LELOUCH);
				MenuItem menuItem = new MenuItem("Replace by Lelouch (" + cost + " SP).");
				menuItem.setOnAction(e -> MainApp.getGameClient()
				                                 .send(new NetInvokeUnitRequest(UnitType.LELOUCH,
						                                 gUnit.getZone())));
				if (GameLayoutController.getLocalPlayerInfoPane()
				                        .getStaffCounter()
				                        .getValue() < cost) {
					menuItem.setDisable(true);
				}

				return Collections.singletonList(menuItem);
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
