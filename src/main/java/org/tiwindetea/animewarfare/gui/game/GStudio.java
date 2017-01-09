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

import com.esotericsoftware.minlog.Log;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.GStudioClickedEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioNeteventListener;

import java.util.Comparator;
import java.util.Random;
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
        Random r = new Random();
        this.zoneID = zoneID;
        this.ownerRectangle = new Rectangle(10, 10, Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        this.ownerRectangle.setStrokeWidth(2);
        this.ownerRectangle.setStroke(Color.BLACK);
        this.ownerRectangle.setFill(Color.TRANSPARENT);
        getChildren().add(this.ownerRectangle);

        setOnMouseClicked(e -> EventDispatcher.send(new GStudioClickedEvent(e, this)));
    }

    public void setZoneID(int zoneID) {
        this.zoneID = zoneID;
    }

    public int getZoneID() {
        return this.zoneID;
    }

    public void setTeam(GameClientInfo client) {
        if (client == null) {
            this.ownerRectangle.setVisible(false);
        } else {
            this.ownerRectangle.setVisible(true);
            this.ownerRectangle.setStroke(GlobalChat.getClientColor(client));
        }
    }

    public void setTeam(int playerID) {
        // todo
    }


    public static GStudio get(int zoneID) {
        GStudio studio = STUDIOS.floor(new GStudio(zoneID));
        if (studio != null && studio.getZoneID() == zoneID) {
            return studio;
        } else {
            return null;
        }
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
            EventDispatcher.registerListener(StudioNetevent.class, new StudioNeteventListener() {
                @Override
                public void handleStudioCreation(StudioNetevent event) {
                    create(event.getZoneID());
                }

                @Override
                public void handleStudioRemoved(StudioNetevent event) {
                    delete(event.getZoneID());
                }

                @Override
                public void handleStudioDeserted(StudioNetevent event) {
                    getOrCreate(event.getZoneID()).setTeam(null);
                }

                @Override
                public void handleStudioCaptured(StudioNetevent event) {
                    getOrCreate(event.getZoneID()).setTeam(event.getPlayerID());
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
        if (o instanceof GStudio) {
            return ((GStudio) o).zoneID == this.zoneID;
        }
        return false;
    }
}
