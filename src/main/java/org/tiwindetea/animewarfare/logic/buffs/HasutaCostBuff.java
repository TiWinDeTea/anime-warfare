package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.CostModifier;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Mask;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class HasutaCostBuff extends Buff implements UnitCounterEventListener {
	private final Mask costMask = new Mask(0);
	private final CostModifier modifier;

	public HasutaCostBuff(CostModifier modifier) {
		super(-1);
		this.modifier = modifier;

		LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
	}

	@Override
	public void handleUnitEvent(UnitCounterEvent event) {
		if (event.getUnitType() == UnitType.HASUTA) {
			this.modifier.removeUnitCost(UnitType.HASUTA, this.costMask);
			if (event.getType() == UnitCounterEvent.Type.ADDED) {
				--(this.costMask.value);
			} else {
				++(this.costMask.value);
			}
			this.modifier.addUnitCost(UnitType.HASUTA, this.costMask);
		}
	}
}
