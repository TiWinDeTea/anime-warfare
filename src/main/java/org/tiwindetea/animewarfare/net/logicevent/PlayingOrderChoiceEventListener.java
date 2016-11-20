package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface PlayingOrderChoiceEventListener extends EventListener {
	void handlePlayingOrder(PlayingOrderChoiceEvent event);
}
