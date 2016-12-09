package org.tiwindetea.animewarfare.net.logicevent;

public class OpenStudioEvent extends ActionEvent<OpenStudioEventListener> {
	private final int zone;

	public OpenStudioEvent(int playerID, int zone) {
		super(playerID);
		this.zone = zone;
	}

	@Override
	public void notify(OpenStudioEventListener listener) {
		listener.handleOpenStudioEvent(this);
	}

	public int getZone() {
		return this.zone;
	}
}
