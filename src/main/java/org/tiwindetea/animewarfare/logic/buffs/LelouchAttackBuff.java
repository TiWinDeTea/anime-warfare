package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class LelouchAttackBuff extends Buff implements UnitCounterEventListener {
	private final BuffMask lelouchBuffMask = new BuffMask();

	private final int nbOfPlayers;

	public LelouchAttackBuff(int nbOfPlayers) {
		super(-1);
		this.nbOfPlayers = nbOfPlayers;

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
		if (event.getType() == UnitCounterEvent.Type.ADDED) {
			if (event.getUnitType() == UnitType.LELOUCH) {
				event.getUnit().getUnitBuffedCharacteristics().addBuffMask(this.lelouchBuffMask);
			} else if (event.getUnitType().isLevel(UnitLevel.HERO)) {
				this.lelouchBuffMask.attackPoints += Math.ceil(8 / this.nbOfPlayers);
			}
		} else if (event.getUnitType().isLevel(UnitLevel.HERO) && event.getUnitType() != UnitType.LELOUCH) {
			this.lelouchBuffMask.attackPoints -= Math.ceil(8 / this.nbOfPlayers);
		}
	}
}
