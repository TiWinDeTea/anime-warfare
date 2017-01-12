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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.GStudioClickedEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioControllerChangedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioNeteventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */

public class GStudio extends GComponent {

    private static final TreeSet<GStudio> STUDIOS = new TreeSet<>(Comparator.comparingInt(GStudio::getZoneID));
    private static final Image STUDIO_IMAGE = null; // todo
    private static boolean initialized = false;

    private Rectangle ownerRectangle;
    private int zoneID;

    private GStudio(int zoneID) {
        super(STUDIO_IMAGE);
        this.zoneID = zoneID;
        this.ownerRectangle = new Rectangle(10, 10, Color.TRANSPARENT);
        this.ownerRectangle.setStrokeWidth(2);
        this.ownerRectangle.setStroke(GlobalChat.getDefaultColor());
        getChildren().add(this.ownerRectangle);

        setOnMouseClicked(e -> EventDispatcher.send(new GStudioClickedEvent(e, this)));
    }

    public void setZoneID(int zoneID) {
        this.zoneID = zoneID;
    }

    public int getZoneID() {
        return this.zoneID;
    }

    private void setTeam(GameClientInfo client) {
        if (client == null) {
            this.ownerRectangle.setStroke(GlobalChat.getDefaultColor());
            setFactionType(null);
        } else {
            this.ownerRectangle.setStroke(GlobalChat.getClientColor(client));
            setFactionType(GlobalChat.getClientFaction(client));
        }
    }


    public static GStudio get(int zoneID) {
        GStudio studio = STUDIOS.floor(new GStudio(zoneID));
        if (studio != null && studio.getZoneID() == zoneID) {
            return studio;
        } else {
            return null;
        }
    }

    public static NavigableSet<GStudio> getAll() {
        return Collections.unmodifiableNavigableSet(STUDIOS.descendingSet());
    }

    public static GStudio getOrCreate(int zoneID) {
        GStudio studio = get(zoneID);
        if (studio == null) {
            studio = new GStudio(zoneID);
            STUDIOS.add(studio);
        }
        return studio;
    }

    public static void create(int zoneID) {
        STUDIOS.add(new GStudio(zoneID));
    }

    public static void delete(int zoneID) {
        STUDIOS.remove(new GStudio(zoneID));
    }

    public static void initFactory() {

        if (!initialized) {
            EventDispatcher.registerListener(StudioControllerChangedNetevent.class,
                    event -> get(event.getZoneID()).setTeam(GlobalChat.getFactionClient(event.getControllerFaction())));
            EventDispatcher.registerListener(StudioNetevent.class, new StudioNeteventListener() {

                @Override
                public void handleStudioBuiltOrDestroyed(StudioNetevent studioNetevent) {
                    if (studioNetevent.getType() == StudioEvent.Type.CREATED) {
                        create(studioNetevent.getZoneID());
                    } else if (studioNetevent.getType() == StudioEvent.Type.DELETED) {
                        delete(studioNetevent.getZoneID());
                    }
                }

                @Override
                public void handleStudioPlayered(StudioNetevent studioNetevent) {
                    //get(studioNetevent.getZoneID()).setTeam(studioNetevent.getPlayerInfo());
                }

                @Override
                public void handleStudioMapped(StudioNetevent studioNetevent) {
                    //get(studioNetevent.getZoneID()).setTeam(studioNetevent.getPlayerInfo());
                }
            });
            EventDispatcher.registerListener(GameEndedNetevent.class, e -> STUDIOS.clear());
            initialized = true;
        } else {
            Log.warn("Tried to init GUnit factory (at least) twice !");
        }
    }

    @Override
    public int hashCode() {
        return this.zoneID;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof GStudio) && ((GStudio) o).zoneID == this.zoneID;
    }

    @Override
    public boolean isUnit() {
        return false;
    }
}
