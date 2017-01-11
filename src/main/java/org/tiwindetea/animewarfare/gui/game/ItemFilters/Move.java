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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.GMap;
import org.tiwindetea.animewarfare.gui.game.GUnit;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.GamePhaseMonitor;
import org.tiwindetea.animewarfare.gui.game.PlayerTurnMonitor;
import org.tiwindetea.animewarfare.logic.FactionType;
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
 * @since 0.1.0
 */
public class Move extends AbstractUnitFilter {

    private final List<MoveUnitsEvent.Movement> movements = new ArrayList<>();
    private final Map<Integer, Integer> linksID = new HashMap<>();
    private ZoneClickedEventListener lastListener;

    private static Button cancel;
    private static Button done;

    @Override
    public List<MenuItem> apply(FactionType factionType, GUnit unit) {
        if (GamePhaseMonitor.getCurrentPhase() != PhaseChangedEvent.Phase.ACTION
                || GlobalChat.getClientFaction(PlayerTurnMonitor.getCurrentPlayer()) != factionType) {
            return Collections.emptyList();
        }

        GMap map = GameLayoutController.getMap();

        if (actionMenuState == GCAMState.NOTHING || actionMenuState == GCAMState.MOVING_UNITS) {
            if (factionType == unit.getFaction()) {
                if (!this.movements.stream().anyMatch(m -> m.getUnitID() == unit.gameID())) {
                    MenuItem item = new MenuItem("Move " + unit.getType()); // todo externalize

                    item.setOnAction(new EventHandler<ActionEvent>() {
                        ZoneClickedEventListener listener;

                        @Override
                        public void handle(ActionEvent event) {
                            map.highlightNeighbour(unit.getZone(), 1); // todo don't use 1 but unit move capacity instead

                            if (actionMenuState != GCAMState.MOVING_UNITS) {

                                done = addButton("Done");
                                cancel = addButton("Cancel"); // todo externalize
                                done.setOnAction(e -> {
                                    MainApp.getGameClient().send(new NetMoveUnitsRequest(new HashSet<>(Move.this.movements)));
                                    clean();
                                });

                                cancel.setOnAction(e -> clean());
                            }

                            actionMenuState = GCAMState.MOVING_UNITS;
                            this.listener = zoneClickedEvent -> {
                                deregister();
                                unit.setOpacity(0.5);
                                MouseEvent mE = zoneClickedEvent.getMouseEvent();
                                Move.this.linksID.put(new Integer(unit.gameID()), new Integer(map.linkTo(unit, mE.getX(), mE.getY())));
                                map.unHighlightNeigbour(unit.gameID(), 1); // todo don't use 1 but unit move capacity instead
                                Move.this.movements.add(new MoveUnitsEvent.Movement(unit.gameID(), unit.getZone(), zoneClickedEvent.getZoneID()));
                            };
                            EventDispatcher.registerListener(ZoneClickedEvent.class, this.listener);
                            Move.this.lastListener = this.listener;
                        }

                        public void deregister() {
                            EventDispatcher.unregisterListener(ZoneClickedEvent.class, this.listener);
                        }

                        public void clean() {
                            actionMenuState = GCAMState.NOTHING;
                            map.removeLinks();
                            for (MoveUnitsEvent.Movement movement : Move.this.movements) {
                                GUnit.get(movement.getUnitID()).setOpacity(1);
                            }
                            deregister();
                            map.unHighlightNeigbour(unit.gameID(), 1); // todo don't use 1 but unit move capacity instead
                            Move.this.movements.clear();
                            Move.this.linksID.clear();
                            remove(cancel, done);
                        }
                    });


                    return Arrays.asList(item);
                } else {
                    MenuItem item = new MenuItem("Cancel " + unit.getType() + "'s move");
                    item.setOnAction(e -> {
                        map.removeLink(this.linksID.remove(new Integer(unit.gameID())).intValue()); // null pointer exception
                        this.movements.stream()
                                .filter(movement -> movement.getUnitID() == unit.gameID())
                                .findAny()
                                .ifPresent(k -> this.movements.remove(k));
                        unit.setOpacity(1);
                        if (this.linksID.isEmpty() && this.movements.isEmpty()) {
                            remove(cancel, done);
                            actionMenuState = GCAMState.NOTHING;
                        }
                    });
                    return Arrays.asList(item);
                }
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getName() {
        return "move";
    }
}
