package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface BattleStartedEventListener extends EventListener {
	void handleBattleStartedEvent(BattleStartedEvent event);
}
