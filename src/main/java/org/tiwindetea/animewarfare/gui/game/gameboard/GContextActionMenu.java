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

package org.tiwindetea.animewarfare.gui.game.gameboard;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.PaperButton;
import org.tiwindetea.animewarfare.gui.game.event.GStudioClickedEvent;
import org.tiwindetea.animewarfare.gui.game.event.GStudioClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.event.GUnitClickedEvent;
import org.tiwindetea.animewarfare.gui.game.event.GUnitClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.event.ZoneClickedEvent;
import org.tiwindetea.animewarfare.gui.game.event.ZoneClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.AbstractFilter;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.AbstractStudioFilter;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.AbstractUnitFilter;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.AbstractZoneFilter;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.CaptureUnit;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.DrawMascot;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.DrawUnit;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.EngageBattle;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.HimejiInvocation;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.LelouchInvocation;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.Move;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.OpenStudio;
import org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters.SelectUnitToCapture;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNeteventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GContextActionMenu extends ContextMenu
        implements GUnitClickedEventListener, GStudioClickedEventListener, ZoneClickedEventListener,
        GameEndedNeteventListener {

    private final DynamicItemList items;
    private Pane ownerNode;

    public GContextActionMenu(FactionType playerFaction, Pane ownerNode) {
        this.ownerNode = ownerNode;
        this.items = new DynamicItemList(playerFaction);
        EventDispatcher.registerListener(GUnitClickedEvent.class, this);
        EventDispatcher.registerListener(ZoneClickedEvent.class, this);
        EventDispatcher.registerListener(GStudioClickedEvent.class, this);
        EventDispatcher.registerListener(GameEndedNetevent.class, this);
    }

    public void setOwnerNode(Pane node) {
        this.ownerNode = node;
    }

    public void destroy() {
        EventDispatcher.unregisterListener(GUnitClickedEvent.class, this);
        EventDispatcher.unregisterListener(ZoneClickedEvent.class, this);
        EventDispatcher.unregisterListener(GStudioClickedEvent.class, this);
        EventDispatcher.unregisterListener(GameEndedNetevent.class, this);
        this.items.destroy();
    }

    @Override
    public void handleClick(ZoneClickedEvent event) {
        if (event.getMouseEvent().getButton() == MouseButton.SECONDARY) {
            setAndShow(this.items.getItems(event.getZoneID()), event.getMouseEvent());
        }
    }

    @Override
    public void handleClick(GUnitClickedEvent event) {
        if (event.getMouseEvent().getButton() == MouseButton.SECONDARY) {
            setAndShow(this.items.getItems(event.getUnit()), event.getMouseEvent());
        }
    }

    @Override
    public void handleClick(GStudioClickedEvent event) {
        if (event.getMouseEvent().getButton() == MouseButton.SECONDARY) {
            setAndShow(this.items.getItems(event.getStudio()), event.getMouseEvent());
        }
    }

    @Override
    public void handleGameEnd(GameEndedNetevent gameEndedEvent) {
        this.destroy();
    }

    public void addUnitContextManager(String id, AbstractUnitFilter handler) {
        this.items.addUnitContextManager(id, handler);
    }

    public BiFunction<FactionType, GUnit, List<MenuItem>> removeUnitContextManager(String id) {
        return this.items.removeUnitContextManager(id);
    }

    public void addStudioContextManager(String id, AbstractStudioFilter handler) {
        this.items.addStudioContextManager(id, handler);
    }

    public BiFunction<FactionType, GStudio, List<MenuItem>> removeStudioContextManager(String id, BiFunction<FactionType, GStudio, List<MenuItem>> handler) {
        return this.items.removeStudioContextManager(id);
    }

    private void setAndShow(Stream<MenuItem> localItems, MouseEvent mouseEvent) {
        ObservableList<MenuItem> parentItems = getItems();
        parentItems.clear();
        localItems.forEach(parentItems::add);
        if (!parentItems.isEmpty()) {
            show(this.ownerNode, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
    }

    private class DynamicItemList {
        private final FactionType playerFaction;

        private final Map<String, AbstractUnitFilter> filteredUnitItems = new HashMap<>();
        private final Map<String, AbstractStudioFilter> filteredStudioItems = new HashMap<>();
        private final Map<String, AbstractZoneFilter> filteredZoneItems = new HashMap<>();
        // The biFunction should never return null. Use Collections.emptyList() instead.
        // Keys should be written in lower case

        DynamicItemList(FactionType playerFaction) {
            this.playerFaction = playerFaction;
            initFilters();
        }

        void destroy() {
            clearAll();
        }

        public void addUnitContextManager(String id, AbstractUnitFilter handler) {
            this.filteredUnitItems.put(id, handler);
        }

        public BiFunction<FactionType, GUnit, List<MenuItem>> removeUnitContextManager(String id) {
            return this.filteredUnitItems.remove(id);
        }

        public void addStudioContextManager(String id, AbstractStudioFilter handler) {
            this.filteredStudioItems.put(id, handler);
        }

        public BiFunction<FactionType, GStudio, List<MenuItem>> removeStudioContextManager(String id) {
            return this.filteredStudioItems.remove(id);
        }

        Stream<MenuItem> getItems(GUnit unit) {
            Stream<MenuItem> stream = Stream.empty();
            for (BiFunction<FactionType, GUnit, List<MenuItem>> biFunction : this.filteredUnitItems.values()) {
                stream = Stream.concat(stream, biFunction.apply(this.playerFaction, unit).stream());
            }
            return stream;
        }

        Stream<MenuItem> getItems(GStudio studio) {
            Stream<MenuItem> stream = Stream.empty();
            for (BiFunction<FactionType, GStudio, List<MenuItem>> biFunction : this.filteredStudioItems.values()) {
                stream = Stream.concat(stream, biFunction.apply(this.playerFaction, studio).stream());
            }
            return stream;
        }

        Stream<MenuItem> getItems(int zoneID) {
            Integer id = new Integer(zoneID);
            Stream<MenuItem> stream = Stream.empty();
            for (BiFunction<FactionType, Integer, List<MenuItem>> biFunction : this.filteredZoneItems.values()) {
                stream = Stream.concat(stream, biFunction.apply(this.playerFaction, id).stream());
            }
            return stream;
        }

        // helper
        private void clearAll() {
            this.filteredStudioItems.values().forEach(AbstractFilter::destroy);
            this.filteredStudioItems.clear();

            this.filteredUnitItems.values().forEach(AbstractFilter::destroy);
            this.filteredUnitItems.clear();

            this.filteredZoneItems.values().forEach(AbstractFilter::destroy);
            this.filteredZoneItems.clear();
        }


        private void initFilters() {
            // == unit filters ==
            AbstractUnitFilter.buttonAdder = GContextActionMenu.this::addButton;
            AbstractUnitFilter.buttonRemover = GContextActionMenu.this::remove;

            AbstractUnitFilter move = new Move();
            this.filteredUnitItems.put(move.getName(), move);

            AbstractUnitFilter openStudio = new OpenStudio();
            this.filteredUnitItems.put(openStudio.getName(), openStudio);

            AbstractUnitFilter selectUnitToCapture = new SelectUnitToCapture();
            this.filteredUnitItems.put(selectUnitToCapture.getName(), selectUnitToCapture);

            AbstractUnitFilter lelouchInvocation = new LelouchInvocation();
            this.filteredUnitItems.put(lelouchInvocation.getName(), lelouchInvocation);

            // == studio filters ==
            AbstractStudioFilter drawUnit = new DrawUnit();
            this.filteredStudioItems.put(drawUnit.getName(), drawUnit);

            // == zone filters ==
            AbstractZoneFilter drawMascot = new DrawMascot();
            this.filteredZoneItems.put(drawMascot.getName(), drawMascot);

            AbstractZoneFilter captureUnit = new CaptureUnit();
            this.filteredZoneItems.put(captureUnit.getName(), captureUnit);

            AbstractZoneFilter engageBattle = new EngageBattle();
            this.filteredZoneItems.put(engageBattle.getName(), engageBattle);

            AbstractZoneFilter himejiInvocation = new HimejiInvocation();
            this.filteredZoneItems.put(himejiInvocation.getName(), himejiInvocation);
        }
    }

    private Button addButton(String text) {
        Button button = new PaperButton(text);
        button.setPrefWidth(150);
        button.setPrefHeight(30);
        this.ownerNode.getChildren().add(button);
        return button;
    }

    private void remove(Button... buttons) {
        this.ownerNode.getChildren().removeAll(buttons);
    }
}
