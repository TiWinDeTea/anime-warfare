package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface SkipTurnEventListener extends EventListener {
	void handleSkipTurnEvent(SkipTurnEvent event);
}
