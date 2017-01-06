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

import org.tiwindetea.animewarfare.logic.events.StudioEvent;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetStudio implements NetReceivable {
    private final int playerID;
    private final int studioID;
    private final StudioEvent.Type type;


    /**
     * Default constructor, required by Kryo.net
     */
    public NetStudio() {
        this.studioID = 0;
        this.playerID = 0;
        this.type = null;
    }

    public NetStudio(StudioEvent event) {
        if (event.getType() == StudioEvent.Type.ADDED || event.getType() == StudioEvent.Type.REMOVED) {
            this.studioID = event.getZoneID();
            this.playerID = -1;
        } else {
            this.playerID = event.getPlayerID();
            this.studioID = -1;
        }

        this.type = event.getType();
    }

    public int getStudioID() {
        return this.studioID;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public StudioEvent.Type getType() {
        return this.type;
    }
}
