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

package org.tiwindetea.net;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Room implements Serializable {

    private String gameName;

    private transient String gamePassword;

    private List<String> members;

    private boolean isLocked;

    Room() {
        this.gameName = null;
        this.members = new LinkedList<>();
        this.gamePassword = null;
        this.isLocked = false;
    }

    Room(String gameName) {
        this.gameName = gameName;
        this.members = new LinkedList<>();
        this.gamePassword = null;
        this.isLocked = false;
    }

    Room(String gameName, String gamePassword) {
        this.gameName = gameName;
        this.members = new LinkedList<>();
        this.gamePassword = gamePassword;
        this.isLocked = (gamePassword != null);
    }

    Room(String gameName, String gamePassword, List<String> members) {
        this.gameName = gameName;
        this.members = members;
        this.gamePassword = gamePassword;
        this.isLocked = (gamePassword != null);
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public String getGameName() {
        return this.gameName;
    }

    public List<String> getMembers() {
        return Collections.unmodifiableList(this.members);
    }

    public boolean checkPassword(String password) {
        return password == this.gamePassword || (password != null && password.equals(this.gamePassword));
    }

    void addMember(String memberName) {
        this.members.add(memberName);
    }

    void removeMember(String memberName) {
        this.members.remove(memberName);
    }

    void setGameName(String gameName) {
        this.gameName = gameName;
    }

    void setGamePassword(String gamePassword) {
        this.gamePassword = gamePassword;
        this.isLocked = gamePassword != null;
    }


    public String toString() {
        return this.gameName;
    }
}
