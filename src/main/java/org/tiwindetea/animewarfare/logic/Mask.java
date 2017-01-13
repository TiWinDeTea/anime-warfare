package org.tiwindetea.animewarfare.logic;

public class Mask {
	public int value;
	public boolean nulled;

	public Mask(int value, boolean nulled) {
		this.value = value;
		this.nulled = nulled;
	}

	public Mask(int value) {
		this(value, false);
	}
}
