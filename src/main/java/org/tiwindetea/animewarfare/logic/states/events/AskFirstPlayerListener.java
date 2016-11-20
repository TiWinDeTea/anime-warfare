package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface AskFirstPlayerListener extends EventListener {
	void askFirstPlayerEvent(AskFirstPlayerEvent event);
}
