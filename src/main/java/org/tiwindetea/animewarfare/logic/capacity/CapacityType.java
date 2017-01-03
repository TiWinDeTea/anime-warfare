package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.FactionType;

public enum CapacityType {
	// No Name
	CLEMENCY(FactionType.NO_NAME),
	LOAN(FactionType.NO_NAME),
	RESTRUCTURATION(FactionType.NO_NAME),
	GENIUS_KIDNAPPER(FactionType.NO_NAME),
	FURY(FactionType.NO_NAME),
	COLD_BLOOD(FactionType.NO_NAME),

	// Kurousagi
	FOR_YOU_I_WILL_INSULT(null),

	// F-Class no Baka
	BAD_BOOK(FactionType.F_CLASS_NO_BAKA),
	FORCED_RETREAT(FactionType.F_CLASS_NO_BAKA),
	BACK_STAB(FactionType.F_CLASS_NO_BAKA),
	FLYING_STUDIO(FactionType.F_CLASS_NO_BAKA),
	DEAF_EAR(FactionType.F_CLASS_NO_BAKA),
	SHONEN_JUMP_APPEARANCE(FactionType.F_CLASS_NO_BAKA),

	// Himeji
	DIPLOMAT(null),

	// Haiyore
	DILEMMA(FactionType.HAIYORE),
	BAD_LUCK(FactionType.HAIYORE),
	MAGIC_MOVEMENT(FactionType.HAIYORE),
	ARTISTE_CLOWN(FactionType.HAIYORE),
	MORE_FANS(FactionType.HAIYORE),
	HIDING_BUSH(FactionType.HAIYORE),

	// Nyaruko
	SHARP_TONGUE(null),
	// Ctuko
	WITH_MY_BELOVED(null),

	// The Black Knights
	STONE_HEART(FactionType.THE_BLACK_KNIGHTS),
	NEGOTIATOR(FactionType.THE_BLACK_KNIGHTS),
	MARKET_FLOODING(FactionType.THE_BLACK_KNIGHTS),
	GENERAL_ALARM(FactionType.THE_BLACK_KNIGHTS),
	UNDERCOVER_AGENT(FactionType.THE_BLACK_KNIGHTS),
	HOLOCAUST(FactionType.THE_BLACK_KNIGHTS),

	// Lelouch
	BACK_TO_SKETCH(null);

	private final FactionType faction;

	CapacityType(FactionType faction) {
		this.faction = faction;
	}

	public FactionType getFaction() {
		return this.faction;
	}
}
