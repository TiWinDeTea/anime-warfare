package org.tiwindetea.animewarfare.net;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class Message implements Serializable {

    private final String message;
    private final GameClientInfo client;

    /**
     * Empty message constructor (used for kryoserealizer)
     */
    public Message() {
        this.message = null;
        this.client = null;
    }

    public Message(String message) {
        this.message = message;
        this.client = null;
    }

    Message(String message, GameClientInfo client) {
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
