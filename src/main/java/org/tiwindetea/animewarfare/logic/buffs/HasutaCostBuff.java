package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.CostModifier;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Mask;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class HasutaCostBuff extends Buff implements UnitCounterEventListener {
	private final Mask costModifier = new Mask(0);

	public HasutaCostBuff(CostModifier modifier) {
		super(-1);
		modifier.addUnitCost(UnitType.HASUTA, this.costModifier);

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
			if (event.getType() == UnitCounterEvent.Type.ADDED) {
				--(this.costModifier.value);
			} else {
				++(this.costModifier.value);
			}
		}
	}
}
