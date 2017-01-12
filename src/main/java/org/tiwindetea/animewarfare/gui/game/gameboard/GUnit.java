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

import com.esotericsoftware.minlog.Log;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.GUnitClickedEvent;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitCreatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitDeletedNetevent;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GUnit extends GComponent {

    private static final Map<UnitType, Image> PICTURES = new HashMap<>();
    private static final TreeSet<GUnit> units = new TreeSet<>(Comparator.comparingInt(GUnit::getGameID));
    private static boolean initialized = false;

    private final UnitType type;
    private final int ID;

    static {
        PICTURES.put(UnitType.KUDOU_ASUKA,
                new Image(GUnit.class.getResourceAsStream("pictures/asuka_circle.png")));
        PICTURES.put(UnitType.CC,
                new Image(GUnit.class.getResourceAsStream("pictures/CC_circle.png")));
        PICTURES.put(UnitType.CTHUKO,
                new Image(GUnit.class.getResourceAsStream("pictures/Cthuka_circle.png")));
        PICTURES.put(UnitType.HASUTA,
                new Image(GUnit.class.getResourceAsStream("pictures/Hasuta_circle.png")));
        PICTURES.put(UnitType.HIMEJI_MIZUKI,
                new Image(GUnit.class.getResourceAsStream("pictures/Himeji_circle.png")));
        PICTURES.put(UnitType.SAKAMAKI_IZAYOI,
                new Image(GUnit.class.getResourceAsStream("pictures/Izayoi_circle.png")));
        PICTURES.put(UnitType.RUSSELL_JIN,
                new Image(GUnit.class.getResourceAsStream("pictures/JinRussell_circle.png")));
        PICTURES.put(UnitType.KALLEN,
                new Image(GUnit.class.getResourceAsStream("pictures/Kallen_circle.png")));
        PICTURES.put(UnitType.TSUCHIYA_KOUTA,
                new Image(GUnit.class.getResourceAsStream("pictures/Kouta_circle.png")));
        PICTURES.put(UnitType.KUROUSAGI,
                new Image(GUnit.class.getResourceAsStream("pictures/Kurousagi_circle.png")));
        PICTURES.put(UnitType.LELOUCH,
                new Image(GUnit.class.getResourceAsStream("pictures/Lelouch_circle.png")));
        PICTURES.put(UnitType.YASAKA_MAHIRO,
                new Image(GUnit.class.getResourceAsStream("pictures/Mahiro_circle.png")));
        PICTURES.put(UnitType.SHIMADA_MINAMI,
                new Image(GUnit.class.getResourceAsStream("pictures/Minami_circle.png")));
        PICTURES.put(UnitType.NUNNALLY,
                new Image(GUnit.class.getResourceAsStream("pictures/Nunally_circle.png")));
        PICTURES.put(UnitType.NYARUKO,
                new Image(GUnit.class.getResourceAsStream("pictures/Nyaruko_circle.png")));
        PICTURES.put(UnitType.SUZAKU,
                new Image(GUnit.class.getResourceAsStream("pictures/Suzaku_circle.png")));
        PICTURES.put(UnitType.KUREI_TAMAO,
                new Image(GUnit.class.getResourceAsStream("pictures/Tamao_circle.png")));
        PICTURES.put(UnitType.YOSHII_AKIHISA,
                new Image(GUnit.class.getResourceAsStream("pictures/Yoshii_circle.png")));
        PICTURES.put(UnitType.KASUKABE_YOU,
                new Image(GUnit.class.getResourceAsStream("pictures/You_circle.png")));
        PICTURES.put(UnitType.SAKAMOTO_YUUJI,
                new Image(GUnit.class.getResourceAsStream("pictures/Yuuji_circle.png")));
    }

    private GUnit(int ID, UnitType type, FactionType faction, GameClientInfo owner) {
        super(PICTURES.get(type));
        //todo : css
        this.ID = ID;
        this.type = type;

        //getChildren().add(s);
        initializeImage(owner);

        setOnMouseClicked(e -> EventDispatcher.send(new GUnitClickedEvent(e, this)));
        setFactionType(faction);
        getImageView().setOnMouseClicked(getOnMouseClicked());
    }

    private void initializeImage(GameClientInfo owner) {
        Circle s = new Circle();
        s.translateXProperty().bind(s.radiusProperty());
        s.translateYProperty().bind(s.radiusProperty());
        s.radiusProperty().bind(Bindings.divide(getImageView().fitWidthProperty(), 2));

        Circle background = new Circle();
        background.radiusProperty().bind(s.radiusProperty());
        background.setFill(GlobalChat.getClientColor(owner));
        background.setStroke(GlobalChat.getClientColor(owner));
        getChildren().add(background);

        getImageView().setClip(s);
        getImageView().setPreserveRatio(true);
        getImageView().toFront();

        Circle border = new Circle();
        border.radiusProperty().bind(s.radiusProperty());
        border.setFill(Color.TRANSPARENT);
        border.setStroke(GlobalChat.getClientColor(owner));
        border.setStrokeWidth(2);
        getChildren().add(border);

        if (UnitLevel.MASCOT.equals(this.type.getUnitLevel())) {
            getImageView().setFitWidth(20);
        } else if (UnitLevel.HERO.equals(this.type.getUnitLevel())) {
            getImageView().setFitWidth(28);
        } else {
            getImageView().setFitWidth(24);
        }
    }

    private GUnit(int ID) {
        this.ID = ID;
        this.type = null;
    }

    public int getGameID() {
        return this.ID;
    }

    public UnitType getType() {
        return this.type;
    }

    /**
     * @param id ID of the unit
     * @return the unit or {@code null} if the is no such unit.
     */
    public static GUnit get(int id) {
        GUnit unit = units.floor(new GUnit(id));
        if (unit != null && unit.getGameID() == id) {
            return unit;
        } else {
            return null;
        }
    }

    public static NavigableSet<GUnit> getAll() {
        return Collections.unmodifiableNavigableSet(units.descendingSet());
    }

    /**
     * Return the unit, creating it if it doesn't exist.
     */
    public static GUnit getOrCreate(int id, UnitType type, FactionType factionType, GameClientInfo owner) {
        GUnit unit = get(id);
        if (unit == null) {
            unit = new GUnit(id, type, factionType, owner);
            units.add(unit);
        }
        return unit;
    }

    /**
     * Creates a unit. Any unit with the same ID will be overwride
     */
    public static void create(int id, UnitType type, FactionType factionType, GameClientInfo owner) {
        units.add(new GUnit(id, type, factionType, owner));
    }

    public static GUnit createAndForget(UnitType type, FactionType factionType, GameClientInfo owner) {
        return new GUnit(-1, type, factionType, owner);
    }

    /**
     * Deletes a unit
     */
    public static void delete(int id) {
        units.remove(new GUnit(id));
    }

    /**
     * Initializes the factory, registering unit creation and deletion events.
     */
    public static void initFactory() {
        if (!initialized) {
            EventDispatcher.registerListener(UnitCreatedNetevent.class
                    , e -> GUnit.create(e.getUnitId(), e.getUnitType(), e.getFactionType(), GlobalChat.getFactionClient(e.getFactionType())));

            EventDispatcher.registerListener(UnitDeletedNetevent.class
                    , e -> GUnit.delete(e.getUnitId()));

            EventDispatcher.registerListener(GameEndedNetevent.class, e -> units.clear());
            initialized = true;
        } else {
            Log.warn("Tried to init GUnit factory (at least) twice !");
        }
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

    @Override
    public boolean isUnit() {
        return true;
    }
}
