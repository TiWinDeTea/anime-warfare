package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

import java.util.Collections;
import java.util.List;

public class AskFirstPlayerEvent implements Event<AskFirstPlayerListener> {
	private final int lastPlayer;
	private final List<Integer> drawPlayers;

	public AskFirstPlayerEvent(int lastPlayer, List<Integer> drawPlayers) {
		this.lastPlayer = lastPlayer;
		this.drawPlayers = Collections.unmodifiableList(drawPlayers);
	}

	@Override
	public void notify(AskFirstPlayerListener listener) {
		listener.askFirstPlayerEvent(this);
	}

	public int getLastPlayer() {
		return this.lastPlayer;
	}

	public List<Integer> getDrawPlayers() {
		return this.drawPlayers;
	}
}
