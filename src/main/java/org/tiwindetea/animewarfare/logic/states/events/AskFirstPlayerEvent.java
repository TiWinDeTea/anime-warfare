package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

import java.util.Collections;
import java.util.List;

public class AskFirstPlayerEvent implements Event<AskFirstPlayerEventListener> {
	private final int lastPlayer;
	private final List<Integer> drawPlayers;

	public AskFirstPlayerEvent(int lastPlayer, List<Integer> drawPlayers) {
		this.lastPlayer = lastPlayer;
		this.drawPlayers = Collections.unmodifiableList(drawPlayers);
	}

	@Override
	public void notify(AskFirstPlayerEventListener listener) {
		listener.askFirstPlayerEvent(this);
	}

	public int getLastPlayer() {
		return this.lastPlayer;
	}

	public List<Integer> getDrawPlayers() {
		return this.drawPlayers;
	}
}
