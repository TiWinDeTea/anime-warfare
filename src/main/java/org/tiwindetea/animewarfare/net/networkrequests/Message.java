package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class Message implements Serializable {

    private final String message;
    private final GameClientInfo client;

    /**
     * Empty Message instance. Required by kryonet lib
     */
    public Message() {
        this.message = null;
        this.client = null;
    }

    public Message(String message) {
        this.message = message;
        this.client = null;
    }

    public Message(String message, GameClientInfo client) {
        this.message = message;
        this.client = client;
    }

    public Message(String message, String senderName, int senderId) {
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
