package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.Message;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class MessageReceivedEvent implements Event<MessageReceivedEventListener> {

    private final String message;

    public MessageReceivedEvent(String message) {
        this.message = message;
    }

    public MessageReceivedEvent(Message message) {
        this.message = message.getMessage();
    }

    public String getMessage() {
        return this.message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(MessageReceivedEventListener listener) {
        listener.handleMessage(this);
    }
}
