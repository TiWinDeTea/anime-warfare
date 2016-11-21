package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.GameClientInfo;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class PlayerConnectionEvent implements Event<PlayerConnectionEventListener> {

    private final GameClientInfo client;

    public PlayerConnectionEvent(GameClientInfo client) {
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    public void notify(PlayerConnectionEventListener listener) {
        listener.handleConnection(this);
    }

    public GameClientInfo getClient() {
        return this.client;
    }
}
