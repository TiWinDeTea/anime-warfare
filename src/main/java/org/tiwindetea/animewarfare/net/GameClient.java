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
import org.tiwindetea.animewarfare.net.networkevent.*;
import org.tiwindetea.animewarfare.net.networkrequests.NetLockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.NetSelectFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.NetUnitEvent;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSendable;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBattleStarted;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFanNumberUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelectionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEndConditionsReached;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEnded;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameStarted;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetHandlePlayerDisconnection;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMarketingLadderUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMessage;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetNewStudio;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetPhaseChange;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetSelectMascotToCapture;

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
    private final GameClientInfo me = new GameClientInfo();
    private boolean isConnected = false;
    private final Listener listener = new Listener();

    /**
     * Looks for available servers on local network
     *
     * @param UDPport UDP port on which servers are running
     * @param timeout The number of milliseconds to wait for a response
     * @return A list containing all servers found on LAN
     */
    public static List<Room> discover(int UDPport, int timeout) {

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
        this.me.gameClientName = name;
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
            this.me.gameClientName = name;
        }
    }

    /**
     * Connects the client to a server, given its room
     *
     * @param room Room to connect to
     * @throws IOException if an I/O error occurs
     */
    public void connect(Room room) throws IOException {
        if (this.me.gameClientName == null) {
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
     * @return GameClientInfo representing this connection
     */
    public GameClientInfo getClientInfo() {
        return this.me;
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
    public void send(String message) {
        Log.trace(GameClient.class.toString(), "Sending message: " + message);
        this.client.sendTCP(message);
    }

    /**
     * Sends something to the server
     */
    public void send(NetSendable sendable) {
        Log.trace(GameClient.class.toString(), "Sending " + sendable);
        this.client.sendTCP(sendable);
    }

    @SuppressWarnings("unused")
    public class Listener extends com.esotericsoftware.kryonet.Listener.ReflectionListener {

        private final EventDispatcher eventDispatcher = EventDispatcher.getInstance();
        
        // general
        public void received(Connection connection, GameClientInfo info) {
            Log.trace(GameClient.Listener.class.toString(), "Incoming GameClientInfo: " + info);
            if (info.gameClientName == null) {
                Log.debug(GameClient.Listener.class.toString(), "Finalizing connection to server");
                GameClient.this.me.id = info.id;
                GameClient.this.client.sendTCP(GameClient.this.me);
            } else {
                Log.debug(GameClient.Listener.class.toString(), "A new player connected: " + info);
                GameClient.this.room.addMember(info);
                this.eventDispatcher.fire(new PlayerConnectionNetevent(info));
            }
        }

        public void received(Connection connection, Room room) {
            Log.debug(GameClient.Listener.class.toString(), "Connected.");
            room.addMember(GameClient.this.me);
            GameClient.this.room = room;
            this.eventDispatcher.fire(new ConnectedNetevent(room));
        }

        // network requests
        public void received(Connection connection, NetBattleStarted battleStarted) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + battleStarted);
            this.eventDispatcher.fire(new BattleStartedNetevent(battleStarted));
        }

        public void received(Connection connection, NetFanNumberUpdated fanNumberUpdated) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + fanNumberUpdated);
            this.eventDispatcher.fire(new FanNumberUpdatedNetevent(fanNumberUpdated));
        }

        public void received(Connection connection, NetFirstPlayerSelected playerSelected) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + playerSelected);
            this.eventDispatcher.fire(new FirstPlayerSelectedNetevent(playerSelected.getFirstPlayer()));
        }

        public void received(Connection connection, NetFirstPlayerSelectionRequest firstPlayerSelectionRequest) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + firstPlayerSelectionRequest);
            this.eventDispatcher.fire(new FirstPlayerSelectionRequestNetvent(firstPlayerSelectionRequest));
        }

        public void received(Connection connection, NetGameEndConditionsReached gameEndConditionsReached) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameEndConditionsReached);
            this.eventDispatcher.fire(new GameEndConditionsReachedNetevent(gameEndConditionsReached));
        }

        public void received(Connection connection, NetGameEnded gameEnded) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameEnded);
            this.eventDispatcher.fire(new GameEndedNetevent(gameEnded));
        }

        public void received(Connection connection, NetGameStarted gameStarted) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameStarted);
            this.eventDispatcher.fire(new GameStartedNetevent());
        }

        public void received(Connection connection, NetHandlePlayerDisconnection playerDisconnection) {
            Log.debug(GameClient.Listener.class.toString(), "Received " + playerDisconnection);
            this.eventDispatcher.fire(new PlayerDisconnectionNetevent(playerDisconnection));
        }

        public void received(Connection connection, NetLockFactionRequest faction) {
            Log.debug(GameClient.Listener.class.toString(), "Received " + faction);
            this.eventDispatcher.fire(new PlayerLockedFactionNetevent(faction));
        }

        public void received(Connection connection, NetMarketingLadderUpdated marketingLadderUpdated) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + marketingLadderUpdated);
            this.eventDispatcher.fire(new MarketingLadderUpdatedNetevent(marketingLadderUpdated));
        }

        public void received(Connection connection, NetMessage message) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + message);
            this.eventDispatcher.fire(new MessageReceivedNetevent(message));
        }

        public void received(Connection connection, NetNewStudio newStudio) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + newStudio);
            this.eventDispatcher.fire(new StudioCreatedNetevent(newStudio));
        }

        public void received(Connection connection, NetPhaseChange phaseChange) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + phaseChange);
            this.eventDispatcher.fire(new PhaseChangeNetevent(phaseChange));
        }

        public void received(Connection connection, NetPlayingOrderChosen playingOrderChosen) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + playingOrderChosen);
            this.eventDispatcher.fire(new PlayOrderChosenNetevent(playingOrderChosen));
        }

        public void received(Connection connection, NetSelectFactionRequest faction) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + faction);
            this.eventDispatcher.fire(new PlayerSelectedFactionNetevent(faction));
        }

        public void received(Connection connection, NetSelectMascotToCapture selectMascotToCapture) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + selectMascotToCapture);
            this.eventDispatcher.fire(new SelectMascotToCaptureRequestNetevent(selectMascotToCapture));
        }

        public void received(Connection connection, NetUnitEvent unitEvent) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + unitEvent);
            this.eventDispatcher.fire(new UnitNetevent(unitEvent));
        }

        @Override
        public void disconnected(Connection connection) {
            this.eventDispatcher.fire(new PlayerDisconnectionNetevent(GameClient.this.me));
        }
    }
}