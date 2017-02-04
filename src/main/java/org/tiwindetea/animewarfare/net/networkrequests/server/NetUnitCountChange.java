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
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.units.UnitType;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetUnitCountChange implements NetReceivable {

    private final UnitCounterEvent.Type type;
    private final FactionType factionType;
    private final UnitType unitType;
    private final int id;

    public NetUnitCountChange() {
        this.type = null;
        this.factionType = null;
        this.unitType = null;
        this.id = 0;
    }

    public NetUnitCountChange(UnitCounterEvent event) {
        this.type = event.getType();
        this.factionType = event.getFaction();
        this.unitType = event.getUnitType();
        this.id = event.getUnit().getID();
    }

    public UnitCounterEvent.Type getType() {
        return this.type;
    }

    public FactionType getFactionType() {
        return this.factionType;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public int getId() {
        return this.id;
    }
}
