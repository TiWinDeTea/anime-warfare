package org.tiwindetea.animewarfare.logic.capacity.events;

import org.lomadriel.lfc.event.Event;

public class FlyingStudioZoneChoiceEvent implements Event<FlyingStudioZoneChoiceEventListener> {
	private final int ID;

	public FlyingStudioZoneChoiceEvent(int ID) {
		this.ID = ID;
	}

	@Override
	public void notify(FlyingStudioZoneChoiceEventListener listener) {
		listener.handleZoneChoice(this);
	}

	public int getID() {
		return this.ID;
	}
}
