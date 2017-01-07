package org.tiwindetea.animewarfare.net.logicevent;

import org.tiwindetea.animewarfare.logic.capacity.CapacityName;

public class BadBookCapacityChoseEvent extends ActionEvent<BadBookCapacityChoseEventListener> {
	private final CapacityName name;

	public BadBookCapacityChoseEvent(int playerID, CapacityName name) {
		super(playerID);
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
