package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.Player;

public class AskPlayingOrderEvent implements Event<AskPlayingOrderEventListener> {
	public final Player player;

	public AskPlayingOrderEvent(Player player) {
		this.player = player;
	}

	@Override
	public void notify(AskPlayingOrderEventListener listener) {
		listener.askPlayingOrder(this);
	}
}
