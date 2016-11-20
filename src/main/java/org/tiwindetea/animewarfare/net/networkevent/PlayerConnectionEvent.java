package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.GameClientInfo;

/**
 * Created by Lucas on 20/11/2016.
 */
public class PlayerConnectionEvent implements Event<PlayerConnectionEventListener> {

    private final GameClientInfo client;

    public PlayerConnectionEvent(GameClientInfo client) {
        this.client = client;
    }

    public void notify(PlayerConnectionEventListener listener) {
        listener.handleConnection(this);
    }

    public GameClientInfo getClient() {
        return this.client;
    }
}
