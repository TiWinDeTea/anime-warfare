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

import com.esotericsoftware.minlog.Log;
import org.tiwindetea.animewarfare.net.networkevent.NetworkCommand;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class used to look for game servers.
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class ServerScanner implements Runnable {

    static class AlreadyRunningException extends RuntimeException {

        AlreadyRunningException() {
            super();
        }

        AlreadyRunningException(String message) {
            super(message);
        }

    }

    static class IllegalCallException extends RuntimeException {

        IllegalCallException() {
            super();
        }

        IllegalCallException(String message) {
            super(message);
        }

    }

    /**
     * Number of IOException to catch before calling the "onFailure" setted Function
     *
     * @see ServerScanner#setOnFailure(Function)
     */
    public static final int TOLERANCE = 100;

    private static final int REFRESH_RATE_MS = 1000;

    private static final int DISCOVERY_TIMEOUT = 500;
    private static final byte[] HEADER = Utils.VERSION_HEADER.getBytes(Utils.CHARSET);

    private static final byte[] REQUEST = new byte[HEADER.length + 1];
    private volatile boolean isRunning = false;
    private volatile boolean isPaused;
    private boolean selfStarted = false;

    private volatile boolean haveBeenNotified = false;
    private final ExecutorService THREAD = Executors.newSingleThreadExecutor();
    private InetSocketAddress[] addresses;

    private Consumer<Room> onDiscovery;
    private Consumer<Room> onDisappear;
    private Consumer<Room> onUpdate;
    private Function<IOException, Boolean> onFailure;

    private DatagramSocket socket;

    static {
        System.arraycopy(HEADER, 0, REQUEST, 0, HEADER.length);
        REQUEST[HEADER.length] = NetworkCommand.SCANNING.getValue();
    }

    /**
     * Looks for available servers on local network
     *
     * @param UDPport UDP port on which servers are running
     * @param timeout The number of milliseconds to wait for a response
     * @return A list containing all servers found on LAN
     *
     * @see ServerScanner#discoverAt(int, InetSocketAddress...)
     * @see ServerScanner#discoverAt(int, Collection)
     */
    public static List<Room> discover(int UDPport, int timeout) {
        return discoverAt(timeout, toBroadcast(UDPport));
    }

    /**
     * Looks for available servers on a specified list of addresses
     *
     * @param timeout   The number of milliseconds to wait for a response
     * @param addresses Addresses of the servers
     * @return A list containing all servers found on LAN
     *
     * @see ServerScanner#discoverAt(int, InetSocketAddress...)
     * @see ServerScanner#discover(int, int)
     */
    public static List<Room> discoverAt(int timeout, Collection<InetSocketAddress> addresses) {
        return discoverAt(timeout, addresses.toArray(new InetSocketAddress[addresses.size()]));
    }

    /**
     * Looks for available servers on a specified list of addresses
     *
     * @param timeout   The number of milliseconds to wait for a response
     * @param addresses Addresses of the servers
     * @return A list containing all servers found on LAN
     *
     * @see ServerScanner#discoverAt(int, InetSocketAddress...)
     * @see ServerScanner#discover(int, int)
     */
    public static List<Room> discoverAt(int timeout, InetSocketAddress... addresses) {

        List<Room> lanRooms = new ArrayList<>();

        try (DatagramSocket lookup = new DatagramSocket()) {

            lookup.setSoTimeout(timeout);
            for (InetSocketAddress address : addresses) {
                ServerScanner.sendHeader(lookup, address);
            }

            Log.trace(ServerScanner.class.getName(), "Listening for server answers");

            //noinspection InfiniteLoopStatement
            for (; ; ) {
                Room room = ServerScanner.listenForServer(lookup);
                if (room != null && !lanRooms.contains(room)) {
                    Log.trace(ServerScanner.class.getName(), "Adding " + room + " to rooms list");
                    lanRooms.add(room);
                }
            }
        } catch (SocketTimeoutException ignored) {
            Log.trace(ServerScanner.class.getName(), "Stopping servers lookup (timed out)");
        } catch (IOException e) {
            Log.warn(ServerScanner.class.getName(), "Unexpected IOException", e);
        }
        return lanRooms;
    }

    /**
     * Scans for server on local network in a separated thread.
     *
     * @param UDPPort UDP port on which servers are running
     * @throws AlreadyRunningException if this instance of ServerScanner is already running.
     * @throws IllegalStateException if this instance of ServerScanner is shutdown
     * @throws SocketException       if a failure occured when trying to open sockets.
     *
     * @see ServerScanner#setOnDisappear(Consumer)
     * @see ServerScanner#setOnDiscovery(Consumer)
     * @see ServerScanner#setOnFailure(Function)
     * @see ServerScanner#stop()
     *
     * @see ServerScanner#discoverAt(int, Collection)
     */
    public void parallelDiscovery(int UDPPort) throws SocketException {
        this.parallelDiscoveryAt(toBroadcast(UDPPort));
    }

    /**
     * Scans for server on specified addresses in a separated thread.
     *
     * @param addresses Addresses of the servers
     * @throws AlreadyRunningException if this instance of ServerScanner is already running.
     * @throws IllegalStateException if this instance of ServerScanner is shutdown
     * @throws SocketException       if a failure occured when trying to open sockets.
     * @see ServerScanner#setOnDisappear(Consumer)
     * @see ServerScanner#setOnDiscovery(Consumer)
     * @see ServerScanner#setOnFailure(Function)
     * @see ServerScanner#stop()
     *
     * @see ServerScanner#discoverAt(int, Collection)
     * @see ServerScanner#parallelDiscovery(int)
     * @see ServerScanner#parallelDiscoveryAt(InetSocketAddress...)
     */
    public void parallelDiscoveryAt(Collection<InetSocketAddress> addresses) throws SocketException {
        this.parallelDiscoveryAt(addresses.toArray(new InetSocketAddress[addresses.size()]));
    }

    /**
     * Scans for server on specified addresses in a separated thread.
     *
     * @param addresses Addresses of the servers
     * @throws AlreadyRunningException if this instance of ServerScanner is already running.
     * @throws IllegalStateException if this instance of ServerScanner is shutdown
     * @throws SocketException       if a failure occured when trying to open sockets.
     * @see ServerScanner#setOnDisappear(Consumer)
     * @see ServerScanner#setOnDiscovery(Consumer)
     * @see ServerScanner#setOnFailure(Function)
     * @see ServerScanner#stop()
     *
     * @see ServerScanner#discoverAt(int, InetSocketAddress...)
     * @see ServerScanner#parallelDiscovery(int)
     * @see ServerScanner#parallelDiscoveryAt(Collection)
     */
    public void parallelDiscoveryAt(InetSocketAddress... addresses) throws SocketException {
        if (this.isRunning) {
            throw new AlreadyRunningException("ServerScanner already running");
        }

        if (this.THREAD.isShutdown()) {
            throw new IllegalStateException("This instance of ServerScanner is shutdown");
        }

        try {
            this.socket = new DatagramSocket();
            this.socket.setSoTimeout(ServerScanner.DISCOVERY_TIMEOUT);
        } catch (SocketException e) {
            this.socket.close();
            throw e;
        }

        this.addresses = addresses;
        this.selfStarted = true;
        this.THREAD.submit(this);
    }

    /**
     * @return true if this instance is currently scanning for rooms, false otherwise
     *
     * @see ServerScanner#isShutdown()
     * @see ServerScanner#stop()
     * @see ServerScanner#shutdown()
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Stops this instance from scanning.
     *
     * @see ServerScanner#shutdown()
     * @see ServerScanner#isRunning()
     * @see ServerScanner#isShutdown()
     *
     * @see ServerScanner#parallelDiscovery(int)
     */
    public void stop() {
        this.isRunning = false;
        this.socket.close();
    }

    /**
     * @param onDisappear Consumer called when a room that was present before is not detected anymore.
     *
     * @see ServerScanner#setOnDiscovery(Consumer)
     * @see ServerScanner#setOnUpdate(Consumer)
     * @see ServerScanner#setOnFailure(Function)
     */
    public void setOnDisappear(Consumer<Room> onDisappear) {
        this.onDisappear = onDisappear;
    }

    /**
     * @param onDiscovery Consumer called when a new room is found
     *
     * @see ServerScanner#setOnDisappear(Consumer)
     * @see ServerScanner#setOnUpdate(Consumer)
     * @see ServerScanner#setOnFailure(Function)
     */
    public void setOnDiscovery(Consumer<Room> onDiscovery) {
        this.onDiscovery = onDiscovery;
    }

    /**
     * @param onUpdate Consumer called when a room is updated (ie: change in players)
     *
     * @see ServerScanner#setOnDiscovery(Consumer)
     * @see ServerScanner#setOnDisappear(Consumer)
     * @see ServerScanner#setOnFailure(Function)
     */
    public void setOnUpdate(Consumer<Room> onUpdate) {
        this.onUpdate = onUpdate;
    }

    /**
     * Sometimes, things doesn't go as expected. This may help you to settle things
     *
     * @param onFailure Function called when the number of IOExceptions received when trying to listen
     *                  and/or sending through network exceeds {@link ServerScanner#TOLERANCE}.
     *                  If this function returns true, the ServerScanner will try to go for another
     *                  {@link ServerScanner#TOLERANCE}. Otherwise, it will stop.
     *
     * @see ServerScanner#setOnDiscovery(Consumer)
     * @see ServerScanner#setOnUpdate(Consumer)
     * @see ServerScanner#setOnDisappear(Consumer)
     */
    public void setOnFailure(Function<IOException, Boolean> onFailure) {
        this.onFailure = onFailure;
    }

    /**
     * If it is running, forces the ServerScanner to update, cleaning all previously
     * found rooms (the Consumer setted using {@link ServerScanner#setOnDisappear(Consumer)}
     * will be called)
     */
    public synchronized void forceUpdate() {
        this.haveBeenNotified = true;
        notify();
    }

    /**
     * Pauses the discovery of rooms.
     *
     * @see ServerScanner#resume()
     * @see ServerScanner#stop()
     * @see ServerScanner#parallelDiscovery(int)
     */
    public synchronized void pause() {
        this.isPaused = true;
        this.notify();
    }

    /**
     * Resumes the discovery of rooms
     *
     * @see ServerScanner#pause()
     * @see ServerScanner#parallelDiscovery(int)
     * @see ServerScanner#stop()
     */
    public synchronized void resume() {
        this.isPaused = false;
        this.haveBeenNotified = true;
        this.notify();
    }

    /**
     * @throws IllegalCallException if this method have been called
     */
    @Override
    public void run() {

        if (!this.selfStarted) {
            throw new IllegalCallException();
        }
        this.selfStarted = false;

        this.isRunning = true;
        int errorCount = 0;

        final TreeSet<Room> retrieved = new TreeSet<>();
        final TreeSet<Room> currentRun = new TreeSet<>();

        final List<Room> toDelete = new ArrayList<>(5);
        try {
            while (this.isRunning) {

                try {
                    for (InetSocketAddress address : this.addresses) {
                        sendHeader(this.socket, address);
                    }

                    for (; ; ) {
                        Room room = ServerScanner.listenForServer(this.socket);
                        if (room != null && !currentRun.contains(room)) {
                            currentRun.add(room);
                            Room r = retrieved.floor(room);
                            if (r == null || !r.equals(room)) {
                                Log.trace(ServerScanner.class.getName(), "Parallel discovery: found " + room);
                                Consumer<Room> discoveryHandler = this.onDiscovery;
                                if (discoveryHandler != null) {
                                    discoveryHandler.accept(room);
                                }
                                retrieved.add(room);
                            } else if (r != null && r.updateable(room)) {
                                Log.trace(ServerScanner.class.getName(), "Parallel discovery: updated " + room);
                                Consumer<Room> updateHandler = this.onUpdate;
                                if (updateHandler != null) {
                                    updateHandler.accept(room);
                                }
                                retrieved.remove(r);
                                retrieved.add(room);
                            }
                        }
                    }
                } catch (SocketTimeoutException ignored) {
                    Log.trace(ServerScanner.class.getName(), "Stopping servers lookup (timed out)");
                } catch (IOException e) {
                    Log.warn(ServerScanner.class.getName(), "Unexpected IOException", e);
                    if (++errorCount > TOLERANCE) {
                        Log.error(GameServer.class.getName() + "#run()", "error count overcame tolerance.", e);

                        if (this.onFailure != null && this.onFailure.apply(e).booleanValue()) {
                            errorCount = 0;
                        } else {
                            this.isRunning = false;
                        }
                    }

                    if (this.socket.isClosed()) {   // I didn't close it at this point, but fortunatly, java often closes it without asking me before.
                        DatagramSocket localSocket;
                        try {
                            localSocket = new DatagramSocket();
                            this.socket.close();
                            this.socket = localSocket;
                            this.socket.setSoTimeout(ServerScanner.DISCOVERY_TIMEOUT);
                        } catch (SocketException ignored) {
                        }
                        this.haveBeenNotified = true;
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                while (this.isPaused) {
                    try {
                        synchronized (this) {
                            wait(10 * REFRESH_RATE_MS);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                retrieved.forEach(room1 -> {
                    if (!currentRun.contains(room1)) {
                        Consumer<Room> disappearenceHandler = this.onDisappear;
                        if (disappearenceHandler != null) {
                            Log.trace(ServerScanner.class.getName(), "Parallel discovery: removing " + room1);
                            disappearenceHandler.accept(room1);
                            toDelete.add(room1);
                        }
                    }
                });

                retrieved.removeAll(toDelete);
                toDelete.clear();

                currentRun.clear();

                if (!this.haveBeenNotified) {
                    try {
                        synchronized (this) {
                            wait(REFRESH_RATE_MS);
                        }
                    } catch (InterruptedException ignored) {
                        Log.trace(ServerScanner.class.getName(), "Was woken up");
                    }
                }

                if (this.haveBeenNotified) {
                    Consumer<Room> disappearenceHandler;
                    for (Room room : retrieved) {
                        disappearenceHandler = this.onDisappear;
                        if (disappearenceHandler != null) {
                            disappearenceHandler.accept(room);
                        }
                    }
                    retrieved.clear();
                    this.haveBeenNotified = false;
                }
            }
        } finally {
            this.socket.close();
        }
    }

    /**
     * Shutdowns this {@link ServerScanner}'s. It cannot be reused afterwards
     *
     * @see ServerScanner#stop()
     * @see ServerScanner#parallelDiscovery(int)
     * @see ServerScanner#pause()
     */
    public synchronized void shutdown() {
        this.isRunning = false;
        this.isPaused = false;
        this.notify();
        this.THREAD.shutdown();
    }

    /**
     * @return true if this instance of {@link ServerScanner} has been shutdown, false otherwise.
     *
     * @see ServerScanner#shutdown()
     */
    public boolean isShutdown() {
        return this.THREAD.isShutdown();
    }

    private static InetSocketAddress[] toBroadcast(int port) {
        List<InetAddress> broadcastAddresses = Utils.findBroadcastAddr();
        InetSocketAddress addresses[] = new InetSocketAddress[broadcastAddresses.size()];
        int i = 0;
        for (InetAddress broadcastAddress : broadcastAddresses) {
            addresses[i] = new InetSocketAddress(broadcastAddress, port);
            ++i;
        }
        return addresses;
    }

    private static Room listenForServer(DatagramSocket socket) throws IOException {

        byte[] reception = new byte[8192];
        DatagramPacket rpacket = new DatagramPacket(reception, reception.length);
        int kk = socket.getLocalPort();
        socket.receive(rpacket);
        Log.trace(ServerScanner.class.getName(), "Received data from " + rpacket.getAddress());
        reception = rpacket.getData();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(reception)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                String serverVersionHeader;
                Room serverRoom;
                serverVersionHeader = (String) ois.readObject();
                serverRoom = (Room) ois.readObject();

                if (Utils.VERSION_HEADER.equals(serverVersionHeader)) {
                    serverRoom.setAddress(rpacket.getAddress());
                    return serverRoom;
                }
            } catch (ClassNotFoundException e) {
                Log.warn(ServerScanner.class.getName(), "Failed to read room from remote " + rpacket.getAddress(), e);
            }
        }
        return null;
    }

    private static void sendHeader(DatagramSocket socket, InetSocketAddress address) throws IOException {
        DatagramPacket packet = new DatagramPacket(REQUEST, REQUEST.length, address.getAddress(), address.getPort());
        Log.trace(ServerScanner.class.getName(), "Sending scan request to " + address);
        socket.send(packet);
    }

}
