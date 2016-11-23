package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.NetSelectFaction;

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

    public PlayerSelectedFactionNetevent(NetSelectFaction faction) {
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
