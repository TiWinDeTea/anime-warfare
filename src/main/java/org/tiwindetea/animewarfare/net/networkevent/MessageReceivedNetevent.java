package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.Message;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class MessageReceivedNetevent implements Event<MessageReceivedNeteventListener> {

    private final Message message;

    public MessageReceivedNetevent(String message, String senderName, int senderId) {
        this.message = new Message(message, senderName, senderId);
    }

    public MessageReceivedNetevent(Message message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message.getMessage();
    }

    public GameClientInfo getSenderInfos() {
        return this.message.getSenderInfos();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(MessageReceivedNeteventListener listener) {
        listener.handleMessage(this);
    }
}
