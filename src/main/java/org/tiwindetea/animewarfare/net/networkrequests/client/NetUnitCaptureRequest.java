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

package org.tiwindetea.animewarfare.net.networkrequests.client;

import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.GameClientInfo;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetUnitCaptureRequest implements NetSendable {
    private final int targetPlayer;
    private final int zoneID;
    private final UnitType unitType;

    /**
     * @param targetPlayer Player you want to assault
     * @param zoneID       ID of the zone where you want to attack
     * @param unitType
     */
    public NetUnitCaptureRequest(int targetPlayer, int zoneID, UnitType unitType) {
        this.targetPlayer = targetPlayer;
        this.zoneID = zoneID;
        this.unitType = unitType;
    }

    /**
     * @param targetPlayer Player you want to assault
     * @param zoneID       ID of the zone where you want to attack
     * @param unitType
     */
    public NetUnitCaptureRequest(GameClientInfo targetPlayer, int zoneID, UnitType unitType) {
        this.targetPlayer = targetPlayer.getId();
        this.zoneID = zoneID;
        this.unitType = unitType;
    }

    public int getTargetPlayer() {
        return this.targetPlayer;
    }

    public int getZoneID() {
        return this.zoneID;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }
}
