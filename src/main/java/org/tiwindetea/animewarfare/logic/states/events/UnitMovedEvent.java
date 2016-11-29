package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class UnitMovedEvent implements Event<UnitMovedEventListener> {
	private final UnitType type;
	private final int source;
	private final int destination;

	public UnitMovedEvent(UnitType type, int source, int destination) {
		this.type = type;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void notify(UnitMovedEventListener listener) {
		listener.handleUnitMoved(this);
	}

	public UnitType getType() {
		return this.type;
	}

	public int getSource() {
		return this.source;
	}

	public int getDestination() {
		return this.destination;
	}
}
