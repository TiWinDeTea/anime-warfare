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
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetSelectUnitToCapture;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class SelectUnitToCaptureRequestNetevent implements Event<SelectUnitToCaptureRequestNeteventListener> {
    private final UnitLevel unitLevel;
    private final int zoneId;
    private final GameClientInfo player;

    public SelectUnitToCaptureRequestNetevent(NetSelectUnitToCapture selectMascotToCapture) {
        this.unitLevel = selectMascotToCapture.getUnitLevel();
        this.zoneId = selectMascotToCapture.getZoneId();
        this.player = selectMascotToCapture.getPlayer();
    }

    @Override
    public void notify(SelectUnitToCaptureRequestNeteventListener listener) {
        listener.handleMascotSelectionRequest(this);
    }

    public UnitLevel getUnitLevel() {
        return this.unitLevel;
    }

    public int getZoneId() {
        return this.zoneId;
    }

    public GameClientInfo getPlayer() {
        return this.player;
    }
}
