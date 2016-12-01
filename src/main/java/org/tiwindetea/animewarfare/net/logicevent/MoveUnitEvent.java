package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.units.UnitType;

import java.util.Collections;
import java.util.List;

public class MoveUnitEvent implements Event<MoveUnitEventListener> {
	private final int playerID;
	private final List<UnitType> unit;
	private final List<Integer> sourceZone;
	private final List<Integer> destinationZone;

	public MoveUnitEvent(int playerID, List<UnitType> unit, List<Integer> sourceZone, List<Integer> destinationZone) {
		if (sourceZone.size() != destinationZone.size()
				|| sourceZone.size() != unit.size()) {
			throw new IllegalArgumentException("Those lists should have the same size.");
		}

		this.playerID = playerID;
		this.unit = Collections.unmodifiableList(unit);
		this.sourceZone = Collections.unmodifiableList(sourceZone);
		this.destinationZone = Collections.unmodifiableList(destinationZone);
	}

	@Override
	public void notify(MoveUnitEventListener listener) {
		listener.handleMoveUnitEvent(this);
	}

	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * @return an unmodifiable list as specified in {@link Collections#unmodifiableList(List)}
	 */
	public List<UnitType> getUnits() {
		return this.unit;
	}

	/**
	 * @return an unmodifiable list as specified in {@link Collections#unmodifiableList(List)}
	 */
	public List<Integer> getSourceZones() {
		return this.sourceZone;
	}

	/**
	 * @return an unmodifiable list as specified in {@link Collections#unmodifiableList(List)}
	 */
	public List<Integer> getDestinationZones() {
		return this.destinationZone;
	}
}
