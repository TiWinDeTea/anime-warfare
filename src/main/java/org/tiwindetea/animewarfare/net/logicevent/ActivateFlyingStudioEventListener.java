package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface ActivateFlyingStudioEventListener extends EventListener {
	void onFlyingStudioActivationRequest(ActivateFlyingStudioEvent event);
}
