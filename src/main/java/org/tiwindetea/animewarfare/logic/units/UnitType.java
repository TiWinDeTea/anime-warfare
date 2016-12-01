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

// TODO: Fill field
public enum UnitType {
	// No Name
	RUSSELL_JIN(FactionType.NO_NAME,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	KUROUSAGI(FactionType.NO_NAME,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, false, 0, 0),
			0),
	KASUKABE_YOU(FactionType.NO_NAME,
			UnitLevel.LVL1,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, false, 0, 0),
			0),
	KUDOU_ASUKA(FactionType.NO_NAME,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, false, 0, 0),
			0),
	SAKAMAKI_IZAYOI(FactionType.NO_NAME,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, true, 0, 0),
			0),

	// F-class no Baka
	YOSHII_AKIHISA(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	SAKAMOTO_YUUJI(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	SHIMADA_MINAMI(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.LVL1,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, false, 0, 0),
			0),
	TSUCHIYA_KOUTA(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	HIMEJI_MIZUKI(FactionType.F_CLASS_NO_BAKA,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, false, 0, 0),
			0),

	// Haiyore
	YOICHI_TAKEHIKO(FactionType.HAIYORE,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	YASAKA_MAHIRO(FactionType.HAIYORE,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	HASUTA(FactionType.HAIYORE,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	CTHUKO(FactionType.HAIYORE,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, false, 0, 0),
			0),
	NYARUKO(FactionType.HAIYORE,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.FEMALE, false, 0, 0),
			0),

	// Cool Guys
	KUBOTA_YOSHINOBU(FactionType.COOL_GUYS,
			UnitLevel.MASCOT,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	ACCHAN(FactionType.COOL_GUYS,
			UnitLevel.LVL0,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	HAYABUSA_SHOU(FactionType.COOL_GUYS,
			UnitLevel.LVL1,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	FUKASE(FactionType.COOL_GUYS,
			UnitLevel.LVL2,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0),
	SAKAMOTO(FactionType.COOL_GUYS,
			UnitLevel.HERO,
			new UnitBasicCharacteristics(UnitBasicCharacteristics.Gender.MALE, false, 0, 0),
			0);

	private final FactionType defaultFaction;
	private final UnitBasicCharacteristics attributes;
	private final int defaultCost;
	private final UnitLevel unitLevel;

	UnitType(FactionType defaultFaction, UnitLevel unitLevel, UnitBasicCharacteristics attributes, int defaultCost) {
		this.defaultFaction = defaultFaction;
		this.unitLevel = unitLevel;
		this.attributes = attributes;
		this.defaultCost = defaultCost;
	}

	public int getDefaultCost() {
		return this.defaultCost;
	}

	public UnitBasicCharacteristics getAttributes() {
		return this.attributes;
	}

	public FactionType getDefaultFaction() {
		return this.defaultFaction;
	}

	public UnitLevel getUnitLevel() {
		return this.unitLevel;
	}
}
