package org.tiwindetea.animewarfare.logic.capacity.events;

import java.util.EventListener;

public interface ActivateFlyingStudioEventListener extends EventListener {
	void onFlyingStudioActivationRequest(ActivateFlyingStudioEvent event);
}
