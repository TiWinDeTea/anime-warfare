package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

public class PhaseChangedEvent implements Event<PhaseChangedEventListener> {
	public enum Phase {
		ACTION,
		PLAYER_SELECTION,
		STAFF_HIRING,
		MARKETING
	}

	private final Phase newPhase;

	public PhaseChangedEvent(Phase newPhase) {
		this.newPhase = newPhase;
	}

	public Phase getNewPhase() {
		return newPhase;
	}

	@Override
	public void notify(PhaseChangedEventListener listener) {
		listener.handlePhaseChanged(this);
	}
}

