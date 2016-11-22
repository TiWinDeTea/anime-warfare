package org.tiwindetea.animewarfare.net;

import java.io.Serializable;

/**
 * Class to represent a game client
 * Basically a wrapper for its ID an
 * its name.
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GameClientInfo implements Serializable {
    String gameClientName;
    int id;

    GameClientInfo() {

    }

    GameClientInfo(int id) {
        this.id = id;
    }

    GameClientInfo(String gameClientName) {
        this.gameClientName = gameClientName;
    }

    GameClientInfo(String gameClientName, int id) {
        this.gameClientName = gameClientName;
        this.id = id;
    }

    public String getGameClientName() {
        return this.gameClientName;
    }

    public int getId() {
        return this.id;
    }
}