package org.tiwindetea.animewarfare.net.logicevent;

import org.tiwindetea.animewarfare.logic.units.UnitType;

public class ColdBloodUnitTypeChoiceEvent extends ActionEvent<ColdBloodUnitTypeChoiceEventListener> {
	private final UnitType unitType;

	public ColdBloodUnitTypeChoiceEvent(int playerID, UnitType unitType) {
		super(playerID);

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
