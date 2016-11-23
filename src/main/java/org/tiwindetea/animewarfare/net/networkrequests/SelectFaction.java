package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class SelectFaction implements Serializable {

    private final FactionType factionType;
    private final GameClientInfo gameClientInfo;

    /**
     * Empty SelectFaction instance. Required by kryonet lib
     */
    public SelectFaction() {
        this.factionType = null;
        this.gameClientInfo = null;
    }

    public SelectFaction(FactionType factionType, GameClientInfo info) {
        this.factionType = factionType;
        this.gameClientInfo = info;
    }

    public FactionType getFactionType() {
        return this.factionType;
    }

    public GameClientInfo getGameClientInfo() {
        return this.gameClientInfo;
    }
}
