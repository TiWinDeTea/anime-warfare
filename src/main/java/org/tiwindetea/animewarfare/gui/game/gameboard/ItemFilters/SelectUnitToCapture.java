package org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters;

import javafx.scene.control.MenuItem;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.net.networkevent.SelectUnitToCaptureRequestNetevent;
import org.tiwindetea.animewarfare.net.networkevent.SelectUnitToCaptureRequestNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetCapturedUnitSelection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectUnitToCapture extends AbstractUnitFilter implements SelectUnitToCaptureRequestNeteventListener {
	private int zoneID;
	private UnitLevel unitLevel;

	public SelectUnitToCapture() {
		EventDispatcher.registerListener(SelectUnitToCaptureRequestNetevent.class, this);
	}

	@Override
	public List<MenuItem> apply(FactionType factionType, GUnit gUnit) {
		if (gUnit.getType()
		         .isLevel(this.unitLevel) && gUnit.getFaction() == factionType && gUnit.getZone() == this.zoneID) {
			MenuItem menuItem = new MenuItem("Select this unit.");
			menuItem.setOnAction(e -> {
				this.zoneID = -1;
				this.unitLevel = null;
				MainApp.getGameClient().send(new NetCapturedUnitSelection(gUnit.getGameID()));
				GameLayoutController.getMap().unHighlightFxThread(gUnit.getZone());
			});

			return Arrays.asList(menuItem);
		}

		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return "select unit to capture";
	}

	@Override
	public void destroy() {
		EventDispatcher.unregisterListener(SelectUnitToCaptureRequestNetevent.class, this);
	}

	@Override
	public void handleMascotSelectionRequest(SelectUnitToCaptureRequestNetevent event) {
		this.zoneID = event.getZoneId();
		this.unitLevel = event.getUnitLevel();
	}
}
