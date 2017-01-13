package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.events.NumberOfFansChangedEvent;
import org.tiwindetea.animewarfare.logic.events.NumberOfFansChangedEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;

public class NyarukoAttackBuff extends Buff implements UnitMovedEventListener, NumberOfFansChangedEventListener {
	private final BuffMask buffMask = new BuffMask();

	public NyarukoAttackBuff() {
		super(-1);

		LogicEventDispatcher.registerListener(UnitMovedEvent.class, this);
		LogicEventDispatcher.registerListener(NumberOfFansChangedEvent.class, this);
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		LogicEventDispatcher.unregisterListener(UnitMovedEvent.class, this);
		LogicEventDispatcher.unregisterListener(NumberOfFansChangedEvent.class, this);
	}

	@Override
	public void handleUnitMoved(UnitMovedEvent event) {
		if (event.getSource() == null && event.getUnit().getType() == UnitType.NYARUKO) {
			event.getUnit().getUnitBuffedCharacteristics().addBuffMask(this.buffMask);
		}
	}

	@Override
	public void handleNumberOfFansChanged(NumberOfFansChangedEvent event) {
		this.buffMask.attackPoints = (event.getNewVal() + 1) / 2;
	}
}
