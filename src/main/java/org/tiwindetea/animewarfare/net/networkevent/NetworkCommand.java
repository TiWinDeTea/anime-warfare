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

package org.tiwindetea.animewarfare.net.networkevent;

public enum NetworkCommand {
    //Not playing
    SCANNING(0x00),
    CHAT_MESSAGE(0x01),


    //Playing requests
    // Action phase
    UNIT_MOVE_R(0x20),
    UNIT_CAPTURE_R(0x21),
    UNIT_RECRUITMENT_R(0x22),
    START_FIGHT_R(0x23),
    CREATE_PORTAL_R(0x24),

    // Marketing phase
    ADVERTISE_R(0x25),

    // Global
    USE_SPECIAL_R(0x26),


    //Played
    // Action phase
    UNIT_MOVED(0x40),
    UNIT_CAPTURED(0x41),
    UNIT_RECRUITED(0x42),
    FIGHT_RESULT(0x43),
    PORTAL_CREATED(0x44),

    // Marketing phase
    ADVERTISED(0x45),

    // Global
    SPECIAL_USED(0x46);

    private final byte value;

    NetworkCommand(int value) {
        assert value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE;
        this.value = (byte) value;
    }

    public byte getValue() {
        return this.value;
    }
}
