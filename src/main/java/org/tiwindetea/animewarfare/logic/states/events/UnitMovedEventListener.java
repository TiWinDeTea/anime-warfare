package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface UnitMovedEventListener extends EventListener {
	void handleUnitMoved(UnitMovedEvent event);
}
