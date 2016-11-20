package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.Player;

import java.util.List;

public class AskFirstPlayerEvent implements Event<AskFirstPlayerListener> {
	public final Player lastPlayer;
	public final List<Player> drawPlayers;

	public AskFirstPlayerEvent(Player lastPlayer, List<Player> drawPlayers) {
		this.lastPlayer = lastPlayer;
		this.drawPlayers = drawPlayers;
	}

	@Override
	public void notify(AskFirstPlayerListener listener) {
		listener.askFirstPlayerEvent(this);
	}
}
