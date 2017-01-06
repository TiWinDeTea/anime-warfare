package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface BadBookCapacityChoseEventListener extends EventListener {
	void handleCapacityChose(BadBookCapacityChoseEvent event);
}
