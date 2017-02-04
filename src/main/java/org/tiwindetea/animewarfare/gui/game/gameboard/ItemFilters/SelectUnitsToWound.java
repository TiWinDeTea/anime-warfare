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

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.event.ZoneClickedEvent;
import org.tiwindetea.animewarfare.gui.game.event.ZoneClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEvent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectWoundedUnitsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class SelectUnitsToWound extends AbstractUnitFilter implements BattleNeteventListener {

	private int numberOfWoundedUnits = -1;
	private int zoneID = -1;
	private FactionType opponent;

	private final Collection<GUnit> selectedUnits = new ArrayList<>();
	private final Collection<GUnit> deadUnits = new ArrayList<>();
	private final Collection<GUnit> movingUnits = new ArrayList<>();
	private final Map<Integer, Integer> linksID = new HashMap<>();
	private final Set<MoveUnitsEvent.Movement> movements = new HashSet<>();

	private Button finish;
	private final ZoneClickedEventListener listener;
	private boolean unregistered = true;


	public SelectUnitsToWound() {
		EventDispatcher.registerListener(BattleNetevent.class, this);

		this.listener = (e -> {
			if (GameMap.areAdjacent(e.getZoneID(), this.zoneID) &&
					!GameLayoutController.getMap()
							.getUnits(e.getZoneID())
							.stream()
							.anyMatch(a -> a.isUnit() && a.getType().getDefaultFaction() == this.opponent)) {
				for (GUnit unit : this.selectedUnits) {
					this.movements.add(new MoveUnitsEvent.Movement(unit.getGameID(), unit.getZone(), e.getZoneID()));
					this.linksID.put(new Integer(unit.getGameID()),
							new Integer(GameLayoutController.getMap().linkTo(unit, e.getMouseEvent().getX(), e.getMouseEvent().getY())));
				}
				this.movingUnits.addAll(this.selectedUnits);
				this.selectedUnits.clear();
				deregister();
			}
		});
	}

	private void deregister() {
		this.unregistered = true;
		EventDispatcher.unregisterListener(ZoneClickedEvent.class, this.listener);
	}

	@Override
	public List<MenuItem> apply(FactionType factionType, GUnit gUnit) {
		if (actionMenuState != GCAMState.SELECTING_WOUNDEDS || !gUnit.getFaction().equals(factionType)) {
			return Collections.emptyList();
		} else {
			initFinishButton(factionType);

			// trying to cancel first
			if (this.selectedUnits.remove(gUnit)) {
				gUnit.setOpacity(1);
			} else if (this.deadUnits.remove(gUnit)) {
				cancelDeathGraphically(gUnit);
			} else if (this.movingUnits.remove(gUnit)) {
				GameLayoutController.getMap().removeLink(this.linksID.remove(new Integer(gUnit.getGameID())).intValue());
				cancelMoveGraphically(gUnit);
			} else if (this.selectedUnits.isEmpty()) {
				MenuItem kill = new MenuItem("Kill " + gUnit.getType()); // todo externalize
				MenuItem wound = new MenuItem("Select " + gUnit.getType() + " for a wound"); // todo externalize
				if (this.numberOfWoundedUnits > this.movements.size() && gUnit.getZone() == this.zoneID) {
					kill.setOnAction(e -> {
						gUnit.cross(Color.RED);
						this.deadUnits.add(gUnit);
						this.movements.add(new MoveUnitsEvent.Movement(gUnit.getGameID(), gUnit.getZone(), gUnit.getZone()));
					});
					wound.setOnAction(e -> {
						gUnit.setOpacity(0.5);
						this.selectedUnits.add(gUnit);
						if (this.unregistered) {
							EventDispatcher.registerListener(ZoneClickedEvent.class, this.listener);
							this.unregistered = false;
						}
					});
				} else {
					kill.setDisable(true);
					wound.setDisable(true);
				}
				return Arrays.asList(kill, wound);
			} else {
				gUnit.setOpacity(0.5);
				this.selectedUnits.add(gUnit);
				if (this.unregistered) {
					EventDispatcher.registerListener(ZoneClickedEvent.class, this.listener);
					this.unregistered = false;
				}
			}
		}
		return Collections.emptyList();
	}

	private void initFinishButton(FactionType factionType) {
		if (this.finish == null) {
			this.finish = addButton("Done");
			this.finish.setOnAction(e -> {
				remove(this.finish);
				this.finish = null;
				if (this.numberOfWoundedUnits == this.movements.size()
						|| this.movements.size() == GameLayoutController.getMap().getUnits(this.zoneID).stream().filter(unit -> unit.getFaction().equals(factionType)).count()) {
					MainApp.getGameClient().send(new NetSelectWoundedUnitsRequest(this.movements));
					GameLayoutController.getMap().removeLinks();
					this.linksID.clear();
					this.movements.clear();
					this.selectedUnits.clear();
					this.deadUnits.forEach(this::cancelDeathGraphically);
					this.deadUnits.clear();
					this.opponent = null;
					this.zoneID = -1;
				}
			});
		}
	}

	private void cancelMoveGraphically(GUnit gUnit) {
		this.movements.removeIf(mov -> (mov.getUnitID() == gUnit.getGameID()));
		gUnit.setOpacity(1);
	}

	private void cancelDeathGraphically(GUnit gUnit) {
		gUnit.uncross();
		this.movements.removeIf(mov -> (mov.getUnitID() == gUnit.getGameID()));
	}

	@Override
	public void destroy() {
		EventDispatcher.unregisterListener(BattleNetevent.class, this);
	}

	@Override
	public void handlePreBattle(BattleNetevent event) {
		// nothing to do
	}

	@Override
	public void handleDuringBattle(BattleNetevent event) {
		if (event.getAttacker().equals(MainApp.getGameClient().getClientInfo())) {
			this.opponent = GlobalChat.getClientFaction(event.getDefender());
			this.numberOfWoundedUnits = event.getNumberOfWoundeds().get(MainApp.getGameClient().getClientInfo());
			this.zoneID = event.getZone();
		} else if (event.getDefender().equals(MainApp.getGameClient().getClientInfo())) {
			this.opponent = GlobalChat.getClientFaction(event.getAttacker());
			this.numberOfWoundedUnits = event.getNumberOfWoundeds().get(MainApp.getGameClient().getClientInfo());
			this.zoneID = event.getZone();
		} else {
			this.numberOfWoundedUnits = -1;
		}
	}

	@Override
	public void handlePostBattle(BattleNetevent event) {
		if (this.numberOfWoundedUnits >= 0) {
			this.actionMenuState = GCAMState.SELECTING_WOUNDEDS;
		}
	}

	@Override
	public void handleBattleFinished(BattleNetevent event) {
		AbstractFilter.actionMenuState = GCAMState.NOTHING;
	}
}
