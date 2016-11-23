////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

package org.tiwindetea.animewarfare.net;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a game room with players.
 * It basically wraps the game name, its member,
 * the address and port of the server, and wether it
 * requires a password or not
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class Room implements Serializable {

    private String gameName;

    private transient String gamePassword;

    private ArrayList<GameClientInfo> members = new ArrayList<>();

    private boolean isLocked;

    private InetAddress address;

    private int port;

    private int numberOfExpectedPlayers = -1;

    Room() {
        this.gameName = null;
        this.gamePassword = null;
        this.isLocked = false;
    }

    Room(String gameName) {
        this.gameName = gameName;
        this.gamePassword = null;
        this.isLocked = false;
    }

    Room(String gameName, String gamePassword) {
        this.gameName = gameName;
        this.gamePassword = gamePassword;
        this.isLocked = (gamePassword != null);
    }

    Room(String gameName, String gamePassword, List<String> members) {
        this.gameName = gameName;
        this.gamePassword = gamePassword;
        this.isLocked = (gamePassword != null);
    }

    /**
     * @return true if the room has a password, false otherwise
     */
    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * @return the name of the game
     */
    public String getGameName() {
        return this.gameName;
    }

    /**
     * @return A unmodifiableList of the members of this room
     *
     * @see Collections#unmodifiableList(List)
     */
    public List<GameClientInfo> getMembers() {
        return Collections.unmodifiableList(this.members);
    }

    /**
     * Search for a player in the room
     *
     * @param id id of the player
     * @return The player, or null if there is no such player.
     */
    public GameClientInfo find(int id) {
        for (GameClientInfo member : this.members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    /**
     * @return the address of the server that has this room
     */
    public InetAddress getAddress() {
        return this.address;
    }

    /**
     * @return the port of the server
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @return The number of expected players for this game. (-1 if unset)
     */
    public int getNumberOfExpectedPlayers() {
        return this.numberOfExpectedPlayers;
    }

    void addMember(GameClientInfo info) {
        this.members.add(info);
    }

    boolean checkPassword(String password) {
        return password == this.gamePassword || (password != null && password.equals(this.gamePassword));
    }

    void removeMember(int id) {

        int i = 0;
        Iterator<GameClientInfo> iterator = this.members.iterator();
        while (iterator.hasNext() && iterator.next().getId() != id) {
            ++i;
        }

        if (i < this.members.size()) {
            this.members.remove(i);
        }
    }

    void clear() {
        this.members.clear();
    }

    void setGameName(String gameName) {
        this.gameName = gameName;
    }

    void setGamePassword(String gamePassword) {
        this.gamePassword = gamePassword;
        this.isLocked = gamePassword != null;
    }

    void setAddress(InetAddress address) {
        this.address = address;
    }

    void setPort(int port) {
        this.port = port;
    }

    void setNumberOfExpectedPlayers(int numberOfExpectedPlayers) {
        this.numberOfExpectedPlayers = numberOfExpectedPlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.gameName;
    }
}
