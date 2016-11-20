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
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedEvent;

import java.io.IOException;

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

    //Scheduler scheduler;
    private final Server server = new Server();

    private final EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    private final Room room = new Room();
    private final Listener listener = new Listener(this.server);
    private final UDPListener udpListener = new UDPListener();
    private boolean isRunning = false;

    public GameServer() {
        this(null, null);
    }

    public GameServer(String gameName) {
        this(gameName, null);
    }

    public GameServer(String gameName, String gamePassword) {
        this.room.setGameName(gameName);
        this.room.setGamePassword(gamePassword);
        this.udpListener.setRoom(this.room);
        Registerer.registerClasses(this.server);
    }

    /**
     * Sets the game password
     *
     * @param gamePassword new password for the game
     * @throws IllegalStateException if the server is already running
     */
    public void setGamePassword(String gamePassword) {
        if (this.isRunning) {
            throw new IllegalStateException();
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
    public void setName(String gameName) {
        if (this.isRunning) {
            throw new IllegalStateException();
        } else {
            this.room.setGameName(gameName);
        }
    }

    /**
     * Start the server with a given TCP port for client's
     * connections and UDP port for client's broacast.
     * <p>
     * Any incoming requests will be ignored until the {@link GameServer#start()}
     * method is called. Clients will still be able to connect, though
     *
     * @param TCPport Port to use for clients connection on TCP
     * @param UDPport Port to use for clients broadcasting when discovering
     * @throws IOException If the server could not be opened
     */
    public void bind(int TCPport, int UDPport) throws IOException {
        this.server.bind(TCPport);
        this.room.setPort(TCPport);
        this.udpListener.bind(UDPport);
        this.start();
    }

    /**
     * Starts the server if it is not started yet,
     * closes all connections that have been previously done
     * and start treating incoming connections and requests.
     * <p>
     * Basically, starts the game
     */
    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            if (!this.udpListener.isRunning()) {
                this.server.start();
                this.udpListener.start();
            }
            this.server.addListener(this.listener);
        }
    }

    /**
     * Closes the TCP server together with the
     * UDP server.
     */
    public void stop() {
        this.isRunning = false;
        if (this.udpListener.isRunning()) {
            this.udpListener.stop();
        }
        this.server.stop();
        this.server.removeListener(this.listener);
    }

    /**
     * returns true if the server is running
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    public class Listener extends com.esotericsoftware.kryonet.Listener.ReflectionListener {

        private Server server;

        Listener(Server server) {
            this.server = server;
        }

        public void connected(Connection connection) {
            connection.sendTCP(new GameClientInfo(connection.getID()));
            connection.sendTCP(GameServer.this.room);
        }

        public void received(Connection connection, GameClientInfo info) {
            this.server.sendToAllExceptTCP(connection.getID(), info);
            GameServer.this.room.addMember(info);
        }

        public void received(Connection connection, String string) {
            this.server.sendToAllExceptTCP(connection.getID(), string);
        }

        public void received(Connection connection, MessageReceivedEvent message) {
            this.server.sendToAllExceptTCP(connection.getID(), message);
        }

        //TODO
        /*
        public void received(Connection connection, String string){
		}
		 */
    }
}
