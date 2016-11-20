package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface FirstPlayerChoiceListener extends EventListener {
	void handleFirstPlayer(FirstPlayerChoiceEvent event);
}
