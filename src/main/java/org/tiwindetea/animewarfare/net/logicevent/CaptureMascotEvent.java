package org.tiwindetea.animewarfare.net.logicevent;

public class CaptureMascotEvent extends ActionEvent<CaptureMascotEventListener> {
	private final int huntedPlayerID;
	private final int zone;
	private final int hunterUnit;

	public CaptureMascotEvent(int playerID, int huntedPlayerID, int hunterUnit, int zone) {
		super(playerID);
		this.huntedPlayerID = huntedPlayerID;
		this.hunterUnit = hunterUnit;
		this.zone = zone;
	}

	@Override
	public void notify(CaptureMascotEventListener listener) {
		listener.handleCaptureMascotEvent(this);
	}

	public int getHuntedPlayerID() {
		return this.huntedPlayerID;
	}

	public int getHunterUnit() {
		return this.hunterUnit;
	}

	public int getZone() {
		return this.zone;
	}
}
