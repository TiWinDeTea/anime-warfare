package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface PhaseChangedEventListener extends EventListener {
	void handlePhaseChanged(PhaseChangedEvent event);
}
