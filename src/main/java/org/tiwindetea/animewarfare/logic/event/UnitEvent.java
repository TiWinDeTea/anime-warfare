package org.tiwindetea.animewarfare.logic.event;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class UnitEvent implements Event<UnitEventListener> {
	public enum Type {
		ADDED,
		REMOVED
	}

	private final Type type;
	private final int unitID;
	private final int zoneID;
	private final FactionType faction;
	private final UnitType unitType;

	public UnitEvent(Type type, int unitID, int zoneID, FactionType faction, UnitType unitType) {
		this.type = type;
		this.unitID = unitID;
		this.zoneID = zoneID;
		this.faction = faction;
		this.unitType = unitType;
	}

	public Type getType() {
		return this.type;
	}

	public int getUnitID() {
		return this.unitID;
	}

	public int getZoneID() {
		return this.zoneID;
	}

	public FactionType getFaction() {
		return this.faction;
	}

	public UnitType getUnitType() {
		return this.unitType;
	}

	@Override
	public void notify(UnitEventListener listener) {
		listener.handleUnitEvent(this);
	}
}
