package org.tiwindetea.animewarfare.logic.units;

public class UnitBuffedCharacteristics {
	private final UnitType type;
	private int attackPoints;
	private boolean canAttack = true;
	private boolean attackable = true;

	public UnitBuffedCharacteristics(UnitType type) {
		this.type = type;
	}

	public int getAttackPoints() {
		return this.attackPoints;
	}
}
