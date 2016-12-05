package org.tiwindetea.animewarfare.logic.units.events;

import java.util.EventListener;

public interface UnitMovedEventListener extends EventListener {
	void handleUnitMoved(UnitMovedEvent event);
}
