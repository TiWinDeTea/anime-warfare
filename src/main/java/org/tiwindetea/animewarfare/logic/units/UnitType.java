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

import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.util.PrettyFormat;

// TODO: Fill field
public enum UnitType {
	// No Name
	RUSSELL_JIN(FactionType.NO_NAME,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, 0),
			1, 6),
	KUROUSAGI(FactionType.NO_NAME,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 1),
			1, 4),
	KASUKABE_YOU(FactionType.NO_NAME,
			UnitLevel.LVL1,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 2),
			2, 2),
	KUDOU_ASUKA(FactionType.NO_NAME,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 3),
			3, 2),
	SAKAMAKI_IZAYOI(FactionType.NO_NAME,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, 6),
			10, 1),

	// F-class no Baka
	YOSHII_AKIHISA(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, 0),
			1, 6),
	SAKAMOTO_YUUJI(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, 0.5f),
			1, 2),
	SHIMADA_MINAMI(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.LVL1,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 1),
			2, 4),
	TSUCHIYA_KOUTA(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, 2),
			3, 3),
	HIMEJI_MIZUKI(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE,
					UnitBasicCharacteristics.MAGIC_NBR_STUDIO_MASCOT),
			8, 1),

	// Haiyore
	KUREI_TAMAO(FactionType.HAIYORE,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 0),
			1, 6),
	YASAKA_MAHIRO(FactionType.HAIYORE,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, 1),
			1, 4),
	HASUTA(FactionType.HAIYORE,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE,
					UnitBasicCharacteristics.MAGIC_NBR_NBR_HASUTA_NOT_IN_GAME),
			4, 4),
	CTHUKO(FactionType.HAIYORE,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 3),
			6, 1),
	NYARUKO(FactionType.HAIYORE,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE,
					UnitBasicCharacteristics.MAGIC_NBR_ENEMY_FANS),
			6, 1),

	// The Black Knights
	NUNNALLY(FactionType.THE_BLACK_KNIGHTS,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 0),
			1, 6),
	KALLEN(FactionType.THE_BLACK_KNIGHTS,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 1),
			2, 4),
	SUZAKU(FactionType.THE_BLACK_KNIGHTS,
			UnitLevel.LVL1,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, 2),
			3, 3),
	CC(FactionType.THE_BLACK_KNIGHTS,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, 3),
			4, 2),
	LELOUCH(FactionType.THE_BLACK_KNIGHTS,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE,
					UnitBasicCharacteristics.MAGIC_NBR_NBR_ENEMY_HEROES_IN_GAME),
			6, 1);

	private final FactionType defaultFaction;
	private final UnitLevel unitLevel;
	private final UnitBasicCharacteristics unitBasicCharacteristics;
	private final int defaultCost;
	private final int maxNumber;
	private final String name;

	UnitType(FactionType defaultFaction,
	         UnitLevel unitLevel,
	         UnitBasicCharacteristics unitBasicCharacteristics,
	         int defaultCost, int maxNumber) {
		this.defaultFaction = defaultFaction;
		this.unitLevel = unitLevel;
		this.unitBasicCharacteristics = unitBasicCharacteristics;
		this.defaultCost = defaultCost;
		this.maxNumber = maxNumber;
		this.name = PrettyFormat.enumToPretty(super.toString());
	}

	public int getDefaultCost() {
		return this.defaultCost;
	}

	public FactionType getDefaultFaction() {
		return this.defaultFaction;
	}

	public UnitLevel getUnitLevel() {
		return this.unitLevel;
	}

	public boolean isLevel(UnitLevel level) {
		return this.unitLevel == level;
	}

	public UnitBasicCharacteristics getUnitBasicCharacteristics() {
		return this.unitBasicCharacteristics;
	}

	public int getMaxNumber() {
		return this.maxNumber;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
