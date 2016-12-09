package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

import java.util.EventListener;

public abstract class RemoveUnitEvent<T extends EventListener> implements Event<T> {
	private final int player;
	private final int numberOfUnits;

	public RemoveUnitEvent(int player, int numberOfUnits) {
		this.player = player;
		this.numberOfUnits = numberOfUnits;
	}

	public int getPlayer() {
		return this.player;
	}

	public int getNumberOfUnits() {
		return this.numberOfUnits;
	}
}
