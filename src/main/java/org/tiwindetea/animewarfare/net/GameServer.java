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

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.sun.istack.internal.Nullable;
import org.lomadriel.lfc.event.EventDispatcher;
import org.lomadriel.lfc.statemachine.DefaultStateMachine;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.events.GameEndConditionsReachedEvent;
import org.tiwindetea.animewarfare.logic.events.GameEndConditionsReachedEventListener;
import org.tiwindetea.animewarfare.logic.events.MarketingLadderUpdatedEvent;
import org.tiwindetea.animewarfare.logic.events.MarketingLadderUpdatedEventListener;
import org.tiwindetea.animewarfare.logic.events.NumberOfFansChangedEvent;
import org.tiwindetea.animewarfare.logic.events.NumberOfFansChangedEventListener;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEventListener;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.states.FirstTurnStaffHiringState;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEventListener;
import org.tiwindetea.animewarfare.logic.states.events.AskMascotToCaptureEvent;
import org.tiwindetea.animewarfare.logic.states.events.AskUnitToCaptureEventListener;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEventListener;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEventListener;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEventListener;
import org.tiwindetea.animewarfare.net.logicevent.CaptureMascotEvent;
import org.tiwindetea.animewarfare.net.logicevent.FirstPlayerChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.InvokeUnitEvent;
import org.tiwindetea.animewarfare.net.logicevent.MascotToCaptureChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitEvent;
import org.tiwindetea.animewarfare.net.logicevent.OpenStudioEvent;
import org.tiwindetea.animewarfare.net.logicevent.OrganizeConventionRequestEvent;
import org.tiwindetea.animewarfare.net.logicevent.PlayingOrderChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.SkipTurnEvent;
import org.tiwindetea.animewarfare.net.logicevent.StartBattleEvent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.NetUnitEvent;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetCapturedMascotSelection;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetConventionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetFirstPlayerSelection;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetInvokeUnitRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetLockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetMascotCaptureRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetMoveUnitRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetOpenStudioRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPassword;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSkipTurnRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetStartBattleRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUnlockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUnselectFactionRequest;
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
import org.tiwindetea.animewarfare.net.networkrequests.server.NetPhaseChange;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetSelectMascotToCapture;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStudio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * The game server class
 * This class is used as a host for the logic game
 * and re dispatches what happened to its clients
 *
 * @author Lucas Lazare
 * @see GameClient
 * @since 0.1.0
 */
public class GameServer {

    public static final int MAXIMUM_PLAYER_NBR = 4;
    public static final int MINIMUM_PLAYER_NBR = 2;

    private final GameClientInfo SERVER_ID = new GameClientInfo(-1);

    private DefaultStateMachine stateMachine;
    private final Server server = new Server();

    private final EventDispatcher eventDispatcher = LogicEventDispatcher.getInstance();

    private final Room room = new Room();
    private final NetworkListener networkNetworkListener = new NetworkListener(this.server, this.room);
    private final LogicListener logicListener = new LogicListener(this.server);
    private final UDPListener udpListener = new UDPListener();
    private boolean isRunning = false;
    private int TCPPort = -1;

    private final TreeSet<Integer> legitConnections = new TreeSet<>(Integer::compareTo);

    /**
     * Instanciate a server with a random name and without password
     * @see GameServer#setGameName(String)
     * @see GameServer#start()
     */
    public GameServer() {
        this(-1, null, null);
    }

    public GameServer(int numberOfExpectedPlayers) {
        this(numberOfExpectedPlayers, null, null);
    }

    /**
     * Instanciate a server without password, given its name
     *
     * @param gameName Name of the game Room
     * @see GameServer#start()
     */
    public GameServer(@Nullable String gameName) {
        this(-1, gameName, null);
    }

    /**
     * Instanciate a server given its name and password
     *
     * @param gameName     Name of the game Room
     * @param gamePassword Password of the game
     * @see GameServer#start()
     */
    public GameServer(@Nullable String gameName, @Nullable String gamePassword) {
        this(-1, gameName, gamePassword);
    }

    /**
     * Instanciate a server given its name and password
     *
     * @param numberOfExpectedPlayers Number of players required before starting the game
     * @param gameName                Name of the Room
     * @param gamePassword            Password of the Room
     * @see GameServer#start()
     */
    public GameServer(int numberOfExpectedPlayers, @Nullable String gameName, @Nullable String gamePassword) {
        if (gameName == null) {
            gameName = generateName();
            Log.trace(GameServer.class.toString(), "Server name randomly created: " + gameName);
        }
        this.room.setGameName(gameName);
        this.SERVER_ID.gameClientName = gameName;
        this.room.setGamePassword(gamePassword);
        this.udpListener.setRoom(this.room);
        this.room.setNumberOfExpectedPlayers(numberOfExpectedPlayers);
        Utils.registerClasses(this.server);
    }

    /**
     * Sets the game password
     *
     * @param gamePassword new password for the game
     * @throws IllegalStateException if the server is already running
     * @see GameServer#setNumberOfExpectedPlayer(int)
     * @see GameServer#setGameName(String)
     */
    public void setGamePassword(String gamePassword) {
        if (this.isRunning) {
            throw new IllegalStateException("Server already running");
        } else {
            this.room.setGamePassword(gamePassword);
        }
    }

    /**
     * Sets the game name
     *
     * @param gameName new name for the game
     * @throws IllegalStateException if the server is already running
     * @see GameServer#setNumberOfExpectedPlayer(int)
     * @see GameServer#setGamePassword(String)
     */
    public void setGameName(String gameName) {
        if (this.isRunning) {
            throw new IllegalStateException("Server already running");
        } else {
            this.room.setGameName(gameName);
            this.SERVER_ID.gameClientName = gameName;
        }
    }

    /**
     * Sets the number of players for the game
     *
     * @param numberOfExpectedPlayer the number of players
     * @throws IllegalStateException    If the server is already running.
     * @throws IllegalArgumentException If the passed argument is outside of the [2-4] range
     * @see GameServer#setGamePassword(String)
     * @see GameServer#setGameName(String)
     */
    public void setNumberOfExpectedPlayer(int numberOfExpectedPlayer) {
        if (this.isRunning) {
            throw new IllegalStateException("Server already running");
        } else if (numberOfExpectedPlayer < MINIMUM_PLAYER_NBR || numberOfExpectedPlayer > MAXIMUM_PLAYER_NBR) {
            throw new IllegalArgumentException("Passed argument outside of the [2-4] range. (got " + numberOfExpectedPlayer + ")");
        } else {
            this.room.setNumberOfExpectedPlayers(numberOfExpectedPlayer);
        }
    }

    /**
     * Binds the server with a given TCP port for client's
     * connections and UDP port for client's broacast.
     *
     * @param TCPport Port to use for clients connection on TCP
     * @param UDPport Port to use for clients broadcasting when discovering
     * @throws IOException If the server could not be opened
     * @see GameServer#start()
     */

    public void bind(int TCPport, int UDPport) throws IOException {
        Log.trace(GameServer.class.toString(), "Binding server to " + TCPport + " - " + UDPport);
        if (TCPport != this.TCPPort) {
            this.room.setPort(TCPport);
            this.TCPPort = TCPport;
            this.server.bind(TCPport);
        }
        this.udpListener.bind(UDPport);
    }

    /**
     * Starts the server if it is not started yet, listening for clients on UDP.
     *
     * @throws IllegalStateException if the number of player was not set
     * @throws org.tiwindetea.animewarfare.net.ServerScanner.AlreadyRunningException if this server is already started
     * @see GameServer#stop()
     * @see GameServer#isRunning()
     * @see GameServer#hide()
     * @see GameServer#startHidden()
     */
    public void start() {
        if (this.room.getNumberOfExpectedPlayers() > 1) {
            if (!this.isRunning) {
                this.isRunning = true;
                this.server.start();
                this.udpListener.start();
                this.server.addListener(this.networkNetworkListener);
                Utils.registerAsLogicListener(this.logicListener);
                Log.debug(GameServer.class.toString(), "Running");
            } else {
                throw new ServerScanner.AlreadyRunningException();
            }
        } else {
            throw new IllegalStateException("Number of expected players was not set");
        }
    }

    /**
     * Starts the server if it is not started yet, without listening on UDP.
     *
     * @throws IllegalStateException if the number of player was not set
     * @see GameServer#stop()
     * @see GameServer#isRunning()
     * @see GameServer#show()
     * @see GameServer#start()
     */
    private void startHidden() {
        if (this.room.getNumberOfExpectedPlayers() > 1) {
            if (!this.isRunning) {
                this.isRunning = true;
                this.server.start();
                this.server.addListener(this.networkNetworkListener);
                Utils.registerAsLogicListener(this.logicListener);
                Log.debug(GameServer.class.toString(), "Running");
            }
        } else {
            throw new IllegalStateException("Number of expected players was not set");
        }
    }

    /**
     * Closes the TCP server together with the
     * UDP server.
     *
     * @see GameServer#isRunning()
     * @see GameServer#start()
     */
    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            if (this.udpListener.isRunning()) {
                this.udpListener.stop();
            }
            this.server.stop();
            try {
                this.server.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.server.removeListener(this.networkNetworkListener);
            this.room.clear();
            Utils.deregisterLogicListener(this.logicListener);
        }
    }

    /**
     * returns true if the server is running
     * @see GameServer#start()
     * @see GameServer#stop()
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * @return true if the server is visible for udp broadcasters
     *
     * @see GameServer#hide()
     * @see GameServer#show()
     * @see GameServer#isRunning()
     */
    public boolean isVisible() {
        return this.udpListener.isRunning();
    }

    /**
     * @return The UDPPort to which this server is binded
     * @see GameServer#bind(int, int)
     * @see GameServer#getTCPPort()
     */
    public int getUDPPort() {
        return this.udpListener.getPort();
    }

    /**
     * @return The TCPPort to which this server is binded
     * @see GameServer#bind(int, int)
     * @see GameServer#getUDPPort()
     */
    public int getTCPPort() {
        return this.TCPPort;
    }

    /**
     * @return The room of this server
     * @see GameServer#setGameName(String)
     * @see GameServer#setGamePassword(String)
     */
    public Room getRoom() {
        return this.room;
    }

    /**
     * Stops the udp listening. Other clients won't be able to find it via UDP.
     *
     * @see GameServer#isVisible()
     * @see GameServer#show()
     */
    public void hide() {
        this.udpListener.stop();
    }

    /**
     * Starts the udp listening. (automatically done
     * when starting the server through {@link GameServer#start()}
     *
     * @throws IllegalStateException if the game server is not running
     */
    public void show() {
        if (this.isRunning) {
            if (!this.udpListener.isRunning()) {
                this.udpListener.start();
            }
        } else {
            throw new IllegalStateException("Tried to make visible an unstarted server");
        }
    }

    private void initNew() {
        this.udpListener.stop();
        Map<Integer, FactionType> shadow = new TreeMap<>();
        this.room.modifiableLocks().forEach((gc, faction) -> shadow.put(new Integer(gc.id), faction));
        this.server.sendToAllTCP(new NetGameStarted());
        this.stateMachine = new DefaultStateMachine(new FirstTurnStaffHiringState(shadow));
        Log.info(GameServer.class.toString(), this.room + ": game started");
    }

    @SuppressWarnings("unused")
    public class NetworkListener extends Listener.ReflectionListener {

        private final Server server;
        private final List<Integer> incomingConnections = new ArrayList<>();
        private final Room room;

        NetworkListener(Server server, Room room) {
            this.server = server;
            this.room = room;
        }

        private boolean isLegit(Connection connection) {
            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {
                return true;
            } else {
                connection.close();
                return false;
            }
        }

        @Override
        public void connected(Connection connection) {
            if (this.room.isFull()) {
                Log.debug(NetworkListener.class.toString(),
                        "Refused incoming connection: " + connection.getID());
                connection.close();
            } else {
                Log.debug(NetworkListener.class.toString(), "Incoming connection: " + connection.getID());
                if (this.room.isLocked()) {
                    this.incomingConnections.add(new Integer(connection.getID()));
                } else {
                    GameServer.this.legitConnections.add(new Integer(connection.getID()));
                    connection.sendTCP(new GameClientInfo(connection.getID()));
                    connection.sendTCP(GameServer.this.room);
                }
            }
        }

        @Override
        public void disconnected(Connection connection) {
            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {
                GameClientInfo info = this.room.removeMember(connection.getID());
                GameServer.this.legitConnections.remove(new Integer(connection.getID()));
                Log.debug(GameServer.NetworkListener.class.toString(), "Player " + info + " disconnected");

                FactionType faction = this.room.modifiableLocks().remove(new GameClientInfo(connection.getID()));
                if (faction != null) {
                    this.server.sendToAllTCP(new NetFactionUnlocked(info, faction));
                }

                faction = this.room.modifiableSelection().remove(new GameClientInfo(connection.getID()));
                if (faction != null) {
                    this.server.sendToAllTCP(new NetFactionUnselected(info, faction));
                }

                this.server.sendToAllTCP(new NetHandlePlayerDisconnection(info));
            }
        }


        //***************************************************************
        //*                                                             *
        //*         ALPHABETICAL ORDER ON SECOND ARGUMENT TYPE          *
        //*                                                             *
        //***************************************************************

        // general
        public void received(Connection connection, GameClientInfo info) {
            if (isLegit(connection)) {
                this.server.sendToAllExceptTCP(connection.getID(), info);
                this.room.addMember(info);
                Log.debug(GameServer.NetworkListener.class.toString(), "Player " + info + " connected");
            }
        }

        public void received(Connection connection, String message) {
            if (isLegit(connection)) {
                this.server.sendToAllExceptTCP(connection.getID(),
                        new NetMessage(message,
                                this.room.find(connection.getID())));
                Log.trace(GameServer.NetworkListener.class.toString(), "Received message: " + message);
            }
        }

        public void received(Connection connection, NetPlayingOrderChosen netPlayingOrderChosen) {

            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new PlayingOrderChoiceEvent(netPlayingOrderChosen.isClockwise()));
                this.server.sendToAllTCP(netPlayingOrderChosen);
                Log.trace(GameServer.NetworkListener.class.toString(),
                        "Play order was choosen (" + netPlayingOrderChosen);
            }
        }

        public void received(Connection connection, NetUnitEvent unitEvent) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(
                        new UnitCounterEvent(
                                unitEvent.getType(),
                                unitEvent.getFactionType(),
                                unitEvent.getUnitType()
                        ));
            }
        }

        // NetSendable classes, alphabetical order on second argument type
        public void received(Connection connection, NetCapturedMascotSelection selection) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new MascotToCaptureChoiceEvent(connection.getID(), selection.getUnitID()));
            }
        }

        public void received(Connection connection, NetConventionRequest ignored) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new OrganizeConventionRequestEvent(connection.getID()));
            }
        }

        public void received(Connection connection, NetFirstPlayerSelection player) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new FirstPlayerChoiceEvent(player.getPlayerID()));
            }
        }

        public void received(Connection connection, NetInvokeUnitRequest unitRequest) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new InvokeUnitEvent(connection.getID(), unitRequest.getUnitType(), unitRequest.getZoneID()));
            }
        }

        public void received(Connection connection, NetLockFactionRequest faction) {

            if (isLegit(connection) && !this.room.modifiableLocks().containsKey(new GameClientInfo(connection.getID()))) {

                Log.trace(GameServer.NetworkListener.class.toString(), "Faction locking requested: " + faction);

                if (!this.room.modifiableLocks().containsValue(faction.getFaction())) {

                    ArrayList<GameClientInfo> clients = new ArrayList<>(4);

                    this.room.modifiableSelection().forEach((gc, fac) -> {
                        if (fac.equals(faction.getFaction()) && !gc.equals(connection.getID())) {
                            clients.add(gc);
                            this.server.sendToAllTCP(new NetFactionUnselected(gc, fac));
                        }
                    });

                    for (GameClientInfo client : clients) {
                        this.room.modifiableSelection().remove(client);
                    }

                    this.server.sendToAllTCP(new NetFactionLocked(this.room.find(connection.getID()), faction.getFaction()));
                    this.room.modifiableLocks().put(this.room.find(connection.getID()), faction.getFaction());
                    Log.debug(GameServer.NetworkListener.class.toString(),
                            "Player " + this.room.find(connection.getID()) + " locked " + faction);

                    if (this.room.modifiableLocks().size() == this.room.getNumberOfExpectedPlayers()) {
                        initNew();
                    }
                }
            }
        }

        public void received(Connection connection, NetMascotCaptureRequest mascotRequest) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new CaptureMascotEvent(
                        connection.getID(),
                        mascotRequest.getTargetPlayer(),
                        mascotRequest.getZoneID()
                ));
            }
        }

        public void received(Connection connection, NetMoveUnitRequest unitMoveRequest) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new MoveUnitEvent(connection.getID(), unitMoveRequest.getMovements()));
            }
        }

        public void received(Connection connection, NetOpenStudioRequest studioRequest) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new OpenStudioEvent(connection.getID(), studioRequest.getZoneID()));
            }
        }

        public void received(Connection connection, NetPassword password) {
            if (this.incomingConnections.contains(new Integer(connection.getID()))) {
                this.incomingConnections.remove(new Integer(connection.getID()));
                if (password != null && this.room.checkPassword(password.getPassword())) {
                    GameServer.this.legitConnections.add(new Integer(connection.getID()));
                    connection.sendTCP(new GameClientInfo(connection.getID()));
                    connection.sendTCP(GameServer.this.room);
                } else {
                    connection.sendTCP(new NetBadPassword());
                    connection.close();
                }
            }
        }

        public void received(Connection connection, NetSelectFactionRequest faction) {

            if (isLegit(connection)
                    && !this.room.modifiableLocks().containsKey(new GameClientInfo(connection.getID()))
                    && !this.room.modifiableLocks().containsValue(faction.getFactionType())) {
                FactionType previousFaction = this.room.modifiableSelection().remove(new GameClientInfo(connection.getID()));
                this.room.modifiableSelection().put(this.room.find(connection.getID()), faction.getFactionType());

                if (previousFaction != null) {
                    this.server.sendToAllTCP(new NetFactionUnselected(this.room.find(connection.getID()), previousFaction));
                }
                this.server.sendToAllTCP(new NetFactionSelected(faction.getFactionType(), this.room.find(connection.getID())));
                Log.trace(GameServer.NetworkListener.class.toString(),
                        this.room.find(connection.getID()) + " selected " + faction);
            }
        }

        public void received(Connection connection, NetSkipTurnRequest ignored) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new SkipTurnEvent(connection.getID()));
            }
        }

        public void received(Connection connection, NetStartBattleRequest battleRequest) {
            if (isLegit(connection)) {
                GameServer.this.eventDispatcher.fire(new StartBattleEvent(
                        connection.getID(),
                        battleRequest.getTargetPlayerID(),
                        battleRequest.getZoneID()
                ));
            }
        }

        public void received(Connection connection, NetUnlockFactionRequest ignored) {
            FactionType faction;
            if (isLegit(connection) && (faction = this.room.modifiableLocks().remove(new GameClientInfo(connection.getID()))) != null) {
                this.server.sendToAllTCP(new NetFactionUnlocked(this.room.find(connection.getID()), faction));
            }
        }

        public void received(Connection connection, NetUnselectFactionRequest ignored) {
            if (isLegit(connection)) {
                FactionType faction = this.room.modifiableSelection().get(new GameClientInfo(connection.getID()));
                if (faction != null && !faction.equals(this.room.modifiableLocks().get(faction))) {
                    this.server.sendToAllTCP(new NetFactionUnselected(this.room.find(connection.getID()), faction));
                }
            }
        }

        // don't forget to check that the client is a legit client (ie : is in the player that sent the good password)
    }

    public class LogicListener implements
            AskFirstPlayerEventListener,
            AskUnitToCaptureEventListener,
            BattleEventListener,
            FirstPlayerSelectedEventListener,
            GameEndedEventListener,
            PhaseChangedEventListener,

            GameEndConditionsReachedEventListener,
            MarketingLadderUpdatedEventListener,
            NumberOfFansChangedEventListener,
            StudioEventListener,
            UnitCounterEventListener {

        private final Server server;

        public LogicListener(Server server) {
            this.server = server;
        }

        //***************************************************************
        //*                                                             *
        //*            ALPHABETICAL ORDER ON ARGUMENT TYPE              *
        //*                                                             *
        //***************************************************************

        // logic.states.events, alphabetical order on argument type
        @Override
        public void askFirstPlayerEvent(AskFirstPlayerEvent event) {
            this.server.sendToAllTCP(new NetFirstPlayerSelectionRequest(GameServer.this.room.find(event.getLastPlayer()),
                    GameServer.this.room.findAll(event.getDrawPlayers())));
        }

        @Override
        public void askUnitToCaptureEvent(AskMascotToCaptureEvent event) {
            this.server.sendToTCP(event.getPlayer(), new NetSelectMascotToCapture(event));
        }

        @Override
        public void handlePreBattle(BattleEvent event) {
            this.server.sendToAllTCP(new NetBattle(
                    event,
                    BattleNetevent.Type.PRE_BATTLE,
                    GameServer.this.room.find(event.getBattleContext().getAttacker().getPlayer().getID()),
                    GameServer.this.room.find(event.getBattleContext().getDefender().getPlayer().getID())
            ));
        }

        @Override
        public void handleDuringBattle(BattleEvent event) {
            this.server.sendToAllTCP(new NetBattle(
                    event,
                    BattleNetevent.Type.DURING_BATTLE,
                    GameServer.this.room.find(event.getBattleContext().getAttacker().getPlayer().getID()),
                    GameServer.this.room.find(event.getBattleContext().getDefender().getPlayer().getID())
            ));
        }

        @Override
        public void handleBattleFinished(BattleEvent event) {
            this.server.sendToAllTCP(new NetBattle(
                    event,
                    BattleNetevent.Type.BATTLE_FINISHED,
                    GameServer.this.room.find(event.getBattleContext().getAttacker().getPlayer().getID()),
                    GameServer.this.room.find(event.getBattleContext().getDefender().getPlayer().getID())
            ));
        }

        @Override
        public void handlePostBattle(BattleEvent event) {
            this.server.sendToAllTCP(new NetBattle(
                    event,
                    BattleNetevent.Type.POST_BATTLE,
                    GameServer.this.room.find(event.getBattleContext().getAttacker().getPlayer().getID()),
                    GameServer.this.room.find(event.getBattleContext().getDefender().getPlayer().getID())
            ));
        }

        @Override
        public void firstPlayerSelected(FirstPlayerSelectedEvent event) {
            this.server.sendToAllTCP(new NetFirstPlayerSelected(GameServer.this.room.find(event.getFirstPlayer())));
        }

        @Override
        public void handleGameEndedEvent(GameEndedEvent gameEndedEvent) {
            this.server.sendToAllTCP(new NetGameEnded(GameServer.this.room.findAll(gameEndedEvent.getWinners())));
            Log.trace(GameServer.LogicListener.class.toString(), "Game terminated.");
        }

        @Override
        public void handlePhaseChanged(PhaseChangedEvent event) {
            this.server.sendToAllTCP(new NetPhaseChange(event));
        }


        // logic.event, alphabetical order on argument type
        @Override
        public void handleGameEndConditionsReached(GameEndConditionsReachedEvent event) {
            this.server.sendToAllTCP(new NetGameEndConditionsReached(event));
        }

        @Override
        public void handleMarketingLadderUpdated(MarketingLadderUpdatedEvent event) {
            this.server.sendToAllTCP(new NetMarketingLadderUpdated(event));
        }

        @Override
        public void handleNumberOfFansChanged(NumberOfFansChangedEvent event) {
            this.server.sendToAllTCP(new NetFanNumberUpdated(event, GameServer.this.room.find(event.getPlayer().getID())));
        }

        @Override
        public void handleStudioAddedEvent(StudioEvent event) {
            this.server.sendToAllTCP(new NetStudio(event));
        }

        @Override
        public void handleStudioRemovedEvent(StudioEvent event) {
            this.server.sendToAllTCP(new NetStudio(event));
        }

        @Override
        public void handleUnitEvent(UnitCounterEvent event) {
            this.server.sendToAllTCP(new NetUnitEvent(event));
        }

        // todo (idem)
    }

    static final String[] names = {"Suna", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto", "Ceres", "Pallas", "Vesta", "Hygiea", "Interamnia", "Europa", "Davida", "Sylvia", "Cybele", "Eunomia", "Juno", "Euphrosyne", "Hektor", "Thisbe", "Bamberga", "Patientia", "Herculina", "Doris", "Ursula", "Camilla", "Eugenia", "Iris", "Amphitrite"};

    private static String generateName() {
        Random random = new Random();
        {
            String out = new String();
            int syllableNbr = random.nextInt(2) + 2;
            for (int j = 0; j < syllableNbr; j++) {
                String word = names[random.nextInt(names.length)];
                int tmp = random.nextInt(word.length() - 2);
                int tmp2 = random.nextInt(2) + 2 + tmp;
                if (tmp2 > word.length()) {
                    tmp2 = word.length();
                }
                out += word.substring(tmp, tmp2);
            }
            return out.substring(0, 1).toUpperCase() + out.substring(1).toLowerCase();
        }
    }
}
