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

package org.tiwindetea.animewarfare.gui.game;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.PaperButton;
import org.tiwindetea.animewarfare.gui.event.GStudioClickedEvent;
import org.tiwindetea.animewarfare.gui.event.GStudioClickedEventListener;
import org.tiwindetea.animewarfare.gui.event.GUnitClickedEvent;
import org.tiwindetea.animewarfare.gui.event.GUnitClickedEventListener;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEventListener;
import org.tiwindetea.animewarfare.gui.game.ItemFilters.AbstractStudioFilter;
import org.tiwindetea.animewarfare.gui.game.ItemFilters.AbstractUnitFilter;
import org.tiwindetea.animewarfare.gui.game.ItemFilters.DrawUnit;
import org.tiwindetea.animewarfare.gui.game.ItemFilters.Move;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangeNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangedNeteventListener;

import java.util.ArrayList;
import java.util.Collection;
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
        setAndShow(this.items.getItems(event.getZoneID()), event.getMouseEvent());
    }

    @Override
    public void handleClick(GUnitClickedEvent event) {
        setAndShow(this.items.getItems(event.getUnit()), event.getMouseEvent());
    }

    @Override
    public void handleClick(GStudioClickedEvent event) {
        setAndShow(this.items.getItems(event.getStudio()), event.getMouseEvent());
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

    private class DynamicItemList implements PhaseChangedNeteventListener {
        private final FactionType playerFaction;

        private final List<MenuItem> unitsItems = new ArrayList<>(); // for all units
        private final List<MenuItem> ennemyUnitsItems = new ArrayList<>(); // for all ennemy units
        private final List<MenuItem> friendlyUnitsItems = new ArrayList<>(); // for all friendly units
        private final Map<UnitType, List<MenuItem>> itemsPerUnitType = new HashMap<>(); // for all units given their types
        private final Map<Integer, List<MenuItem>> itemsPerUnit = new HashMap<>(); // for a particular unit
        //never set a list to null (you can clearit or remove it though)

        private final List<MenuItem> studioItems = new ArrayList<>();
        private final List<MenuItem> friendlyStudioItems = new ArrayList<>();
        private final Map<Integer, List<MenuItem>> studioItemsPerStudio = new HashMap<>();
        private final Map<Integer, List<MenuItem>> zoneItems = new HashMap<>();
        //same apply here

        private final Map<String, AbstractUnitFilter> filteredUnitItems = new HashMap<>();
        private final Map<String, AbstractStudioFilter> filteredStudioItems = new HashMap<>();
        // The biFunction should never return null. Use Collections.emptyList() instead.
        // Keys should be written in lower case

        DynamicItemList(FactionType playerFaction) {
            this.playerFaction = playerFaction;
            initFilters();

            //register events
            EventDispatcher.registerListener(PhaseChangeNetevent.class, this);
        }

        void destroy() {
            clearAll();
            // unregister events
            EventDispatcher.unregisterListener(PhaseChangeNetevent.class, this);
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
            List<MenuItem> perUnitTypeItems = this.itemsPerUnitType.get(unit);
            if (perUnitTypeItems == null) {
                perUnitTypeItems = new ArrayList<>(0);
            }

            List<MenuItem> perUnitItems = this.itemsPerUnit.get(new Integer(unit.gameID()));
            if (perUnitItems == null) {
                perUnitItems = new ArrayList<>(0);
            }

            Stream<MenuItem> stream = Stream.concat(
                    Stream.concat(this.unitsItems.stream(), perUnitItems.stream()),
                    perUnitTypeItems.stream()
            );

            for (BiFunction<FactionType, GUnit, List<MenuItem>> biFunction : this.filteredUnitItems.values()) {
                stream = Stream.concat(stream, biFunction.apply(this.playerFaction, unit).stream());
            }

            if (unit.getFaction().equals(this.playerFaction)) {
                return Stream.concat(stream, this.friendlyUnitsItems.stream());
            } else {
                return Stream.concat(stream, this.ennemyUnitsItems.stream());
            }
        }

        Stream<MenuItem> getItems(GStudio studio) {
            Collection<MenuItem> items = this.studioItemsPerStudio.get(new Integer(studio.getZoneID()));

            Stream<MenuItem> stream = Stream.concat(
                    (items == null
                            ? Stream.empty()
                            : items.stream())
                    , this.studioItems.stream());

            if (GlobalChat.getClientFaction(MainApp.getGameClient().getClientInfo()).equals(studio.getFaction())) {
                stream = Stream.concat(stream, this.friendlyUnitsItems.stream());
            }

            for (BiFunction<FactionType, GStudio, List<MenuItem>> biFunction : this.filteredStudioItems.values()) {
                stream = Stream.concat(stream, biFunction.apply(this.playerFaction, studio).stream());
            }

            return stream;
        }

        Stream<MenuItem> getItems(int zoneID) {
            Collection<MenuItem> items = this.zoneItems.get(new Integer(zoneID));
            return items == null ? new ArrayList<MenuItem>(0).stream() : items.stream();
        }

        @Override
        public void handlePhaseChanged(PhaseChangeNetevent event) {
            /*clearAll();

            if (event.getPhase().equals(PhaseChangedEvent.Phase.ACTION)) {
                MenuItem unitInfos = new MenuItem("Unit infos");
                unitInfos.setOnAction(e -> System.out.println("TODO: Open unit infos!"));
                this.unitsItems.add(unitInfos);

                MenuItem moveUnit = new MenuItem("Move unit");
                moveUnit.setOnAction(e -> System.out.println("TODO: move unit context."));
                this.friendlyUnitsItems.add(moveUnit);

                MenuItem drawUnit = new MenuItem("Draw unit");
                drawUnit.setOnAction(e -> System.out.println("TODO: Open a menu to select the unit to draw."));
                this.friendlyStudioItems.add(drawUnit);
            }*/
        }

        // helper
        private void clearAll() {
            this.unitsItems.clear();
            this.ennemyUnitsItems.clear();
            this.friendlyUnitsItems.clear();
            this.itemsPerUnitType.clear();
            this.itemsPerUnit.clear();
            this.studioItemsPerStudio.clear();
            this.zoneItems.clear();
            //this.filteredStudioItems.clear();
            //this.filteredUnitItems.clear();
        }


        private void initFilters() {
            AbstractUnitFilter.buttonAdder = GContextActionMenu.this::addButton;
            AbstractUnitFilter.buttonRemover = GContextActionMenu.this::remove;

            AbstractUnitFilter move = new Move();
            this.filteredUnitItems.put(move.getName(), move);

            AbstractStudioFilter drawUnit = new DrawUnit();
            this.filteredStudioItems.put(drawUnit.getName(), drawUnit);
        }
    }

    private Button addButton(String text) {
        Button button = new PaperButton(text);
        this.ownerNode.getChildren().add(button);
        return button;
    }

    private void remove(Button... buttons) {
        this.ownerNode.getChildren().removeAll(buttons);
    }
}
