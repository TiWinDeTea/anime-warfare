package org.tiwindetea.animewarfare.net.networkevent;

import java.util.EventListener;

public interface StudioControllerChangedNeteventListener extends EventListener {
	void handleStudioControllerChanged(StudioControllerChangedNetevent event);
}
