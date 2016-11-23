package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class FirstPlayerSelected implements Serializable {

    private final GameClientInfo gameClientInfo;

    public FirstPlayerSelected(GameClientInfo gameClientInfo) {
        this.gameClientInfo = gameClientInfo;
    }

    public GameClientInfo getGameClientInfo() {
        return this.gameClientInfo;
    }
}
