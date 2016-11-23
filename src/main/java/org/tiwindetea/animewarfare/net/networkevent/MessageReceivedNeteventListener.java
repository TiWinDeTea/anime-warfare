package org.tiwindetea.animewarfare.net.networkevent;

import java.util.EventListener;

/**
 * Interface for MessageReceivedNetevent listeners
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public interface MessageReceivedNeteventListener extends EventListener {

    /**
     * Method invoked when a message is received.
     *
     * @param message The received message
     */
    void handleMessage(MessageReceivedNetevent message);
}
