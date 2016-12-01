package org.tiwindetea.animewarfare.logic.units;

public class UnitBuffedCharacteristics {
	private final UnitType type;
	private int attackPoint;
	private boolean canAttack = true;
	private boolean attackable = true;

	public UnitBuffedCharacteristics(UnitType type) {
		this.type = type;
	}
}
