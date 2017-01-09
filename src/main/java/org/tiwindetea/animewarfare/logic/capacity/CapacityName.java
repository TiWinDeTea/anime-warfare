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

/*
 * @author Jérôme BOULMIER
 * @author Benoît CORTIER
 */
public enum CapacityName {
	// No Name
	CLEMENCY(FactionType.NO_NAME, CapacityType.POST_BATTLE, new CapacityCost("Nombre d'unités à réconforter")),// TODO: externalize
	LOAN(FactionType.NO_NAME, CapacityType.ACTION, new CapacityCost(1)),
	RESTRUCTURATION(FactionType.NO_NAME, CapacityType.ACTION, new CapacityCost(0)),
	GENIUS_KIDNAPPER(FactionType.NO_NAME, CapacityType.ACTION, new CapacityCost(1)),
	FURY(FactionType.NO_NAME, CapacityType.PRE_BATTLE, new CapacityCost("1,5 SP par unité.")),
	COLD_BLOOD(FactionType.NO_NAME, CapacityType.ACTION, new CapacityCost(4)),

	// Kurousagi
	FOR_YOU_I_WILL_INSULT(null, CapacityType.PERMANENT, new CapacityCost(0)),

	// F-Class no Baka
	BAD_BOOK(FactionType.F_CLASS_NO_BAKA, CapacityType.ACTION, new CapacityCost(1)),
	FORCED_RETREAT(FactionType.F_CLASS_NO_BAKA, CapacityType.PRE_BATTLE, new CapacityCost(1)),
	BACK_STAB(FactionType.F_CLASS_NO_BAKA, CapacityType.POST_BATTLE, new CapacityCost(1)),
	FLYING_STUDIO(FactionType.F_CLASS_NO_BAKA, CapacityType.ACTION, new CapacityCost(1)),
	DEAF_EAR(FactionType.F_CLASS_NO_BAKA, CapacityType.ACTION, new CapacityCost("1 SP par unité non héros, 2 SP par héros.")),
	SHONEN_JUMP_APPEARANCE(FactionType.F_CLASS_NO_BAKA, CapacityType.MARKETING_ACTION, new CapacityCost(0)),

	// Himeji
	DIPLOMAT(null, CapacityType.POST_BATTLE, new CapacityCost(3)),

	// Haiyore
	DILEMMA(FactionType.HAIYORE, CapacityType.PRE_BATTLE, new CapacityCost(0)),
	BAD_LUCK(FactionType.HAIYORE, CapacityType.POST_BATTLE, new CapacityCost(1)),
	MAGIC_MOVEMENT(FactionType.HAIYORE, CapacityType.ACTION, new CapacityCost("1,25 SP par unité.")),
	CLOWN_ARTIST(FactionType.HAIYORE, CapacityType.PRE_BATTLE, new CapacityCost(2)),
	MORE_FANS(FactionType.HAIYORE, CapacityType.ACTION, new CapacityCost(3)),
	HIDING_BUSH(FactionType.HAIYORE, CapacityType.ACTION, new CapacityCost("1 SP par non héros, 2 SP par héros.")),

	// Nyaruko
	SHARP_TONGUE(null, CapacityType.DURING_BATTLE, new CapacityCost(1)),
	// Ctuko
	WITH_MY_BELOVED(null, CapacityType.PERMANENT, new CapacityCost(0)),

	// The Black Knights
	STONE_HEART(FactionType.THE_BLACK_KNIGHTS, CapacityType.PERMANENT, new CapacityCost(0)),
	NEGOTIATOR(FactionType.THE_BLACK_KNIGHTS, CapacityType.MARKETING_ACTION, new CapacityCost(3)),
	MARKET_FLOODING(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION, new CapacityCost(3)),
	GENERAL_ALARM(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION, new CapacityCost(2)),
	UNDERCOVER_AGENT(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION, new CapacityCost(3)),
	HOLOCAUST(FactionType.THE_BLACK_KNIGHTS, CapacityType.ACTION, new CapacityCost(2)),

	// Lelouch
	BACK_TO_SKETCH(null, CapacityType.PRE_BATTLE, new CapacityCost(1));

	private final FactionType faction;
	private final CapacityType type;
	private final CapacityCost cost;

	CapacityName(FactionType faction, CapacityType type, CapacityCost cost) {
		this.faction = faction;
		this.type = type;
		this.cost = cost;
	}

	public FactionType getFaction() {
		return this.faction;
	}

	public CapacityType getType() {
		return this.type;
	}

	public int getStaffCost() {
		return this.cost.getStaffCost();
	}

	public boolean hasStaffCost() {
		return this.cost.hasStaffCost();
	}

	public String getCostDescription() {
		return this.cost.getCostDescription();
	}
}
