package org.tiwindetea.animewarfare.net.networkevent;

import java.util.EventListener;

/**
 * Created by Lucas on 20/11/2016.
 */
public interface MessageReceivedEventListener extends EventListener {
    void handleMessage(MessageReceivedEvent message);
}
