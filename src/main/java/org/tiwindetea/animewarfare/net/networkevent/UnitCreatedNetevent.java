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

package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetUnitCountChange;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class UnitCreatedNetevent implements Event<UnitCreatedNeteventListener> {

    private final int unitId;
    private final FactionType factionType;
    private final UnitType unitType;

    public UnitCreatedNetevent(NetUnitCountChange unitCountChange) {
        this.unitId = unitCountChange.getId();
        this.factionType = unitCountChange.getFactionType();
        this.unitType = unitCountChange.getUnitType();
    }

    @Override
    public void notify(UnitCreatedNeteventListener listener) {
        listener.handleUnitCreation(this);
    }

    public int getUnitId() {
        return this.unitId;
    }

    public FactionType getFactionType() {
        return this.factionType;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }
}
