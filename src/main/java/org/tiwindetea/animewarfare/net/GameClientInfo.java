package org.tiwindetea.animewarfare.net;

import org.tiwindetea.animewarfare.net.networkrequests.NetworkedClass;

/**
 * Class to represent a game client
 * Basically a wrapper for its ID an
 * its name.
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GameClientInfo implements NetworkedClass {
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

    public GameClientInfo(String gameClientName, int id) {
        this.gameClientName = gameClientName;
        this.id = id;
    }

    public String getGameClientName() {
        return this.gameClientName;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameClientInfo) {
            return this.equals((GameClientInfo) o);
        }
        return false;
    }

    public boolean equals(GameClientInfo gci) {
        return gci.id == this.id && (gci.gameClientName == null ? this.gameClientName == null : gci.gameClientName.equals(this.gameClientName));
    }

    @Override
    public String toString() {
        return this.gameClientName;
    }
}