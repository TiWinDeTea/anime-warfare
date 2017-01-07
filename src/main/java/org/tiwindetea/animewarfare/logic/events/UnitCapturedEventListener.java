package org.tiwindetea.animewarfare.logic.events;

import java.util.EventListener;

public interface UnitCapturedEventListener extends EventListener {
	void onUnitCaptured(UnitCapturedEvent event);
}
