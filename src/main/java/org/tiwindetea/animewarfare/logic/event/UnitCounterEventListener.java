package org.tiwindetea.animewarfare.logic.event;

import java.util.EventListener;

public interface UnitCounterEventListener extends EventListener {
	void handleUnitEvent(UnitCounterEvent event);
}
