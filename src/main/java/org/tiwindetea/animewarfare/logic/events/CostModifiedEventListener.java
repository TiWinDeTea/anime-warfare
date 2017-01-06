package org.tiwindetea.animewarfare.logic.events;

import java.util.EventListener;

public interface CostModifiedEventListener extends EventListener {
	void onUnitCostModified(CostModifiedEvent event);
}
