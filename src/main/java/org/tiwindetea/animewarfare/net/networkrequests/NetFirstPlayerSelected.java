package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetFirstPlayerSelected implements Serializable {

    private final GameClientInfo firstPlayer;

    public NetFirstPlayerSelected() {
        this.firstPlayer = null;
    }

    public NetFirstPlayerSelected(GameClientInfo gameClientInfo) {
        this.firstPlayer = gameClientInfo;
    }

    public GameClientInfo getFirstPlayer() {
        return this.firstPlayer;
    }
}
