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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.net.networkrequests.NetFirstPlayerSelected;
import org.tiwindetea.animewarfare.net.networkrequests.NetFirstPlayerSelectionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.NetGameEnded;
import org.tiwindetea.animewarfare.net.networkrequests.NetHandlePlayerDisconnection;
import org.tiwindetea.animewarfare.net.networkrequests.NetLockFaction;
import org.tiwindetea.animewarfare.net.networkrequests.NetMessage;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.NetSelectFaction;
import org.tiwindetea.animewarfare.net.networkrequests.playingcommands.NetAbstractPlayingCommand;
import org.tiwindetea.animewarfare.net.networkrequests.playingcommands.NetPlayingCommand;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class regroups some methods used in the net package
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
class Utils {

    static final String VERSION_HEADER = "Anime-Wafare v0.1.0";
    static final Charset CHARSET = Charset.defaultCharset();

    static List<InetAddress> findBroadcastAddr() {

        LinkedList<InetAddress> broadcastAddresses = new LinkedList<>();
        Enumeration<NetworkInterface> en = Collections.emptyEnumeration();

        try {
            en = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            Log.warn(Utils.class.getName(), "Failed to find broacast addresses", e);
        }

        while (en.hasMoreElements()) {
            List<InterfaceAddress> list = en.nextElement().getInterfaceAddresses();
            broadcastAddresses.addAll(list.stream().filter(ia -> ia.getBroadcast() != null).map(InterfaceAddress::getBroadcast).collect(Collectors.toList()));
        }
        return broadcastAddresses;
    }

    /**
     * Register classes sent by network.
     */
    static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        // general
        kryo.register(GameClientInfo.class);
        kryo.register(Room.class);
        kryo.register(String.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);

        // network requests
        kryo.register(NetAbstractPlayingCommand.class);
        kryo.register(NetFirstPlayerSelected.class);
        kryo.register(NetFirstPlayerSelectionRequest.class);
        kryo.register(NetGameEnded.class);
        kryo.register(NetHandlePlayerDisconnection.class);
        kryo.register(NetLockFaction.class);
        kryo.register(NetMessage.class);
        kryo.register(NetPlayingCommand.class);
        kryo.register(NetPlayingOrderChosen.class);
        kryo.register(NetSelectFaction.class);
    }

    public static void registerAsLogicListener(GameServer.LogicListener logicListener) {

        EventDispatcher ed = EventDispatcher.getInstance();

        ed.addListener(AskFirstPlayerEvent.class, logicListener);
        ed.addListener(FirstPlayerSelectedEvent.class, logicListener);
        ed.addListener(GameEndedEvent.class, logicListener);
    }

    public static void deregisterLogicListener(GameServer.LogicListener logicListener) {

        EventDispatcher ed = EventDispatcher.getInstance();

        ed.removeListener(AskFirstPlayerEvent.class, logicListener);
        ed.removeListener(FirstPlayerSelectedEvent.class, logicListener);
        ed.removeListener(GameEndedEvent.class, logicListener);
    }
}
