package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;

public class LelouchAttackBuff extends Buff implements UnitMovedEventListener {
	private final BuffMask lelouchBuffMask = new BuffMask();

	public LelouchAttackBuff() {
		super(-1);

		LogicEventDispatcher.registerListener(UnitMovedEvent.class, this);
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		LogicEventDispatcher.unregisterListener(UnitMovedEvent.class, this);
	}

	@Override
	public void handleUnitMoved(UnitMovedEvent event) {
		if (event.getSource() == null) {
			if (event.getUnit().getType() == UnitType.LELOUCH) {
				event.getUnit().getUnitBuffedCharacteristics().addBuffMask(this.lelouchBuffMask);
			} else if (event.getUnit().isLevel(UnitLevel.HERO)) {
				this.lelouchBuffMask.attackPoints += 2;
			}
		} else if (event.getDestination() == null && event.getUnit().isLevel(UnitLevel.HERO)) {
			this.lelouchBuffMask.attackPoints -= 2;
		}
	}
}
