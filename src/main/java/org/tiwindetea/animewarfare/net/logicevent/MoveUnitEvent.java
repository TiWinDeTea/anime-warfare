package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class MoveUnitEvent implements Event<MoveUnitEventListener> {
	private final int playerID;
	private final UnitType unit;
	private final int sourceZone;
	private final int destinationZone;

	public MoveUnitEvent(int playerID, UnitType unit, int sourceZone, int destinationZone) {
		this.playerID = playerID;
		this.unit = unit;
		this.sourceZone = sourceZone;
		this.destinationZone = destinationZone;
	}

	@Override
	public void notify(MoveUnitEventListener listener) {
		listener.handleMoveUnitEvent(this);
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public UnitType getUnit() {
		return this.unit;
	}

	public int getSourceZone() {
		return this.sourceZone;
	}

	public int getDestinationZone() {
		return this.destinationZone;
	}
}
