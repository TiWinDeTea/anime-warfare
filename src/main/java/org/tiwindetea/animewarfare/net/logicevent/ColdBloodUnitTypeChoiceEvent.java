package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class ColdBloodUnitTypeChoiceEvent implements Event<ColdBloodUnitTypeChoiceEventListener> {
	private final UnitType unitType;

	public ColdBloodUnitTypeChoiceEvent(UnitType unitType) {
		this.unitType = unitType;
	}

	@Override
	public void notify(ColdBloodUnitTypeChoiceEventListener listener) {
		listener.handleUnitTypeChoice(this);
	}

	public UnitType getUnitType() {
		return this.unitType;
	}
}
