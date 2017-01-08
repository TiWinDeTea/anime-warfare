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
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.NetSelectFactionRequest;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class PlayerSelectedFactionNetevent implements Event<PlayerSelectedFactionNeteventListener> {

    private final GameClientInfo playerInfo;
    private final FactionType factionType;

    public PlayerSelectedFactionNetevent() {
        this.playerInfo = null;
        this.factionType = null;
    }

    public PlayerSelectedFactionNetevent(GameClientInfo playerId, FactionType factionType) {
        this.playerInfo = playerId;
        this.factionType = factionType;
    }

    public PlayerSelectedFactionNetevent(NetSelectFactionRequest faction) {
        this.playerInfo = faction.getGameClientInfo();
        this.factionType = faction.getFactionType();
    }

    @Override
    public void notify(PlayerSelectedFactionNeteventListener listener) {
        listener.handleFactionChoice(this);
    }

    public GameClientInfo getPlayerInfo() {
        return this.playerInfo;
    }

    public FactionType getFactionType() {
        return this.factionType;
    }
}
