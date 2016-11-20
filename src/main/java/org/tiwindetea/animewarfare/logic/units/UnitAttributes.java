////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

package org.tiwindetea.animewarfare.logic.units;

public class UnitAttributes {
	public enum Gender {
		MALE,
		FEMALE
	}

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
