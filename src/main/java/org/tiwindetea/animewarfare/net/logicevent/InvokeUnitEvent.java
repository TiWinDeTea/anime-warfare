package org.tiwindetea.animewarfare.net.logicevent;

import org.tiwindetea.animewarfare.logic.units.UnitType;

public class InvokeUnitEvent extends ActionEvent<InvokeUnitEventListener> {
	private final UnitType unitType;
	private final int zone;

	public InvokeUnitEvent(int playerID, UnitType unitType, int zone) {
		super(playerID);
		this.unitType = unitType;
		this.zone = zone;
	}

	@Override
	public void notify(InvokeUnitEventListener listener) {
		listener.handleInvokeUnitEvent(this);
	}

	public UnitType getUnitType() {
		return this.unitType;
	}

	public int getZone() {
		return this.zone;
	}
}
