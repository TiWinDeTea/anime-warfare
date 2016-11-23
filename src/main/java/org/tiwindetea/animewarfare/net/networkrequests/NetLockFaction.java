package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClientInfo;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetLockFaction {

    private final FactionType faction;
    private final GameClientInfo gameClientInfo;

    /**
     * Empty Lockfaction instance. Required by kryonet lib
     */
    public NetLockFaction() {
        this.faction = null;
        this.gameClientInfo = null;
    }

    public NetLockFaction(FactionType faction, GameClientInfo info) {
        this.faction = faction;
        this.gameClientInfo = info;
    }

    public FactionType getFaction() {
        return this.faction;
    }

    public GameClientInfo getGameClientInfo() {
        return this.gameClientInfo;
    }
}
