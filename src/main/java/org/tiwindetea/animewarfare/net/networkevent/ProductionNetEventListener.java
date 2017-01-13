package org.tiwindetea.animewarfare.net.networkevent;

import java.util.EventListener;

public interface ProductionNetEventListener extends EventListener {
	void handleProductionNetevent(ProductionNetEvent productionNetEvent);
}
