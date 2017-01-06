package org.tiwindetea.animewarfare.logic.events;

import java.util.EventListener;

public interface UnitCounterEventListener extends EventListener {
	void handleUnitEvent(UnitCounterEvent event);
}
