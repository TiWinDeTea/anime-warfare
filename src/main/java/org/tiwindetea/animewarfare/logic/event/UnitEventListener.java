package org.tiwindetea.animewarfare.logic.event;

import java.util.EventListener;

public interface UnitEventListener extends EventListener {
	void handleUnitEvent(UnitEvent event);
}
