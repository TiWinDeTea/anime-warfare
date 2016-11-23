package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface FirstPlayerSelectedEventListener extends EventListener {
	void firstPlayerSelected(FirstPlayerSelectedEvent event);
}
