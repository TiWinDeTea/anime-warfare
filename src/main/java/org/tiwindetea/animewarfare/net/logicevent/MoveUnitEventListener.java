package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface MoveUnitEventListener extends EventListener {
	void handleMoveUnitEvent(MoveUnitEvent event);
}
