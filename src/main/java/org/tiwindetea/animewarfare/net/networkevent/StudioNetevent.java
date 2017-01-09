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
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStudio;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class StudioNetevent implements Event<StudioNeteventListener> {
    private final GameClientInfo playerInfo;
    private final int zoneID;
    private final StudioEvent.Type type;

    public StudioNetevent(NetStudio newStudio) {
        this.zoneID = newStudio.getZoneID();
        this.playerInfo = newStudio.getPlayerInfo();
        this.type = newStudio.getType();
    }

    @Override
    public void notify(StudioNeteventListener listener) {
        switch (this.type) {
            case ADDED:
                listener.handleStudioCreation(this);
                break;
            case ADDED_PLAYER:
                listener.handleStudioCaptured(this);
                break;
            case REMOVED:
                listener.handleStudioRemoved(this);
                break;
            case REMOVED_PLAYER:
                listener.handleStudioDeserted(this);
                break;
        }
    }

    public int getZoneID() {
        if (this.zoneID == -1) {
            throw new IllegalStateException();
        }

        return this.zoneID;
    }

    public GameClientInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            throw new IllegalStateException();
        }

        return this.playerInfo;
    }

    public StudioEvent.Type getType() {
        return this.type;
    }
}
