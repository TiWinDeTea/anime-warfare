package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class MascotToCaptureChoiceEvent implements Event<MascotToCaptureChoiceEventListener> {
	private final int playerID;
	private final int mascotID;

	public MascotToCaptureChoiceEvent(int playerID, int mascotID) {
		this.playerID = playerID;
		this.mascotID = mascotID;
	}

	@Override
	public void notify(MascotToCaptureChoiceEventListener listener) {
		listener.handleMascotToCaptureChoiceEvent(this);
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public int getMascotID() {
		return this.mascotID;
	}
}
