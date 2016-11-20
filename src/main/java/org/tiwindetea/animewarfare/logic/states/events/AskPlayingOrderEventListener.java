package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface AskPlayingOrderEventListener extends EventListener {
	void askPlayingOrder(AskPlayingOrderEvent event);
}
