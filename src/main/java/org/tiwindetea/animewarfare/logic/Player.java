package org.tiwindetea.animewarfare.logic;

public class Player {
	private int fanNumber;
	private int staffAvailable;
	private final BuffManager buffManager = new BuffManager();

	public int getStaffAvailable() {
		return this.staffAvailable;
	}
}
