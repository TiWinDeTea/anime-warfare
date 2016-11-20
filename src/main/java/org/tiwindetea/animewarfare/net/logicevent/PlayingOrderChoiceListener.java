package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface PlayingOrderChoiceListener extends EventListener {
	void handlePlayingOrder(PlayingOrderChoiceEvent event);
}
