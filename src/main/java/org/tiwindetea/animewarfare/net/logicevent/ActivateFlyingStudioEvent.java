package org.tiwindetea.animewarfare.net.logicevent;

public class ActivateFlyingStudioEvent extends ActionEvent<ActivateFlyingStudioEventListener> {
	public ActivateFlyingStudioEvent(int playerID) {
		super(playerID);
	}

	@Override
	public void notify(ActivateFlyingStudioEventListener listener) {
		listener.onFlyingStudioActivationRequest(this);
	}

}
