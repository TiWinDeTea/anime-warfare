package org.tiwindetea.animewarfare.logic.units;

public class UnitAttributes {
	private final Gender gender;
	private final boolean isHero;
	private final int baseAttackPoints;
	private final int baseMovementPoints;

	UnitAttributes(Gender gender, boolean isHero, int baseAttackPoints, int baseMovementPoints) {
		this.gender = gender;
		this.isHero = isHero;
		this.baseAttackPoints = baseAttackPoints;
		this.baseMovementPoints = baseMovementPoints;
	}

	public Gender getGender() {
		return this.gender;
	}

	public boolean isHero() {
		return this.isHero;
	}

	public int getBaseAttackPoints() {
		return this.baseAttackPoints;
	}

	public int getBaseMovementPoints() {
		return this.baseMovementPoints;
	}
}
