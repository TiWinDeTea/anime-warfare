package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.GameClientInfo;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class PlayerConnectionNetevent implements Event<PlayerConnectionNeteventListener> {

    private final GameClientInfo client;

    public PlayerConnectionNetevent(GameClientInfo client) {
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(PlayerConnectionNeteventListener listener) {
        listener.handlePlayerConnection(this);
    }

    public GameClientInfo getClient() {
        return this.client;
    }
}
