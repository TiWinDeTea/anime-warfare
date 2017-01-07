package org.tiwindetea.animewarfare.net.logicevent;

public class FlyingStudioZoneChoiceEvent extends ActionEvent<FlyingStudioZoneChoiceEventListener> {
	private final int ID;

	public FlyingStudioZoneChoiceEvent(int playerID, int ID) {
		super(playerID);

		this.ID = ID;
	}

	@Override
	public void notify(FlyingStudioZoneChoiceEventListener listener) {
		listener.handleZoneChoice(this);
	}

	public int getID() {
		return this.ID;
	}
}
