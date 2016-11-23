package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetMessage implements Serializable {

    private final String message;
    private final GameClientInfo client;

    /**
     * Empty NetMessage instance. Required by kryonet lib
     */
    public NetMessage() {
        this.message = null;
        this.client = null;
    }

    public NetMessage(String message) {
        this.message = message;
        this.client = null;
    }

    public NetMessage(String message, GameClientInfo client) {
        this.message = message;
        this.client = client;
    }

    public NetMessage(String message, String senderName, int senderId) {
        this.message = message;
        this.client = new GameClientInfo(senderName, senderId);
    }

    public String getMessage() {
        return this.message;
    }

    public GameClientInfo getSenderInfos() {
        return this.client;
    }
}
