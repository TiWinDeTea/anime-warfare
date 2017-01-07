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

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GUnit extends GComponent {

    private static final HashMap<UnitType, Image> pictures = new HashMap<>();
    private static final TreeSet<GUnit> units = new TreeSet<>(Comparator.comparingInt(GUnit::getID));

    private final int ID;

    static {
        //todo : initialize pictures
    }

    private GUnit(int ID, UnitType type, GameClientInfo owner) {
        super(pictures.get(type));
        //todo : css
        this.ID = ID;
        Circle s = new Circle();
        s.setStroke(GlobalChat.getClientColor(owner));
        s.setStrokeWidth(2);
        s.setFill(Color.TRANSPARENT);
        if (UnitLevel.MASCOT.equals(type.getUnitLevel())) {
            s.setRadius(10);
        } else if (UnitLevel.HERO.equals(type.getUnitLevel())) {
            s.setRadius(14);
        } else {
            s.setRadius(12);
        }
        getChildren().add(s);
    }

    private GUnit(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    /**
     * @param id ID of the unit
     * @return the unit or {@code null} if the is no such unit.
     */
    public static GUnit get(int id) {
        GUnit unit = units.floor(new GUnit(id));
        if (unit != null && unit.getID() == id) {
            return unit;
        } else {
            return null;
        }
    }

    /**
     * Return the unit, creating it if it doesn't exist.
     */
    public static GUnit getOrCreate(int id, UnitType type, GameClientInfo owner) {
        GUnit unit = get(id);
        if (unit == null) {
            unit = new GUnit(id, type, owner);
            units.add(unit);
        }
        return unit;
    }

    /**
     * Creates a unit. Any unit with the same ID will be overwride
     */
    public static void create(int id, UnitType type, GameClientInfo owner) {
        units.add(new GUnit(id, type, owner));
    }

    /**
     * Deletes a unit
     */
    public static void delete(int id) {
        units.remove(new GUnit(id));
    }

    @Override
    public int hashCode() {
        return this.ID;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GUnit && this.equals((GUnit) o);
    }

    public boolean equals(GUnit u) {
        if (u != null) {
            return this.ID == u.ID;
        }
        return false;
    }

}
