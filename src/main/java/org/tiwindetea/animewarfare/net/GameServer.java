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
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.sun.istack.internal.Nullable;
import org.lomadriel.lfc.event.EventDispatcher;
import org.lomadriel.lfc.statemachine.DefaultStateMachine;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.states.FirstTurnStaffHiringState;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEventListener;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEventListener;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEventListener;
import org.tiwindetea.animewarfare.net.logicevent.PlayingOrderChoiceEvent;
import org.tiwindetea.animewarfare.net.networkrequests.NetFirstPlayerSelected;
import org.tiwindetea.animewarfare.net.networkrequests.NetFirstPlayerSelectionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.NetGameEnded;
import org.tiwindetea.animewarfare.net.networkrequests.NetGameStarted;
import org.tiwindetea.animewarfare.net.networkrequests.NetHandlePlayerDisconnection;
import org.tiwindetea.animewarfare.net.networkrequests.NetLockFaction;
import org.tiwindetea.animewarfare.net.networkrequests.NetMessage;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.NetSelectFaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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

    private DefaultStateMachine stateMachine;
    private final Server server = new Server();

    private final EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    private final Room room = new Room();
    private final NetworkListener networkNetworkListener = new NetworkListener(this.server);
    private final LogicListener logicListener = new LogicListener();
    private final UDPListener udpListener = new UDPListener();
    private boolean isRunning = false;

    private final HashMap<Integer, FactionType> playersSelection = new HashMap<>(4, 1);
    private final HashMap<Integer, FactionType> playersLocks = new HashMap<>(4, 1);
    private final TreeSet<Integer> legitConnections = new TreeSet<>(Integer::compareTo);

    /**
     * Instanciate a server with a random name and without password
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
     */
    public GameServer(@Nullable String gameName) {
        this(-1, gameName, null);
    }

    public GameServer(@Nullable String gameName, @Nullable String gamePassword) {
        this(-1, gameName, gamePassword);
    }

    /**
     * Instanciate a server given its name and password
     *
     * @param gameName     Name of the Room
     * @param gamePassword Password of the Room
     */
    public GameServer(int numberOfExpectedPlayers, @Nullable String gameName, @Nullable String gamePassword) {
        if (gameName == null) {
            gameName = new BigInteger(17, new Random()).toString(Character.MAX_RADIX);
            Log.trace(GameServer.class.toString(), "Server name randomly created: " + gameName);
        }
        this.room.setGameName(gameName);
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
     */
    public void setGameName(String gameName) {
        if (this.isRunning) {
            throw new IllegalStateException("Server already running");
        } else {
            this.room.setGameName(gameName);
        }
    }

    /**
     * Sets the number of players for the game
     *
     * @param numberOfExpectedPlayer the number of players
     * @throws IllegalStateException    If the server is already running.
     * @throws IllegalArgumentException If the passed argument is outside of the [2-4] range
     */
    public void setNumberOfExpectedPlayer(int numberOfExpectedPlayer) {
        if (this.isRunning) {
            throw new IllegalStateException("Server already running");
        } else if (numberOfExpectedPlayer < 2 || numberOfExpectedPlayer > 4) {
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
     */
    public void bind(int TCPport, int UDPport) throws IOException {
        Log.trace(GameServer.class.toString(), "Binding server to " + TCPport + " - " + UDPport);
        this.server.bind(TCPport);
        this.room.setPort(TCPport);
        this.udpListener.bind(UDPport);
    }

    /**
     * Starts the server if it is not started yet,
     *
     * @throws IllegalStateException if the number of player was not set
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
            }
        } else {
            throw new IllegalStateException("Number of expected players was not set");
        }
    }

    /**
     * Closes the TCP server together with the
     * UDP server.
     */
    public void stop() {
        if (this.isRunning) {
            if (this.udpListener.isRunning()) {
                this.udpListener.stop();
            }
            this.server.stop();
            this.server.removeListener(this.networkNetworkListener);
            this.room.clear();
            this.isRunning = false;
            Utils.deregisterLogicListener(this.logicListener);
        }
    }

    /**
     * returns true if the server is running
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * @return true if the server is visible for udp broadcasters
     */
    public boolean isVisible() {
        return this.udpListener.isRunning();
    }

    /**
     * stops the udp listening
     */
    public void hide() {
        this.udpListener.stop();
    }

    /**
     * starts the udp listening. (automatically done
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

    void initNew() {
        this.udpListener.stop();
        this.stateMachine = new DefaultStateMachine(new FirstTurnStaffHiringState(this.playersLocks));
        this.playersSelection.clear();
        this.playersLocks.clear();
        this.server.sendToAllTCP(new NetGameStarted());
        Log.debug(GameServer.class.toString(), this.room.getGameName() + ": started");
    }

    public class NetworkListener extends com.esotericsoftware.kryonet.Listener.ReflectionListener {

        private final Server server;

        NetworkListener(Server server) {
            this.server = server;
        }

        @Override
        public void connected(Connection connection) {
            if (GameServer.this.room.isFull()) {
                Log.trace(NetworkListener.class.toString(),
                          "Refused incomming connection: " + connection.getID());
                connection.close();
            } else {
                Log.trace(NetworkListener.class.toString(), "Incomming connection: " + connection.getID());
                connection.sendTCP(new GameClientInfo(connection.getID()));
                connection.sendTCP(GameServer.this.room);
            }
        }

        @Override
        public void disconnected(Connection connection) {
            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {
                GameClientInfo info = GameServer.this.room.removeMember(connection.getID());
                GameServer.this.legitConnections.remove(new Integer(connection.getID()));
                Log.debug(GameServer.NetworkListener.class.toString(), "Player " + info + " disconnected");
                this.server.sendToAllTCP(new NetHandlePlayerDisconnection(info));
            }
        }

        // general
        public void received(Connection connection, GameClientInfo info) {
            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {
                this.server.sendToAllExceptTCP(connection.getID(), info);
                GameServer.this.room.addMember(info);
                if (GameServer.this.room.getMembers().size() == GameServer.this.room.getNumberOfExpectedPlayers()) {
                    GameServer.this.initNew();
                }
                Log.debug(GameServer.NetworkListener.class.toString(), "Player " + info + " connected");
            }
        }

        public void received(Connection connection, NetMessage message) {
            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {
                this.server.sendToAllExceptTCP(connection.getID(),
                                               new NetMessage(message.getMessage(),
                                                              GameServer.this.room.find(connection.getID())));
                Log.debug(GameServer.NetworkListener.class.toString(), "Received message: " + message.getMessage());
            }
        }

        // network requests
        public void received(Connection connection, NetLockFaction faction) {
            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {

                Log.trace(GameServer.NetworkListener.class.toString(), "Faction locking requested: " + faction);
                boolean isFactionLocked = false;
                for (Map.Entry<Integer, FactionType> integerFactionTypeEntry : GameServer.this.playersSelection.entrySet()) {
                    if (integerFactionTypeEntry.getValue() == faction.getFaction()) {
                        isFactionLocked = true;
                        break;
                    }
                }
                if (!isFactionLocked) {
                    int numberOfTimesTheFactionIsSelected = (int) GameServer.this.playersSelection.entrySet()
                                                                                                  .stream()
                                                                                                  .filter(integerFactionTypeEntry -> integerFactionTypeEntry
                                                                                                          .getValue() == faction
                                                                                                          .getFaction())
                                                                                                  .count();
                    if (numberOfTimesTheFactionIsSelected <= 1) {
                        this.server.sendToAllTCP(faction);
                        GameServer.this.playersLocks.put(new Integer(connection.getID()), faction.getFaction());
                        Log.debug(GameServer.NetworkListener.class.toString(),
                                  "Player " + GameServer.this.room.find(connection.getID()) + " locked " + faction);

                        if (GameServer.this.playersLocks.size() == GameServer.this.room.getNumberOfExpectedPlayers()) {
                            initNew();
                        }
                    }
                }
            }
        }

        public void received(Connection connection, NetSelectFaction faction) {

            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {
                GameServer.this.playersSelection.put(new Integer(connection.getID()), faction.getFactionType());
                this.server.sendToAllTCP(faction);
                Log.trace(GameServer.NetworkListener.class.toString(),
                          GameServer.this.room.find(connection.getID()) + " selected " + faction);
            }
        }

        public void received(Connection connection, NetPlayingOrderChosen netPlayingOrderChosen) {

            if (GameServer.this.legitConnections.contains(new Integer(connection.getID()))) {
                GameServer.this.eventDispatcher.fire(new PlayingOrderChoiceEvent(netPlayingOrderChosen.isClockwise()));
                this.server.sendToAllTCP(netPlayingOrderChosen);
                Log.trace(GameServer.NetworkListener.class.toString(),
                          "Play order was choosen (" + netPlayingOrderChosen);
            }
        }

        // todo (when possible, which is not yet) ;
        // don't forget to check that the client is a legit client (ie : is in the player that sent the good password)
    }

    public class LogicListener implements AskFirstPlayerEventListener, FirstPlayerSelectedEventListener, GameEndedEventListener {

        @Override
        public void handleGameEndedEvent(GameEndedEvent gameEndedEvent) {
            GameServer.this.server.sendToAllTCP(new NetGameEnded(GameServer.this.room.findAll(gameEndedEvent.getWinners())));
            Log.trace(GameServer.LogicListener.class.toString(), "Game terminated.");
        }

        @Override
        public void askFirstPlayerEvent(AskFirstPlayerEvent event) {
            GameServer.this.server.sendToAllTCP(new NetFirstPlayerSelectionRequest(GameServer.this.room.find(event.getLastPlayer()),
                                                                                   GameServer.this.room.findAll(event.getDrawPlayers())));
        }

        @Override
        public void firstPlayerSelected(FirstPlayerSelectedEvent event) {
            GameServer.this.server.sendToAllTCP(new NetFirstPlayerSelected(GameServer.this.room.find(event.getFirstPlayer())));
        }

        // todo (idem)
    }
}
