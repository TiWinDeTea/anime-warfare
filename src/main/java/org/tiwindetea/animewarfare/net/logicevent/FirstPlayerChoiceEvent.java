package org.tiwindetea.animewarfare.net.logicevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.Player;

public class FirstPlayerChoiceEvent implements Event<FirstPlayerChoiceListener> {
	public final Player firstPlayer;

	public FirstPlayerChoiceEvent(Player player) {
		this.firstPlayer = player;
	}

	@Override
	public void notify(FirstPlayerChoiceListener listener) {
		listener.handleFirstPlayer(this);
	}
}
