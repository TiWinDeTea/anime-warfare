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

public enum UnitType {
	// No Name
	RUSSELL_JIN, // Staff
	KUROUSAGI, // LVL0
	KASUKABE_YOU, // LVL1
	KUDOU_ASUKA, //LV2
	SAKAMAKI_IZAYOI, // Great Old One

	// F-class no Baka
	YOSHII_AKIHISA, // Staff
	SAKAMOTO_YUUJI, // LVL0
	SHIMADA_MINAMI, // LVL1
	TSUCHIYA_KOUTA, // LVL2
	HIMEJI_MIZUKI, // Great Old One

	// Haiyore
	YOICHI_TAKEHIKO, // Staff
	YASAKA_MAHIRO, // LVL0
	HASUTA, // LVL1
	CTHUKO, // Great Old One
	NYARUKO, // Great Old One

	// Cool Guys
	KUBOTA_YOSHINOBU, // Staff
	ACCHAN, // LVL0
	HAYABUSA_SHOU, // LVL1
	FUKASE, // LVL2
	SAKAMOTO; // Great Old One

	private UnitAttributes attributes;
	private int defaultCost;

	public int getDefaultCost() {
		return this.defaultCost;
	}

	public UnitAttributes getAttributes() {
		return this.attributes;
	}

}
