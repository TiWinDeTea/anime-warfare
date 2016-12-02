package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class InvokeUnitEvent implements Event<InvokeUnitEventListener> {
	private final int playerID;
	private final UnitType unitType;
	private final int zone;

	public InvokeUnitEvent(int playerID, UnitType unitType, int zone) {
		this.playerID = playerID;
		this.unitType = unitType;
		this.zone = zone;
	}

	@Override
	public void notify(InvokeUnitEventListener listener) {
		listener.handleInvokeUnitEvent(this);
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public UnitType getUnitType() {
		return this.unitType;
	}

	public int getZone() {
		return this.zone;
	}
}
