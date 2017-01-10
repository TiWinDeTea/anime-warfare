package org.tiwindetea.animewarfare.logic.events;

import java.util.EventListener;

public interface StudioEventListener extends EventListener {
	void handleStudioAddedEvent(StudioEvent event);

	void handleStudioRemovedEvent(StudioEvent event);

	void handleStudioBuiltOrDestroyed(StudioEvent studioEvent);
}
