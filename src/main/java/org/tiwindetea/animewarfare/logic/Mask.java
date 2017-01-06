package org.tiwindetea.animewarfare.logic;

public class Mask {
	final int value;
	final boolean nulled;

	public Mask(int value, boolean nulled) {
		this.value = value;
		this.nulled = nulled;
	}

	public Mask(int value) {
		this(value, false);
	}
}
