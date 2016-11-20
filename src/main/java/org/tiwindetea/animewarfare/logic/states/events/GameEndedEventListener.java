package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface GameEndedEventListener extends EventListener {
	void handleGameEndedEvent(GameEndedEvent gameEndedEvent);
}
