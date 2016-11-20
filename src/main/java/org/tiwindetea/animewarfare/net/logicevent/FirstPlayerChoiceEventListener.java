package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface FirstPlayerChoiceEventListener extends EventListener {
	void handleFirstPlayer(FirstPlayerChoiceEvent event);
}
