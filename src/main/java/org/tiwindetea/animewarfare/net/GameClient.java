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

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.net.networkevent.ConnectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameStartedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.NetworkCommand;
import org.tiwindetea.animewarfare.net.networkevent.PlayOrderChosenNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerConnectionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerLockedFactionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerSelectedFactionNetevent;
import org.tiwindetea.animewarfare.net.networkrequests.NetFirstPlayerSelected;
import org.tiwindetea.animewarfare.net.networkrequests.NetGameEnded;
import org.tiwindetea.animewarfare.net.networkrequests.NetGameStarted;
import org.tiwindetea.animewarfare.net.networkrequests.NetLockFaction;
import org.tiwindetea.animewarfare.net.networkrequests.NetMessage;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.NetSelectFaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * The game client class
 * This class is used to connect to a game
 * server that hosts a game.
 * No logic involved in the client.
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GameClient {

    private final Client client = new Client();
    private Room room;
    private final GameClientInfo myName = new GameClientInfo();
    private boolean isConnected = false;
    private final EventDispatcher eventDispatcher = EventDispatcher.getInstance();
    private final Listener listener = new Listener();

    /**
     * Looks for available servers on local network
     *
     * @param UDPport UDP port on which servers are running
     * @param timeout The number of milliseconds to wait for a response
     * @return A list containing all servers found on LAN
     */
    public List<Room> discover(int UDPport, int timeout) {

        List<InetAddress> broadcastAddresses = Utils.findBroadcastAddr();
        List<Room> lanRooms = new ArrayList<>(3);
        List<InetSocketAddress> retrievedFrom = new ArrayList<>(3);

        try (DatagramSocket lookup = new DatagramSocket()) {
            byte[] header = Utils.VERSION_HEADER.getBytes(Utils.CHARSET);
            byte[] request = new byte[header.length + 1];
            System.arraycopy(header, 0, request, 0, header.length);
            request[header.length] = NetworkCommand.SCANNING.getValue();

            for (InetAddress address : broadcastAddresses) {
                DatagramPacket packet = new DatagramPacket(request, request.length, address, UDPport);
                Log.trace(GameClient.class.getName(), "Broadcasting to " + address);
                lookup.send(packet);
            }
            lookup.setSoTimeout(timeout);

            Log.trace(GameClient.class.getName(), "Listening for server answers");
            //noinspection InfiniteLoopStatement
            for (; ; ) {
                byte[] reception = new byte[8192];
                DatagramPacket rpacket = new DatagramPacket(reception, reception.length);
                lookup.receive(rpacket);
                Log.trace(GameClient.class.getName(), "Received data from " + rpacket.getAddress());
                reception = rpacket.getData();
                try (ByteArrayInputStream bais = new ByteArrayInputStream(reception)) {
                    try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                        String serverVersionHeader;
                        Room serverRoom;
                        serverVersionHeader = (String) ois.readObject();
                        serverRoom = (Room) ois.readObject();

                        if (Utils.VERSION_HEADER.equals(serverVersionHeader)) {
                            InetSocketAddress sender = new InetSocketAddress(rpacket.getAddress(), rpacket.getPort());
                            serverRoom.setAddress(rpacket.getAddress());
                            if (serverRoom != null && !retrievedFrom.contains(sender)) {
                                Log.trace(GameClient.class.getName(), "Adding " + serverRoom + " to rooms list");
                                lanRooms.add(serverRoom);
                                retrievedFrom.add(sender);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        Log.warn(GameClient.class.getName(), "Failed to read room from remote " + rpacket.getAddress(), e);
                    }
                }
            }
        } catch (SocketTimeoutException ignored) {
            Log.trace(GameClient.class.getName(), "Stopping servers lookup (timed out)");
        } catch (IOException e) {
            Log.warn(GameClient.class.getName(), "Unexpected IOException", e);
        }
        return lanRooms;
    }

    /**
     * Instanciate a new GameClient with to name
     */
    public GameClient() {
        this(null);
    }

    /**
     * Instanciate a new GameClient
     *
     * @param name client name
     */
    public GameClient(String name) {
        this.myName.gameClientName = name;
        Utils.registerClasses(this.client);
    }

    /**
     * Sets the name of the client
     *
     * @param name new name for the client
     * @throws IllegalStateException if the client is already connected
     */
    public void setName(String name) {
        if (this.isConnected) {
            throw new IllegalStateException();
        } else {
            this.myName.gameClientName = name;
        }
    }

    /**
     * Connects the client to a server, given its room
     *
     * @param room Room to connect to
     * @throws IOException if an I/O error occurs
     */
    public void connect(Room room) throws IOException {
        if (this.myName.gameClientName == null) {
            throw new IllegalStateException();
        }
        Log.debug(GameClient.class.toString(), "Connecting to " + room);
        this.client.addListener(this.listener);
        this.client.start();
        this.client.connect(500, room.getAddress(), room.getPort());
    }

    /**
     * @return Gets the room to which this client is connected
     */
    public Room getRoom() {
        return this.room;
    }

    /**
     * Disconnects from the server
     */
    public void disconnect() {
        if (this.isConnected) {
            Log.debug(GameClient.class.toString(), "Disconnecting");
            this.client.stop();
            this.client.removeListener(this.listener);
            this.isConnected = false;
        }
    }

    /**
     * Sends a message to the server, to be send to any other room member
     *
     * @param message message to be send
     */
    public void send(NetMessage message) {
        Log.trace(GameClient.class.toString(), "Sending " + message);
        this.client.sendTCP(message);
    }

    public void send(NetPlayingOrderChosen netPlayingOrderChosen) {
        Log.trace(GameClient.class.toString(), "Sending " + netPlayingOrderChosen);
        this.client.sendTCP(netPlayingOrderChosen);
    }

    public void send(NetLockFaction lockFaction) {
        Log.trace(GameClient.class.toString(), "Sending " + lockFaction);
        this.client.sendTCP(lockFaction);
    }

    public void send(NetSelectFaction selectFaction) {
        Log.trace(GameClient.class.toString(), "Sending " + selectFaction);
        this.client.sendTCP(selectFaction);
    }

    @SuppressWarnings("unused")
    public class Listener extends com.esotericsoftware.kryonet.Listener.ReflectionListener {

        // general
        public void received(Connection connection, GameClientInfo info) {
            Log.trace(GameClient.Listener.class.toString(), "Incoming GameClientInfo: " + info);
            if (info.gameClientName == null) {
                Log.debug(GameClient.Listener.class.toString(), "Finalizing connection to server");
                GameClient.this.myName.id = info.id;
                GameClient.this.client.sendTCP(GameClient.this.myName);
            } else {
                Log.debug(GameClient.Listener.class.toString(), "A new player connected: " + info);
                GameClient.this.room.addMember(info);
                GameClient.this.eventDispatcher.fire(new PlayerConnectionNetevent(info));
            }
        }

        public void received(Connection connection, Room room) {
            Log.debug(GameClient.Listener.class.toString(), "Connected.");
            GameClient.this.room = room;
            GameClient.this.eventDispatcher.fire(new ConnectedNetevent(room));
        }

        public void received(Connection connection, NetFirstPlayerSelected playerSelected) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + playerSelected);
            GameClient.this.eventDispatcher.fire(new FirstPlayerSelectedNetevent(playerSelected.getFirstPlayer()));
        }

        public void received(Connection connection, NetGameEnded gameEnded) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameEnded);
            GameClient.this.eventDispatcher.fire(new GameEndedNetevent(gameEnded));
        }

        // network requests
        public void received(Connection connection, NetGameStarted gameStarted) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameStarted);
            GameClient.this.eventDispatcher.fire(new GameStartedNetevent());
        }

        public void received(Connection connection, NetLockFaction faction) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + faction);
            GameClient.this.eventDispatcher.fire(new PlayerLockedFactionNetevent(faction));
        }

        public void received(Connection connection, NetMessage message) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + message);
            GameClient.this.eventDispatcher.fire(new MessageReceivedNetevent(message));
        }

        public void received(Connection connection, NetPlayingOrderChosen playingOrderChosen) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + playingOrderChosen);
            GameClient.this.eventDispatcher.fire(new PlayOrderChosenNetevent(playingOrderChosen));
        }

        public void received(Connection connection, NetSelectFaction faction) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + faction);
            GameClient.this.eventDispatcher.fire(new PlayerSelectedFactionNetevent(faction));
        }
    }
}
