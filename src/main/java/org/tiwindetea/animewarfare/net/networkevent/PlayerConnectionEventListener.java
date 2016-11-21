package org.tiwindetea.animewarfare.net.networkevent;

import java.util.EventListener;

/**
 * Interface fo PlayerConnectionEvent listeners
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public interface PlayerConnectionEventListener extends EventListener {

    /**
     * Method invoked when a new player connects
     *
     * @param player The newly connected player
     */
    void handleConnection(PlayerConnectionEvent player);
}
