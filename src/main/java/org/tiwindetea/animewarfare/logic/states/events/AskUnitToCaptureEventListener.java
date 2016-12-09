package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface AskUnitToCaptureEventListener extends EventListener {
	void askUnitToCaptureEvent(AskMascotToCaptureEvent event);
}
