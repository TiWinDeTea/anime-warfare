package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface StartBattleEventListener extends EventListener {
	void handleBattleEvent(StartBattleEvent event);
}
