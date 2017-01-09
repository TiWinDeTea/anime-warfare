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
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.event.GStudioClickedEvent;
import org.tiwindetea.animewarfare.gui.event.GStudioClickedEventListener;
import org.tiwindetea.animewarfare.gui.event.GUnitClickedEvent;
import org.tiwindetea.animewarfare.gui.event.GUnitClickedEventListener;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEventListener;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNeteventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GContextActionMenu extends ContextMenu
        implements GUnitClickedEventListener, GStudioClickedEventListener, ZoneClickedEventListener,
        GameEndedNeteventListener {

    private final DynamicItemList items;
    private final Node ownerNode;

    public GContextActionMenu(FactionType playerFaction, Node ownerNode) {
        this.ownerNode = ownerNode;
        this.items = new DynamicItemList(playerFaction);
        EventDispatcher.registerListener(GUnitClickedEvent.class, this);
        EventDispatcher.registerListener(ZoneClickedEvent.class, this);
        EventDispatcher.registerListener(GStudioClickedEvent.class, this);
        EventDispatcher.registerListener(GameEndedNetevent.class, this);
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

    private void setAndShow(Stream<MenuItem> localItems, MouseEvent mouseEvent) {
        ObservableList<MenuItem> parentItems = getItems();
        parentItems.clear();
        localItems.forEach(parentItems::add);
        show(this.ownerNode, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    private class DynamicItemList {

        private final FactionType playerFaction;

        private final List<MenuItem> unitsItems = new ArrayList<>(); // for all units
        private final List<MenuItem> ennemyUnitsItems = new ArrayList<>(); // for all ennemy units
        private final List<MenuItem> friendlyUnitsItems = new ArrayList<>(); // for all friendly units
        private final HashMap<UnitType, List<MenuItem>> ItemsPerUnitType = new HashMap<>(); // for all units given their types
        private final HashMap<Integer, List<MenuItem>> ItemsPerUnit = new HashMap<>(); // for a particular unit
        //never set a list to null (you can clearit or remove it though)

        private final HashMap<Integer, List<MenuItem>> studioItems = new HashMap<>();
        private final HashMap<Integer, List<MenuItem>> zoneItems = new HashMap<>();
        //same apply here


        DynamicItemList(FactionType playerFaction) {
            this.playerFaction = playerFaction;

            // dummy example
            MenuItem sayHello = new MenuItem("Say hello with this unit");
            sayHello.setOnAction(e -> System.out.println("Hello !"));
            this.friendlyUnitsItems.add(sayHello);

            //register events
        }

        void destroy() {
            // deregister events
        }

        Stream<MenuItem> getItems(UnitType unit, int unitID) {

            List<MenuItem> perUnitTypeItems = this.ItemsPerUnitType.get(unit);
            if (perUnitTypeItems == null) {
                perUnitTypeItems = new ArrayList<>(0);
            }

            List<MenuItem> perUnitItems = this.ItemsPerUnit.get(new Integer(unitID));
            if (perUnitItems == null) {
                perUnitItems = new ArrayList<>(0);
            }

            if (unit.getDefaultFaction().equals(this.playerFaction)) {
                return Stream.concat(Stream.concat(this.unitsItems.stream(), this.friendlyUnitsItems.stream()),
                        Stream.concat(perUnitItems.stream(), perUnitTypeItems.stream()));
            } else {
                return Stream.concat(Stream.concat(this.unitsItems.stream(), this.ennemyUnitsItems.stream()),
                        Stream.concat(perUnitItems.stream(), perUnitTypeItems.stream()));
            }
        }

        Stream<MenuItem> getItems(GUnit unit) {
            return getItems(unit.getType(), unit.getID());
        }

        Stream<MenuItem> getItems(GStudio studio) {
            Collection<MenuItem> items = this.studioItems.get(new Integer(studio.getZoneID()));
            return items == null ? new ArrayList<MenuItem>(0).stream() : items.stream();
        }

        Stream<MenuItem> getItems(int zoneID) {
            Collection<MenuItem> items = this.zoneItems.get(new Integer(zoneID));
            return items == null ? new ArrayList<MenuItem>(0).stream() : items.stream();
        }
    }
}
