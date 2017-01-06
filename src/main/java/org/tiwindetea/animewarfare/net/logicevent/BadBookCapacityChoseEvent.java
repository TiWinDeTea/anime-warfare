package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;

public class BadBookCapacityChoseEvent implements Event<BadBookCapacityChoseEventListener> {
	private final CapacityName name;

	public BadBookCapacityChoseEvent(CapacityName name) {
		this.name = name;
	}

	@Override
	public void notify(BadBookCapacityChoseEventListener listener) {
		listener.handleCapacityChose(this);
	}

	public CapacityName getName() {
		return this.name;
	}
}
