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
import com.sun.istack.internal.Nullable;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.net.networkevent.BadPasswordNetevent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.net.networkevent.ConnectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FactionUnlockedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FactionUnselectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FanNumberUpdatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectionRequestNetvent;
import org.tiwindetea.animewarfare.net.networkevent.GameEndConditionsReachedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameStartedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.MarketingLadderUpdatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangeNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayOrderChosenNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerConnectionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerDisconnectionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerLockedFactionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerSelectedFactionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.SelectMascotToCaptureRequestNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StaffPointUpdatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioControllerChangedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitCreatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitDeletedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitMovedNetevent;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPassword;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSendable;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBadPassword;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBattle;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionLocked;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionSelected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionUnlocked;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionUnselected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFanNumberUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelectionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEndConditionsReached;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEnded;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameStarted;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetHandlePlayerDisconnection;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMarketingLadderUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMessage;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetPhaseChanged;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetSelectMascotToCapture;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStaffPointsUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStudio;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStudioControllerChanged;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetUnitCountChange;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetUnitMoveEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * The game client class
 * This class is used to connect to a game server that hosts a game.
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
     * Instanciate a new GameClient with to name
     * @see GameClient#GameClient(String)
     */
    public GameClient() {
        this(null);
    }

    /**
     * Instanciate a new GameClient, given its name
     *
     * @param name client name
     * @see GameClient#GameClient()
     */
    public GameClient(@Nullable String name) {
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
     * @param room Room to connectAt to
     * @throws IOException if an I/O error occurs
     * @throws IllegalStateException if the name of the client was not set
     * @throws IllegalArgumentException if the room is locked and no password was given, or if the room is not locked and a password was given.
     *
     * @see ServerScanner
     * @see GameClient#connect(GameServer)
     * @see GameClient#connect(Room, NetPassword)
     * @see GameClient#connectAt(InetSocketAddress)
     * @see GameClient#connectAt(InetSocketAddress, NetPassword)
     */
    public void connect(Room room) throws IOException {
        this.connect(room, null);
    }

    /**
     * Connects the client to a server, given its room and its password
     *
     * @param room     Room to connectAt to
     * @param password Password of the server, null if there is no password
     * @throws IOException if an I/O error occurs
     * @throws IllegalStateException if the name of the client was not set
     * @throws IllegalArgumentException if the room is locked and no password was given, or if the room is not locked and a password was given.
     *
     * @see ServerScanner
     * @see GameClient#connect(GameServer)
     * @see GameClient#connect(Room)
     * @see GameClient#connectAt(InetSocketAddress)
     * @see GameClient#connectAt(InetSocketAddress, NetPassword)
     */
    public void connect(Room room, @Nullable NetPassword password) throws IOException {
        if (room.isLocked()
                ? password == null || password.getPassword() == null
                : password != null && password.getPassword() != null) {
            throw new IllegalArgumentException("Invalid arguments correspondance: room.isLocked() = " + this.room.isLocked() + " ; password = " + password);
        }
        this.connectAt(new InetSocketAddress(room.getAddress(), room.getPort()), password);
    }

    /**
     * Connects to a local server
     *
     * @param server Server to connectAt to
     * @throws IOException              if an I/O error occurs
     * @throws IllegalStateException    if the name of the client was not set
     * @throws IllegalArgumentException if the room is locked and no password was given, or if the room is not locked and a password was given.
     *
     * @see ServerScanner
     * @see GameClient#connect(Room)
     * @see GameClient#connect(Room, NetPassword)
     * @see GameClient#connectAt(InetSocketAddress)
     * @see GameClient#connectAt(InetSocketAddress, NetPassword)
     */
    public void connect(GameServer server) throws IOException {
        Room room = server.getRoom();
        room.setAddress(InetAddress.getLocalHost());
        this.connect(room, new NetPassword(room.getGamePassword()));
    }

    /**
     * Connects to a local or remote server
     *
     * @param TCPAddress Address and port of the remote server
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if the name of the client was not set
     *
     * @see ServerScanner
     * @see GameClient#connectAt(InetSocketAddress, NetPassword)
     * @see GameClient#connect(GameServer)
     * @see GameClient#connect(Room, NetPassword)
     * @see GameClient#connect(Room)
     */
    public void connectAt(InetSocketAddress TCPAddress) throws IOException {
        connectAt(TCPAddress, null);
    }

    /**
     * Connects to a local or remote server
     *
     * @param TCPAddress Address and port of the remote server
     * @param password   Password of the server, null if there is no password
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if the name of the client was not set
     *
     * @see ServerScanner
     * @see GameClient#connectAt(InetSocketAddress)
     * @see GameClient#connect(GameServer)
     * @see GameClient#connect(Room, NetPassword)
     * @see GameClient#connect(Room)
     */
    public void connectAt(InetSocketAddress TCPAddress, @Nullable NetPassword password) throws IOException {
        if (this.me.gameClientName == null) {
            throw new IllegalStateException();
        }
        Log.debug(GameClient.class.toString(), "Connecting to " + this.room);
        this.client.addListener(this.listener);
        this.client.start();
        this.client.connect(500, TCPAddress.getAddress(), TCPAddress.getPort());

        if (password != null && password.getPassword() != null) {
            this.client.sendTCP(password);
        }
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
     * @see GameClient#connect(Room)
     */
    public void disconnect() {
        if (this.isConnected) {
            Log.debug(GameClient.class.toString(), "Disconnecting");
            this.client.stop();
            this.client.removeListener(this.listener);
        }
    }

    /**
     * Sends a message to the server, to be send to any other room member
     *
     * @param message message to be send
     * @see GameClient#send(NetSendable)
     */
    public void send(String message) {
        Log.trace(GameClient.class.toString(), "Sending message: " + message);
        this.client.sendTCP(message);
    }

    /**
     * Sends something to the server
     * @see GameClient#send(String)
     */
    public void send(NetSendable sendable) {
        Log.trace(GameClient.class.toString(), "Sending " + sendable);
        this.client.sendTCP(sendable);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameClientInfo) {
            return this.me.equals((GameClientInfo) o);
        } else if (o instanceof GameClient) {
            return this.me.equals(((GameClient) o).getClientInfo());
        }
        return false;
    }

    @SuppressWarnings("unused")
    public class Listener extends com.esotericsoftware.kryonet.Listener.ReflectionListener {

        //***************************************************************
        //*                                                             *
        //*         ALPHABETICAL ORDER ON SECOND ARGUMENT TYPE          *
        //*                                                             *
        //***************************************************************

        @Override
        public void connected(Connection connection) {
            GameClient.this.isConnected = true;
        }

        @Override
        public void disconnected(Connection connection) {
            GameClient.this.isConnected = false;
            EventDispatcher.send(new PlayerDisconnectionNetevent(GameClient.this.me));
        }

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
                EventDispatcher.send(new PlayerConnectionNetevent(info));
            }
        }

        public void received(Connection connection, Room room) {
            Log.debug(GameClient.Listener.class.toString(), "Connected.");
            room.addMember(GameClient.this.me);
            GameClient.this.room = room;
            EventDispatcher.send(new ConnectedNetevent(room));
        }

        // network requests, alphabetical order on second argument type
        public void received(Connection connection, NetBadPassword bpw) {
            EventDispatcher.send(new BadPasswordNetevent());
        }

        public void received(Connection connection, NetBattle battleStarted) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + battleStarted);
            EventDispatcher.send(new BattleNetevent(battleStarted));
        }

        public void received(Connection connection, NetFactionLocked faction) {
            GameClient.this.room.modifiableLocks().put(faction.getClient(), faction.getFaction());
            Log.debug(GameClient.Listener.class.toString(), "Received " + faction);
            EventDispatcher.send(new PlayerLockedFactionNetevent(faction));
        }

        public void received(Connection connection, NetFactionSelected faction) {
            GameClient.this.room.modifiableSelection().put(faction.getGameClientInfo(), faction.getFaction());
            EventDispatcher.send(new PlayerSelectedFactionNetevent(faction));
        }

        public void received(Connection connection, NetFactionUnlocked faction) {
            GameClient.this.room.modifiableLocks().remove(faction.getClient());
            EventDispatcher.send(new FactionUnlockedNetevent(faction.getClient(), faction.getFaction()));
        }

        public void received(Connection connection, NetFactionUnselected faction) {
            GameClient.this.room.modifiableSelection().remove(faction.getClient());
            EventDispatcher.send(new FactionUnselectedNetevent(faction.getClient(), faction.getFaction()));
        }

        public void received(Connection connection, NetFanNumberUpdated fanNumberUpdated) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + fanNumberUpdated);
            EventDispatcher.send(new FanNumberUpdatedNetevent(fanNumberUpdated));
        }

        public void received(Connection connection, NetFirstPlayerSelected playerSelected) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + playerSelected);
            EventDispatcher.send(new FirstPlayerSelectedNetevent(playerSelected.getFirstPlayer()));
        }

        public void received(Connection connection, NetFirstPlayerSelectionRequest firstPlayerSelectionRequest) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + firstPlayerSelectionRequest);
            EventDispatcher.send(new FirstPlayerSelectionRequestNetvent(firstPlayerSelectionRequest));
        }

        public void received(Connection connection, NetGameEndConditionsReached gameEndConditionsReached) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameEndConditionsReached);
            EventDispatcher.send(new GameEndConditionsReachedNetevent(gameEndConditionsReached));
        }

        public void received(Connection connection, NetGameEnded gameEnded) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameEnded);
            EventDispatcher.send(new GameEndedNetevent(gameEnded));
        }

        public void received(Connection connection, NetGameStarted gameStarted) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + gameStarted);
            EventDispatcher.send(new GameStartedNetevent());
        }

        public void received(Connection connection, NetHandlePlayerDisconnection playerDisconnection) {
            Log.debug(GameClient.Listener.class.toString(), "Received " + playerDisconnection);
            GameClient.this.room.removeMember(playerDisconnection.getPlayer());
            EventDispatcher.send(new PlayerDisconnectionNetevent(playerDisconnection));
        }

        public void received(Connection connection, NetMarketingLadderUpdated marketingLadderUpdated) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + marketingLadderUpdated);
            EventDispatcher.send(new MarketingLadderUpdatedNetevent(marketingLadderUpdated));
        }

        public void received(Connection connection, NetMessage message) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + message);
            EventDispatcher.send(new MessageReceivedNetevent(message));
        }

        public void received(Connection connection, NetPhaseChanged phaseChange) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + phaseChange);
            EventDispatcher.send(new PhaseChangeNetevent(phaseChange));
        }

        public void received(Connection connection, NetPlayingOrderChosen playingOrderChosen) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + playingOrderChosen);
            EventDispatcher.send(new PlayOrderChosenNetevent(playingOrderChosen));
        }

        public void received(Connection connection, NetStaffPointsUpdated staffPointsUpdated) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + staffPointsUpdated);
            EventDispatcher.send(new StaffPointUpdatedNetevent(staffPointsUpdated));
        }

        public void received(Connection connection, NetStudioControllerChanged studioControllerChanged) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + studioControllerChanged);
            EventDispatcher.send(new StudioControllerChangedNetevent(studioControllerChanged));
        }

        public void received(Connection connection, NetStudio newStudio) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + newStudio);
            EventDispatcher.send(new StudioNetevent(newStudio));
        }

        public void received(Connection connection, NetUnitCountChange unitCountChange) {
            if (unitCountChange.getType().equals(UnitCounterEvent.Type.ADDED)) {
                EventDispatcher.send(new UnitCreatedNetevent(unitCountChange));
            } else {
                EventDispatcher.send(new UnitDeletedNetevent(unitCountChange));
            }
        }

        public void received(Connection connection, NetSelectMascotToCapture selectMascotToCapture) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + selectMascotToCapture);
            EventDispatcher.send(new SelectMascotToCaptureRequestNetevent(selectMascotToCapture));
        }

        public void received(Connection connection, NetUnitMoveEvent unitEvent) {
            Log.trace(GameClient.Listener.class.toString(), "Received " + unitEvent);
            EventDispatcher.send(new UnitMovedNetevent(unitEvent));
        }

    }
}