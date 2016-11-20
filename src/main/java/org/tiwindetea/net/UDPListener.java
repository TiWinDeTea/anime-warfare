package org.tiwindetea.net;

import com.esotericsoftware.minlog.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;

public class UDPListener implements Runnable {


    private volatile boolean isRunning = true;
    private volatile boolean clientClosed;
    private Room room;
    private int port;
    private DatagramSocket client;

    UDPListener() {

    }

    UDPListener(Room room, int listeningPort, UUID me) {
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

    public void open() throws SocketException {
        this.clientClosed = true;
        if (this.client != null) {
            this.client.close();
        }
        this.client = new DatagramSocket(this.port);
    }

    @Override
    public void run() {
        this.isRunning = true;
        this.clientClosed = false;

        byte[] versionHeader = Utils.VERSION_HEADER.getBytes(Utils.CHARSET);
        int size = versionHeader.length + 1;
        DatagramPacket packet = new DatagramPacket(new byte[size], size);
        Log.trace(UDPListener.class.getName(), "Starting UDPListener for room" + this.room);
        while (this.isRunning) {
            try {
                this.client.receive(packet);


                if (packet != null && packet.getLength() == size) {
                    byte[] data = packet.getData();

                    boolean valid = true;
                    for (int i = size - 2; i >= 0 && valid; i--) {
                        valid = data[i] == versionHeader[i];
                    }

                    if (valid) {

                        if (data[size - 1] == NetworkCommand.SCANNING.value) {

                            Log.trace(UDPListener.class.getName(), "Sending game infos to " + packet.getAddress());

                            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                                try (ObjectOutputStream os = new ObjectOutputStream(bos)) {
                                    os.writeObject(this.room);
                                    os.flush();
                                }

                                data = bos.toByteArray();
                                this.client.send(new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort()));

                            } catch (IOException e) {
                                Log.warn(UDPListener.class.getName(), "Failed to instantiate the ByteArrayOutpuStream", e);
                                stop();
                            }
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
                        Thread.sleep(100);
                    } catch (InterruptedException ignore) {
                    }
                }
            } catch (IOException e) {
                Log.debug(UDPListener.class.getName(), "Unexpected IOException", e);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }
        this.client.disconnect();
        Log.trace(UDPListener.class.getName(), "Stopping UDPListener for room " + this.room);
    }

    public void stop() {
        this.isRunning = false;
        this.clientClosed = true;
        this.client.close();
    }
}
