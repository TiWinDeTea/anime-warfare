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

package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.FactionType;

public enum CapacityName {
	// No Name
	CLEMENCY(FactionType.NO_NAME, CapacityType.POST_BATTLE),
	LOAN(FactionType.NO_NAME, CapacityType.ACTION),
	RESTRUCTURATION(FactionType.NO_NAME, CapacityType.ACTION),
	GENIUS_KIDNAPPER(FactionType.NO_NAME, CapacityType.ACTION),
	FURY(FactionType.NO_NAME, CapacityType.PRE_BATTLE),
	COLD_BLOOD(FactionType.NO_NAME, CapacityType.ACTION),

	// Kurousagi
	FOR_YOU_I_WILL_INSULT(null, CapacityType.ACTION),

	// F-Class no Baka
	BAD_BOOK(FactionType.F_CLASS_NO_BAKA, CapacityType.ACTION),
	FORCED_RETREAT(FactionType.F_CLASS_NO_BAKA, CapacityType.PRE_BATTLE),
	BACK_STAB(FactionType.F_CLASS_NO_BAKA, CapacityType.POST_BATTLE),
	FLYING_STUDIO(FactionType.F_CLASS_NO_BAKA, CapacityType.ACTION),
	DEAF_EAR(FactionType.F_CLASS_NO_BAKA, CapacityType.ACTION),
	SHONEN_JUMP_APPEARANCE(FactionType.F_CLASS_NO_BAKA, CapacityType.MARKETING_ACTION),

	// Himeji
	DIPLOMAT(null, CapacityType.POST_BATTLE),

	// Haiyore
	DILEMMA(FactionType.HAIYORE, CapacityType.PRE_BATTLE),
	BAD_LUCK(FactionType.HAIYORE, CapacityType.POST_BATTLE),
	MAGIC_MOVEMENT(FactionType.HAIYORE, CapacityType.ACTION),
	ARTISTE_CLOWN(FactionType.HAIYORE, CapacityType.PRE_BATTLE),
	MORE_FANS(FactionType.HAIYORE, CapacityType.ACTION),
	HIDING_BUSH(FactionType.HAIYORE, CapacityType.ACTION),

	// Nyaruko
	SHARP_TONGUE(null, CapacityType.DURING_BATTLE),
	// Ctuko
	WITH_MY_BELOVED(null, CapacityType.PERMANENT),

	// The Black Knights
	STONE_HEART(FactionType.THE_BLACK_KNIGHTS, CapacityType.POST_BATTLE),
	NEGOTIATOR(FactionType.THE_BLACK_KNIGHTS, CapacityType.MARKETING_ACTION),
	MARKET_FLOODING(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION),
	GENERAL_ALARM(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION),
	UNDERCOVER_AGENT(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION),
	HOLOCAUST(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION),

	// Lelouch
	BACK_TO_SKETCH(null, CapacityType.PRE_BATTLE);

	private final FactionType faction;
	private final CapacityType type;

	CapacityName(FactionType faction, CapacityType type) {
		this.faction = faction;
		this.type = type;
	}

	public FactionType getFaction() {
		return this.faction;
	}

	public CapacityType getType() {
		return this.type;
	}
}
