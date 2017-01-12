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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.gui.game.gameboard.GMap;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEvent;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetMoveUnitsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Lucas Lazare
 * @author Benoît CORTIER
 * @since 0.1.0
 */
public class Move extends AbstractUnitFilter {

    private final List<MoveUnitsEvent.Movement> movements = new ArrayList<>();
    private final List<GUnit> selectedUnits = new ArrayList<>();
    private final Map<Integer, Integer> linksID = new HashMap<>();
    private final List<ZoneClickedEventListener> clickedEventListeners = new ArrayList<>();
    private GMap map;

    private static Button cancel;
    private static Button done;

    @Override
    public List<MenuItem> apply(FactionType factionType, GUnit unit) {
        this.map = GameLayoutController.getMap();
        if (GamePhaseMonitor.getCurrentPhase() != PhaseChangedEvent.Phase.ACTION
                || GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) != factionType) {
            return Collections.emptyList();
        }

        GMap map = GameLayoutController.getMap();

        if (actionMenuState == GCAMState.NOTHING || actionMenuState == GCAMState.MOVING_UNITS) {
            if (factionType == unit.getFaction()) {
                if (!this.movements.stream().anyMatch(m -> m.getUnitID() == unit.gameID()) && !this.selectedUnits.contains(unit)) {
                    if (actionMenuState == GCAMState.MOVING_UNITS) {
                        select(unit);
                        return Collections.emptyList();
                    } else {

                        MenuItem item = new MenuItem("Move " + unit.getType() + " (1 SP)"); // todo externalize
                        if (this.movements.size() + this.selectedUnits.size() + 1 > GameLayoutController.getLocalPlayerInfoPane().getStaffCounter().getValue()) {
                            item.setDisable(true);
                        }

                        item.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                done = addButton("Done");
                                cancel = addButton("Cancel"); // todo externalize
                                done.setOnAction(e -> {
                                    MainApp.getGameClient().send(new NetMoveUnitsRequest(new HashSet<>(Move.this.movements)));
                                    clean();
                                });
                                cancel.setOnAction(e -> clean());
                                select(unit);
                            }

                            public void clean() {
                                actionMenuState = GCAMState.NOTHING;
                                map.removeLinks();
                                for (MoveUnitsEvent.Movement movement : Move.this.movements) {
                                    GUnit.get(movement.getUnitID()).setOpacity(1);
                                }
                                for (GUnit selectedUnit : Move.this.selectedUnits) {
                                    selectedUnit.setOpacity(1);
                                }
                                for (ZoneClickedEventListener clickedEventListener : Move.this.clickedEventListeners) {
                                    EventDispatcher.unregisterListener(ZoneClickedEvent.class, clickedEventListener);
                                }
                                map.unHighlightNeigbour(unit.getZone(), 1); // todo don't use 1 but unit move capacity instead
                                Move.this.selectedUnits.clear();
                                Move.this.movements.clear();
                                Move.this.linksID.clear();
                                remove(cancel, done);
                            }
                        });

                        return Arrays.asList(item);
                    }
                } else {
                    deselect(unit);
                    return Collections.emptyList();
                }
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    private void deselect(GUnit unit) {
        Integer linkId = this.linksID.remove(new Integer(unit.gameID()));
        if (linkId != null) {
            this.map.removeLink(linkId.intValue());
            this.movements.stream()
                    .filter(movement -> movement.getUnitID() == unit.gameID())
                    .findAny()
                    .ifPresent(k -> this.movements.remove(k));
            if (this.linksID.isEmpty() && this.movements.isEmpty()) {
                remove(cancel, done);
                actionMenuState = GCAMState.NOTHING;
            }
        } else {
            int index = this.selectedUnits.indexOf(unit);
            EventDispatcher.unregisterListener(ZoneClickedEvent.class, this.clickedEventListeners.remove(index));
            this.selectedUnits.remove(index);
        }
        unit.setOpacity(1);
    }

    private void select(GUnit unit) {

        unit.setOpacity(0.5);
        Move.this.selectedUnits.add(unit);
        this.map.highlightNeighbour(unit.getZone(), 1, Color.rgb(0xFF, 0xD7, 0x00, 0.3), Color.rgb(0xFF, 0xE7, 0x10, 0.5)); // todo don't use 1 but unit move capacity instead  // todo externalize


        actionMenuState = GCAMState.MOVING_UNITS;
        ZoneClickedEventListener listener = new ZoneClickedEventListener() {
            @Override
            public void handleClick(ZoneClickedEvent zoneClickedEvent) {
                if (GameMap.getDistanceBetween(unit.getZone(), zoneClickedEvent.getZoneID()) == 1) { // FIXME: take into account the unit speed.
                    Move.this.selectedUnits.clear();
                    deregister(this);
                    MouseEvent mE = zoneClickedEvent.getMouseEvent();
                    Move.this.linksID.put(new Integer(unit.gameID()), new Integer(Move.this.map.linkTo(unit, mE.getX(), mE.getY())));
                    Move.this.map.unHighlightNeigbour(unit.getZone(), 1); // todo don't use 1 but unit move capacity instead
                    Move.this.movements.add(new MoveUnitsEvent.Movement(unit.gameID(), unit.getZone(), zoneClickedEvent.getZoneID()));
                }
            }
        };
        EventDispatcher.registerListener(ZoneClickedEvent.class, listener);
        Move.this.clickedEventListeners.add(listener);
    }

    private void deregister(ZoneClickedEventListener listener) {
        EventDispatcher.unregisterListener(ZoneClickedEvent.class, listener);
    }

    @Override
    public String getName() {
        return "move";
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
