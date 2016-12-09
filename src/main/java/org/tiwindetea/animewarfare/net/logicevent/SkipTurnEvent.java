package org.tiwindetea.animewarfare.net.logicevent;

public class SkipTurnEvent extends ActionEvent<SkipTurnEventListener> {
	public SkipTurnEvent(int playerID) {
		super(playerID);
	}

	@Override
	public void notify(SkipTurnEventListener listener) {
		listener.handleSkipTurnEvent(this);
	}
}
