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
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedEvent;
import org.tiwindetea.animewarfare.net.networkevent.NetworkCommand;

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

    public GameClient() {
        this(null);
    }

    public GameClient(String name) {
        this.myName.gameClientName = name;
        Registerer.registerClasses(this.client);
    }

    public void setName(String name) {
        if (this.isConnected) {
            throw new IllegalStateException();
        } else {
            this.myName.gameClientName = name;
        }
    }

    public void connect(Room room) throws IOException {
        if (this.myName.gameClientName == null) {
            throw new IllegalStateException();
        }
        this.client.addListener(this.listener);
        this.client.start();
        this.client.connect(500, room.getAddress(), room.getPort());
    }

    public Room getRoom() {
        return this.room;
    }

    public void disconnect() {
        if (this.isConnected) {
            this.client.stop();
            this.client.removeListener(this.listener);
            this.isConnected = false;
        }
    }

    public void send(String string) {
        this.client.sendTCP(string);
    }

    public class Listener extends com.esotericsoftware.kryonet.Listener.ReflectionListener {

        public void received(Connection connection, GameClientInfo id) {
            if (id.gameClientName == null) {
                GameClient.this.myName.id = id.id;
                GameClient.this.client.sendTCP(GameClient.this.myName);
            } else {
                // TODO: player connected
            }
        }

        public void received(Connection connection, Room room) {
            GameClient.this.room = room;
            // TODO : fire I am connected ?
        }

        public void received(Connection connection, String message) {
            GameClient.this.eventDispatcher.fire(new MessageReceivedEvent(message));
        }
    }
}
