package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.LockFaction;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class PlayerLockedFactionNetevent implements Event<PlayerLockedFactionNeteventListener> {

    private final GameClientInfo playerInfo;
    private final FactionType faction;

    public PlayerLockedFactionNetevent() {
        this.faction = null;
        this.playerInfo = null;
    }

    public PlayerLockedFactionNetevent(FactionType faction, GameClientInfo playerId) {
        this.faction = faction;
        this.playerInfo = playerId;
    }

    public PlayerLockedFactionNetevent(LockFaction faction) {
        this.playerInfo = faction.getGameClientInfo();
        this.faction = faction.getFaction();
    }

    @Override
    public void notify(PlayerLockedFactionNeteventListener listener) {
        listener.handleFactionLock(this);
    }

    public GameClientInfo getPlayerInfo() {
        return this.playerInfo;
    }

    public FactionType getFaction() {
        return this.faction;
    }
}
