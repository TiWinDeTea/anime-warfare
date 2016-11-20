package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.Player;

public class AskFirstPlayerEvent implements Event<AskFirstPlayerListener> {
	public final Player lastPlayer;

	public AskFirstPlayerEvent(Player lastPlayer) {
		this.lastPlayer = lastPlayer;
	}

	@Override
	public void notify(AskFirstPlayerListener listener) {
		listener.askFirstPlayerEvent(this);
	}
}
