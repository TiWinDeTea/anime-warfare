package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GameStartedNetevent implements Event<GameStartedNeteventListener> {

    @Override
    public void notify(GameStartedNeteventListener listener) {
        listener.handleGameStart();
    }
}
