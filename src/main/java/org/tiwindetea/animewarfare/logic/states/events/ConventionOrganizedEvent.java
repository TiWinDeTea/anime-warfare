package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.Player;

public class ConventionOrganizedEvent implements Event<ConventionOrganizedEventListener> {
	private final Player player;

	public ConventionOrganizedEvent(Player currentPlayer) {
		this.player = currentPlayer;
	}

	@Override
	public void notify(ConventionOrganizedEventListener listener) {
		listener.onConventionOrganized(this);
	}

	public Player getPlayer() {
		return this.player;
	}
}
