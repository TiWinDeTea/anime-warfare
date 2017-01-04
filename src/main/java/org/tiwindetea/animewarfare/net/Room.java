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

import org.tiwindetea.animewarfare.logic.FactionType;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a game room with players.
 * It basically wraps the game name, its member,
 * the address and port of the server, and wether it
 * requires a password or not
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class Room implements Serializable, Comparable<Room> {

    private String gameName;

    private transient String gamePassword;

    private final ArrayList<GameClientInfo> members = new ArrayList<>();

    private boolean isLocked;

    private transient InetAddress address;

    private int port;

    private int numberOfExpectedPlayers = -1;

    private final Map<GameClientInfo, FactionType> selections = new HashMap<>(4, 1);
    private final Map<GameClientInfo, FactionType> locks = new HashMap<>(4, 1);

    private static int NEXT_ID_VAL = 0;
    private final int ROOM_ID = NEXT_ID_VAL++;

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
     * Finds players in the room
     *
     * @param players the ids of the players to look for
     * @return A list containing found players
     */
    public List<GameClientInfo> findAll(List<Integer> players) {
        LinkedList<GameClientInfo> ans = new LinkedList<>();
        GameClientInfo currentPlayer;
        for (Integer playerId : players) {
            currentPlayer = find(playerId.intValue());
            if (currentPlayer != null) {
                ans.add(currentPlayer);
            }
        }
        return ans;
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

    /**
     * @return An unmodifiable map of infos about players' selections,
     * as sepcified in {@link Collections#unmodifiableMap(Map)}
     */
    public Map<GameClientInfo, FactionType> getSelections() {
        return Collections.unmodifiableMap(this.selections);
    }

    /**
     * @return An unmodifiable map of infos about players' locked factions,
     * as sepcified in {@link Collections#unmodifiableMap(Map)}
     */
    public Map<GameClientInfo, FactionType> getLocks() {
        return Collections.unmodifiableMap(this.locks);
    }

    Map<GameClientInfo, FactionType> modifiableSelection() {
        return this.selections;
    }

    Map<GameClientInfo, FactionType> modifiableLocks() {
        return this.locks;
    }



    void addMember(GameClientInfo info) {
        if (this.isFull()) {
            throw new IllegalStateException("Trying to add a member into a full room");
        }
        this.members.add(info);
    }

    boolean checkPassword(String password) {
        return this.gamePassword.equals(password) || (password != null && password.equals(this.gamePassword));
    }

    void removeMember(GameClientInfo member) {
        this.removeMember(member.id);
    }

    GameClientInfo removeMember(int id) {

        int i = 0;
        Iterator<GameClientInfo> iterator = this.members.iterator();
        while (iterator.hasNext() && iterator.next().getId() != id) {
            ++i;
        }

        if (i < this.members.size()) {
            return this.members.remove(i);
        } else {
            return null;
        }
    }

    void clear() {
        this.members.clear();
        this.locks.clear();
        this.selections.clear();
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

    boolean isFull() {
        return this.numberOfExpectedPlayers == this.members.size();
    }

    String getGamePassword() {
        return this.gamePassword;
    }

    boolean updateable(Room r) {
        return this.ROOM_ID == r.ROOM_ID && !this.members.equals(r.members);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.gameName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Room) {
            return this.equals((Room) o);
        }
        return false;
    }

    public boolean equals(Room room) {
        return this.ROOM_ID == room.ROOM_ID
                && this.address.equals(room.address)
                && this.port == room.port;
    }

    @Override
    public int hashCode() {
        return this.address.hashCode() + this.port;
    }

    @Override
    public int compareTo(Room o) {
        return this.ROOM_ID - o.ROOM_ID;
    }

}
