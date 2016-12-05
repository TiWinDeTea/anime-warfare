package org.tiwindetea.animewarfare.logic.units.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.units.Unit;

public class UnitMovedEvent implements Event<UnitMovedEventListener> {
	private final Unit unit;
	private final Zone source;
	private final Zone destination;

	public UnitMovedEvent(Unit unit, Zone source, Zone destination) {
		this.unit = unit;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public void notify(UnitMovedEventListener listener) {
		listener.handleUnitMoved(this);
	}

	public Unit getUnit() {
		return this.unit;
	}

	public Zone getSource() {
		return this.source;
	}

	public Zone getDestination() {
		return this.destination;
	}
}
