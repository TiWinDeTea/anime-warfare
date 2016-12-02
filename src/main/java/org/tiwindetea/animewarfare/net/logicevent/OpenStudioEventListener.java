package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface OpenStudioEventListener extends EventListener {
	void handleOpenStudioEvent(OpenStudioEvent event);
}
