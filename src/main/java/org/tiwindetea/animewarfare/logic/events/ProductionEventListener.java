package org.tiwindetea.animewarfare.logic.events;

import java.util.EventListener;

public interface ProductionEventListener extends EventListener {
	void onProductionActivated(ProductionEvent event);

	void onProductionDisabled(ProductionEvent event);
}
