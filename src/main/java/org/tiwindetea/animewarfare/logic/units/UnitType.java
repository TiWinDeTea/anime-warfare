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
