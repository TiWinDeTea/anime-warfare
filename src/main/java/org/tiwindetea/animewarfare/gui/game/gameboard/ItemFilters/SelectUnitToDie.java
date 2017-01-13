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

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectUnitsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Benoît CORTIER
 */
public class SelectUnitToDie extends AbstractUnitFilter implements BattleNeteventListener {
	private int zoneID = -1;
	private int numberOfDeads;
	private List<GUnit> selectedToDie = new ArrayList<>();
	private Button validadeDeathButton;

	public SelectUnitToDie() {
		EventDispatcher.registerListener(BattleNetevent.class, this);
	}

	@Override
	public List<MenuItem> apply(FactionType factionType, GUnit unit) {
		if (unit.getZone() == this.zoneID && unit.getFaction() == factionType) {
			if (this.selectedToDie.contains(unit)) {
				MenuItem menuItem = new MenuItem("Unselect"); // TODO: externalize
				menuItem.setOnAction(e -> {
					this.selectedToDie.remove(unit);
					unit.setOpacity(1);
				});
				return Arrays.asList(menuItem);
			} else {
				MenuItem menuItem = new MenuItem("Select to die");
				menuItem.setOnAction(e -> {
					this.selectedToDie.add(unit);
					unit.setOpacity(0.5);
				});
				if (this.selectedToDie.size() + 1 > this.numberOfDeads) {
					menuItem.setDisable(true);
				}
				return Arrays.asList(menuItem);
			}
		}

		return Collections.emptyList();
	}

	@Override
	public void destroy() {
		this.selectedToDie.clear();
		EventDispatcher.unregisterListener(BattleNetevent.class, this);
	}

	@Override
	public void handlePreBattle(BattleNetevent event) {
		// nothing to do
	}

	@Override
	public void handleDuringBattle(BattleNetevent event) {
		this.selectedToDie.clear();
		this.zoneID = event.getZone();
		this.numberOfDeads = event.getNumberOfDeads().get(MainApp.getGameClient().getClientInfo());
		Platform.runLater(() -> {
			this.validadeDeathButton = addButton("Validate death"); // TODO: externalize
			this.validadeDeathButton.setOnAction(e -> MainApp.getGameClient().send(new NetSelectUnitsRequest(
					this.selectedToDie.stream().map(gu -> gu.getGameID()).collect(Collectors.toSet())
			)));
		});
	}

	@Override
	public void handlePostBattle(BattleNetevent event) {
		this.selectedToDie.clear(); // just to be safe
		this.zoneID = -1;
		Platform.runLater(() -> {
			remove(this.validadeDeathButton);
		});
	}

	@Override
	public void handleBattleFinished(BattleNetevent event) {
		// nothing to do
	}
}
