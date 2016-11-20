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
	RUSSELL_JIN(FactionType.NO_NAME, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // Acolyte
	KUROUSAGI(FactionType.NO_NAME, new UnitAttributes(UnitAttributes.Gender.FEMALE, false, 0, 0), 0), // LVL0
	KASUKABE_YOU(FactionType.NO_NAME, new UnitAttributes(UnitAttributes.Gender.FEMALE, false, 0, 0), 0), // LVL1
	KUDOU_ASUKA(FactionType.NO_NAME, new UnitAttributes(UnitAttributes.Gender.FEMALE, false, 0, 0), 0), //LV2
	SAKAMAKI_IZAYOI(FactionType.NO_NAME, new UnitAttributes(UnitAttributes.Gender.MALE, true, 0, 0), 0), // Great Old One

	// F-class no Baka
	YOSHII_AKIHISA(FactionType.F_CLASS_NO_BAKA, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // Acolyte
	SAKAMOTO_YUUJI(FactionType.F_CLASS_NO_BAKA, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // LVL0
	SHIMADA_MINAMI(FactionType.F_CLASS_NO_BAKA, new UnitAttributes(UnitAttributes.Gender.FEMALE, false, 0, 0), 0), // LVL1
	TSUCHIYA_KOUTA(FactionType.F_CLASS_NO_BAKA, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // LVL2
	HIMEJI_MIZUKI(FactionType.F_CLASS_NO_BAKA, new UnitAttributes(UnitAttributes.Gender.FEMALE, false, 0, 0), 0), // Great Old One

	// Haiyore
	YOICHI_TAKEHIKO(FactionType.HAIYORE, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // Acolyte
	YASAKA_MAHIRO(FactionType.HAIYORE, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // LVL0
	HASUTA(FactionType.HAIYORE, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // LVL1
	CTHUKO(FactionType.HAIYORE, new UnitAttributes(UnitAttributes.Gender.FEMALE, false, 0, 0), 0), // Great Old One
	NYARUKO(FactionType.HAIYORE, new UnitAttributes(UnitAttributes.Gender.FEMALE, false, 0, 0), 0), // Great Old One

	// Cool Guys
	KUBOTA_YOSHINOBU(FactionType.COOL_GUYS, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // Acolyte
	ACCHAN(FactionType.COOL_GUYS, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // LVL0
	HAYABUSA_SHOU(FactionType.COOL_GUYS, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // LVL1
	FUKASE(FactionType.COOL_GUYS, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0), // LVL2
	SAKAMOTO(FactionType.COOL_GUYS, new UnitAttributes(UnitAttributes.Gender.MALE, false, 0, 0), 0); // Great Old One

	private FactionType faction;
	private UnitAttributes attributes;
	private int defaultCost;

	UnitType(FactionType faction, UnitAttributes attributes, int defaultCost) {
		this.faction = faction;
		this.attributes = attributes;
		this.defaultCost = defaultCost;
	}

	public int getDefaultCost() {
		return this.defaultCost;
	}

	public UnitAttributes getAttributes() {
		return this.attributes;
	}

	public FactionType getFaction() {
		return this.faction;
	}
}
