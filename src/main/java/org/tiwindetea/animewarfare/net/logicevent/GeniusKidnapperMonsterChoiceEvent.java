package org.tiwindetea.animewarfare.net.logicevent;

public class GeniusKidnapperMonsterChoiceEvent extends ActionEvent<GeniusKidnapperMonsterChoiceEventListener> {
	private final int zoneID;
	private final int unitID;

	public GeniusKidnapperMonsterChoiceEvent(int playerID, int zoneID, int unitID) {
		super(playerID);
		this.zoneID = zoneID;
		this.unitID = unitID;
	}

	@Override
	public void notify(GeniusKidnapperMonsterChoiceEventListener listener) {
		listener.handleGeniusKidnapperMonsterChoiceEvent(this);
	}

	public int getUnitID() {
		return this.unitID;
	}

	public int getZoneID() {
		return this.zoneID;
	}
}
