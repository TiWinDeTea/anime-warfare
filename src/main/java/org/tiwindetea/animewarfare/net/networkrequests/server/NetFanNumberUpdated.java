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

import org.tiwindetea.animewarfare.logic.events.NumberOfFansChangedEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSendable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetFanNumberUpdated implements NetSendable {

    private final GameClientInfo player;
    private final int diff;
    private final int newValue;

    /**
     * Default constructor, required by Kryo.net
     */
    public NetFanNumberUpdated() {
        this.player = null;
        this.diff = this.newValue = 0;
    }

    public NetFanNumberUpdated(NumberOfFansChangedEvent event, GameClientInfo player) {
        this.player = player;
        this.diff = event.getDiff();
        this.newValue = event.getNewVal();
    }

    public GameClientInfo getPlayer() {
        return this.player;
    }

    public int getDiff() {
        return this.diff;
    }

    public int getNewValue() {
        return this.newValue;
    }
}
