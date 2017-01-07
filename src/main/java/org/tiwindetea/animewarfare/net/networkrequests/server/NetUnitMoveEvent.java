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

package org.tiwindetea.animewarfare.net.networkrequests.server;

import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetUnitMoveEvent implements NetReceivable {

    private final int destination;
    private final int source;
    private final int ID;
    private final FactionType factionType;
    private final UnitType unitType;

    /**
     * Default constructor, required by Kryo.net
     */
    public NetUnitMoveEvent() {
        this.destination = 0;
        this.source = 0;
        this.ID = 0;
        this.factionType = null;
        this.unitType = null;
    }

    public NetUnitMoveEvent(UnitMovedEvent event) {

        if (event.getSource() == null) {
            this.source = event.getSource().getID();
        } else {
            this.source = Integer.MIN_VALUE;
        }

        if (event.getDestination() == null) {
            this.destination = event.getDestination().getID();
        } else {
            this.destination = Integer.MIN_VALUE;
        }

        this.ID = event.getSource().getID();
        this.factionType = event.getUnit().getFaction();
        this.unitType = event.getUnit().getType();
    }

    public int getDestination() {
        return this.destination;
    }

    public int getSource() {
        return this.source;
    }

    public int getID() {
        return this.ID;
    }

    public FactionType getFactionType() {
        return this.factionType;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

}

