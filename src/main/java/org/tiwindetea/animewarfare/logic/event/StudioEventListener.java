package org.tiwindetea.animewarfare.logic.event;

import java.util.EventListener;

public interface StudioEventListener extends EventListener {
	void handleStudioAddedEvent(StudioAddedEvent event);
}
