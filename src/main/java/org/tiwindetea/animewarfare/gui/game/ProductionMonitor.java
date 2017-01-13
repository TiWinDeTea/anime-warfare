package org.tiwindetea.animewarfare.gui.game;

import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.events.ProductionEvent;
import org.tiwindetea.animewarfare.net.networkevent.ProductionNetEvent;
import org.tiwindetea.animewarfare.net.networkevent.ProductionNetEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductionMonitor implements ProductionNetEventListener {
	private final static ProductionMonitor MONITOR = new ProductionMonitor();

	private final List<CapacityName> activatedCapacities = new ArrayList<>();

	public static ProductionMonitor getProductionMonitor() {
		return MONITOR;
	}

	@Override
	public void handleProductionNetevent(ProductionNetEvent productionNetEvent) {
		if (MainApp.getGameClient().getClientInfo().equals(productionNetEvent.getGameClientInfo())) {
			if (productionNetEvent.getType() == ProductionEvent.Type.ACTIVATED) {
				this.activatedCapacities.add(productionNetEvent.getName());
			} else {
				this.activatedCapacities.remove(productionNetEvent.getName());
			}
		}
	}

	public boolean hasCapacity(CapacityName name) {
		return this.activatedCapacities.contains(name);
	}
}
