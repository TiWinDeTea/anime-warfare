package org.tiwindetea.animewarfare.logic.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;

public class ProductionEvent implements Event<ProductionEventListener> {
	public enum Type {
		ACTIVATED,
		DISABLED
	}

	private final Type type;
	private final int playerID;
	private final CapacityName name;

	public ProductionEvent(Type type, int playerID, CapacityName name) {
		this.type = type;
		this.playerID = playerID;
		this.name = name;
	}

	@Override
	public void notify(ProductionEventListener listener) {
		if (this.type == Type.ACTIVATED) {
			listener.onProductionActivated(this);
		} else {
			listener.onProductionDisabled(this);
		}
	}

	public Type getType() {
		return this.type;
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public CapacityName getName() {
		return this.name;
	}
}
