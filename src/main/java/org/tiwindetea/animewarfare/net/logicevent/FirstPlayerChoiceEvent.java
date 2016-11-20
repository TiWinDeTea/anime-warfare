package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;

public class FirstPlayerChoiceEvent implements Event<FirstPlayerChoiceEventListener> {
	private final int firstPlayer;

	public FirstPlayerChoiceEvent(int player) {
		this.firstPlayer = player;
	}

	@Override
	public void notify(FirstPlayerChoiceEventListener listener) {
		listener.handleFirstPlayer(this);
	}

	public int getFirstPlayer() {
		return this.firstPlayer;
	}
}
