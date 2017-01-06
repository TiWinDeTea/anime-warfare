package org.tiwindetea.animewarfare.logic.event;

import java.util.EventListener;

public interface CostModifiedEventListener extends EventListener {
	void onUnitCostModified(CostModifiedEvent event);
}
