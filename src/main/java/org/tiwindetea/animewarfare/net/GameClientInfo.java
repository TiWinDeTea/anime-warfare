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

    public GameClientInfo(int id) {
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

    /**
     * @return The id of the client. If the id is -1, it should be consider to be a special server client.
     */
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
        return gci == null ? false : gci.id == this.id;
    }

    public boolean equals(int id) {
        return this.id == id;
    }

    @Override
    public String toString() {
        return this.gameClientName;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
}
