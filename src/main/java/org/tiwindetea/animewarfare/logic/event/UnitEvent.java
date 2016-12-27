package org.tiwindetea.animewarfare.logic.event;

import org.lomadriel.lfc.event.Event;

public class UnitEvent implements Event<UnitEventListener> {
	public enum Type {
		ADDED,
		REMOVED
	}

	private final Type type;
	private final int unitID;
	private final int zoneID;

	public UnitEvent(Type type, int unitID, int zoneID) {
		this.type = type;
		this.unitID = unitID;
		this.zoneID = zoneID;
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

	@Override
	public void notify(UnitEventListener listener) {
		listener.handleUnitEvent(this);
	}
}
