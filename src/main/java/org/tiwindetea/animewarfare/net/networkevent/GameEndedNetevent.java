package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.NetGameEnded;

import java.util.List;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GameEndedNetevent implements Event<GameEndedNeteventListener> {

    private final List<GameClientInfo> winners;

    public GameEndedNetevent(NetGameEnded gameEnded) {
        this.winners = gameEnded.getWinners();
    }

    @Override
    public void notify(GameEndedNeteventListener listener) {
        listener.handleGameEnd(this);
    }
}
