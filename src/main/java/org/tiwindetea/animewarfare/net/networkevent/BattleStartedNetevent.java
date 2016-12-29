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
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBattleStarted;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class BattleStartedNetevent implements Event<BattleStartedNeteventListener> {

    private final GameClientInfo attacker;
    private final GameClientInfo defender;
    private final int zone;

    public BattleStartedNetevent(NetBattleStarted battleStarted) {
        this.attacker = battleStarted.getAttacker();
        this.defender = battleStarted.getDefender();
        this.zone = battleStarted.getZone();
    }

    @Override
    public void notify(BattleStartedNeteventListener listener) {
        listener.handleBattleStart(this);
    }

    public GameClientInfo getAttacker() {
        return this.attacker;
    }

    public GameClientInfo getDefender() {
        return this.defender;
    }

    public int getZone() {
        return this.zone;
    }
}

