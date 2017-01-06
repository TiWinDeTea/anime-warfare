package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class NumberOfUnitiesToReconfortEvent implements Event<NumberOfUnitiesToReconfortEventListener> {
	private final int number;

	public NumberOfUnitiesToReconfortEvent(int number) {
		this.number = number;
	}

	@Override
	public void notify(NumberOfUnitiesToReconfortEventListener listener) {
		listener.handleNumberOfUnitiesToReconfortEvent(this);
	}

	public int getNumber() {
		return this.number;
	}
}
