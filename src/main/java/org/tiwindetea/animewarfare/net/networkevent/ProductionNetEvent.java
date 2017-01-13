package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.events.ProductionEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.NetProduction;

public class ProductionNetEvent implements Event<ProductionNetEventListener> {
	private final ProductionEvent.Type type;
	private final GameClientInfo gameClientInfo;
	private final CapacityName name;

	public ProductionNetEvent(NetProduction netProduction) {
		this.type = netProduction.getType();
		this.gameClientInfo = netProduction.getGameClientInfo();
		this.name = netProduction.getName();
	}

	@Override
	public void notify(ProductionNetEventListener listener) {
		listener.handleProductionNetevent(this);
	}

	public ProductionEvent.Type getType() {
		return this.type;
	}

	public GameClientInfo getGameClientInfo() {
		return this.gameClientInfo;
	}

	public CapacityName getName() {
		return this.name;
	}
}
