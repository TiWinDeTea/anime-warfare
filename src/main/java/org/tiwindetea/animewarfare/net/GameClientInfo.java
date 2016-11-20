package org.tiwindetea.animewarfare.net;

/**
 * Created by Lucas on 20/11/2016.
 */
public class GameClientInfo {
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