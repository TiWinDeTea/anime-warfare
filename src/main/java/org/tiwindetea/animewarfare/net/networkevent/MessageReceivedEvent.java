package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;

/**
 * Created by Lucas on 20/11/2016.
 */
public class MessageReceivedEvent implements Event<MessageReceivedEventListener> {

    private final String message;

    public MessageReceivedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void notify(MessageReceivedEventListener listener) {
        listener.handleMessage(this);
    }
}
