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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class UDPListener implements Runnable {


    private volatile boolean isRunning = false;
    private volatile boolean clientClosed;
    private Room room;
    private int port;
    private DatagramSocket client;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    UDPListener() {
        this.port = -1;
        this.room = null;
        this.client = null;
    }

    UDPListener(Room room, int listeningPort) {
        this.room = room;
        this.port = listeningPort;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void bind(int port) throws SocketException {

        this.port = port;
        this.clientClosed = true;
        if (this.client != null) {
            this.client.close();
        }
        this.client = new DatagramSocket(this.port);
        this.start();
    }

    public void start() {

        this.isRunning = true;
        if (this.room == null || this.client == null || this.client.isClosed()) {
            throw new IllegalStateException("Trying to start an UDPListener with unvalid parameters");
        }
        this.executor.submit(this);
    }

    @Override
    public void run() {
        try {
            this.clientClosed = false;

            byte[] versionHeader = Utils.VERSION_HEADER.getBytes(Utils.CHARSET);
            int size = versionHeader.length + 1;
            DatagramPacket packet = new DatagramPacket(new byte[size], size);
            Log.info(UDPListener.class.getName(), "Starting UDPListener for room " + this.room);
            while (this.isRunning) {
                try {
                    this.client.receive(packet);


                    if (packet.getLength() == size) {
                        byte[] data = packet.getData();

                        boolean valid = true;
                        for (int i = size - 2; i >= 0 && valid; i--) {
                            valid = data[i] == versionHeader[i];
                        }

                        if (valid) {

                            if (data[size - 1] == NetworkCommand.SCANNING.getValue()) {

                                Log.trace(UDPListener.class.getName(), "Sending game infos to " + packet.getAddress());

                                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                                    try (ObjectOutputStream os = new ObjectOutputStream(bos)) {
                                        os.writeObject(Utils.VERSION_HEADER);
                                        os.writeObject(this.room);
                                        os.flush();
                                    }

                                    data = bos.toByteArray();
                                    this.client.send(new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort()));

                                } catch (IOException e) {
                                    Log.warn(UDPListener.class.getName(), "Failed to instantiate the ByteArrayOutpuStream", e);
                                    stop();
                                }
                            } else {
                                Log.trace(UDPListener.class.getName(), "Unexpected command received from " + packet.getAddress());
                            }
                        } else {
                            Log.trace(UDPListener.class.getName(), "Bad Version header received from " + packet.getAddress());
                        }
                    } else {
                        Log.trace(UDPListener.class.getName(), "Bad Version header received from " + packet.getAddress());
                    }

                } catch (SocketException e) {
                    if (this.clientClosed) {
                        this.isRunning = false;
                    } else {

                        Log.info(UDPListener.class.getName(), "Catched an unexpected SocketException");
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ignore) {
                        }
                    }
                } catch (IOException e) {
                    Log.debug(UDPListener.class.getName(), "Unexpected IOException", e);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        } catch (Exception e) {
            Log.error(UDPListener.class.getName(), "Caught " + e.getClass(), e);
            throw e;

        } finally {
            this.client.close();
            this.client.disconnect();
            this.executor.shutdown();
            Log.info(UDPListener.class.getName(), "Stopping UDPListener for room " + this.room);
        }
    }

    public void stop() {
        this.isRunning = false;
        this.clientClosed = true;
        this.client.close();
    }
}
