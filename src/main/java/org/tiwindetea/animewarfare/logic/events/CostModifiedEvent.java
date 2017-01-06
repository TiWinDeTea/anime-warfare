package org.tiwindetea.animewarfare.logic.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class CostModifiedEvent implements Event<CostModifiedEventListener> {
	public enum Type {
		UNIT,
		BATTLE,
		UNIQUE_ACTION,
	}

	private final UnitType unitType;
	private final int cost;
	private final Type type;

	public CostModifiedEvent(UnitType unitType, int unitCost) {
		this.unitType = unitType;
		this.cost = unitCost;
		this.type = Type.UNIT;
	}

	public CostModifiedEvent(Type type, int cost) {
		this.type = type;
		this.cost = cost;
		this.unitType = null;
	}

	@Override
	public void notify(CostModifiedEventListener listener) {
		listener.onUnitCostModified(this);
	}

	public UnitType getUnitType() {
		return this.unitType;
	}

	public int getCost() {
		return this.cost;
	}
}
