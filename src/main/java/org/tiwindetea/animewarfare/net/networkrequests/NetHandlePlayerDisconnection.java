package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetHandlePlayerDisconnection implements Serializable {

    private final GameClientInfo player;

    /**
     * Default constructor used by kryonet
     */
    public NetHandlePlayerDisconnection() {
        this.player = null;
    }

    public NetHandlePlayerDisconnection(GameClientInfo player) {
        this.player = player;
    }

    public GameClientInfo getPlayer() {
        return this.player;
    }
}
