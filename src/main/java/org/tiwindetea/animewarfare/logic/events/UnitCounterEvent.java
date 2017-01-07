package org.tiwindetea.animewarfare.logic.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class UnitCounterEvent implements Event<UnitCounterEventListener> {
	public enum Type {
		ADDED,
		REMOVED
	}

	private final Type type;
	private final FactionType faction;
	private final UnitType unitType;
	private final int unitId;

	public UnitCounterEvent(Type type, FactionType faction, UnitType unitType, int unitId) {
		this.type = type;
		this.faction = faction;
		this.unitType = unitType;
		this.unitId = unitId;
	}

	public Type getType() {
		return this.type;
	}

	public FactionType getFaction() {
		return this.faction;
	}

	public UnitType getUnitType() {
		return this.unitType;
	}

	public int getUnitId() {
		return this.unitId;
	}

	@Override
	public void notify(UnitCounterEventListener listener) {
		listener.handleUnitEvent(this);
	}
}
