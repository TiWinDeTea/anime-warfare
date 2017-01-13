package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.events.ProductionEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;

public class NetProduction {
	private final ProductionEvent.Type type;
	private final GameClientInfo gameClientInfo;
	private final CapacityName name;

	// Kryo
	public NetProduction() {
		this.type = null;
		this.gameClientInfo = null;
		this.name = null;
	}

	public NetProduction(ProductionEvent event, GameClientInfo gameClientInfo) {
		this.type = event.getType();
		this.gameClientInfo = gameClientInfo;
		this.name = event.getName();
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
