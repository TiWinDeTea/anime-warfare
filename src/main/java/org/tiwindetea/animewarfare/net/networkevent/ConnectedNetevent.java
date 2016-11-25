package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.Room;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class ConnectedNetevent implements Event<ConnectedNeteventListener> {

    private final Room room;

    public ConnectedNetevent(Room room) {
        this.room = room;
    }

    @Override
    public void notify(ConnectedNeteventListener listener) {
        listener.handleSelfConnection(this);
    }

    public Room getRoom() {
        return this.room;
    }
}
