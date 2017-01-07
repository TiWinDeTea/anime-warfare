package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.units.Unit;

import java.util.Set;

public class HidingInTheBushBuff extends Buff {
	private final Set<Unit> units;
	private final BuffMask mask;

	public HidingInTheBushBuff(Set<Unit> units) {
		super(3);

		this.units = units;
		this.mask = new BuffMask();
		this.mask.visible = true;

		for (Unit unit : units) {
			unit.getUnitBuffedCharacteristics().addBuffMask(this.mask);
		}

	}

	@Override
	boolean isActionBuff() {
		return true;
	}

	@Override
	void destroy() {
		for (Unit unit : this.units) {
			unit.getUnitBuffedCharacteristics().removeBuffMask(this.mask);
		}
	}
}
